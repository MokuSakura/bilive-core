package org.mokusakura.bilive.core;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.mokusakura.bilive.core.event.DanmakuReceivedEvent;
import org.mokusakura.bilive.core.event.LiveBeginEvent;
import org.mokusakura.bilive.core.event.LiveEndEvent;
import org.mokusakura.bilive.core.event.OtherEvent;
import org.mokusakura.bilive.core.model.*;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * @author MokuSakura
 * <p>
 * This is not completed yet.
 * </p>
 */
@Slf4j
public class SocketDanmakuClient extends WebSocketClient implements DanmakuClient {
    public static final int HEADER_SIZE = 16;
    private final Map<ProtocolVersion, BiConsumer<WebSocketHeader, ByteBuffer>> messageHandlers;
    private final Set<Consumer<DanmakuReceivedEvent>> danmakuReceivedHandlers;
    private final Set<Consumer<LiveEndEvent>> danmakuClosedHandlers;
    private final Set<Consumer<LiveBeginEvent>> liveBeginHandlers;
    private final Set<Consumer<OtherEvent>> otherHandlers;
    private final RoomInfo roomInfo;
    private final RoomInit roomInit;
    private final URI uri;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final ScheduledThreadPoolExecutor timer;


    public SocketDanmakuClient(URI uri, RoomInit roomInit, RoomInfo roomInfo) {
        super(uri);
        this.uri = uri;
        this.roomInit = roomInit;
        this.roomInfo = roomInfo;
        messageHandlers = new HashMap<>();
        messageHandlers.put(ProtocolVersion.PureJson, this::handlePureJson);
        messageHandlers.put(ProtocolVersion.CompressedBuffer, this::handleCompressedData);
        timer = new ScheduledThreadPoolExecutor(10, (ThreadFactory) Thread::new);
        danmakuReceivedHandlers = new LinkedHashSet<>();
        danmakuClosedHandlers = new LinkedHashSet<>();
        liveBeginHandlers = new LinkedHashSet<>();
        otherHandlers = new LinkedHashSet<>();
        threadPoolExecutor = new ThreadPoolExecutor(10, 10, 1000, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));

    }

    @Override
    public Collection<Consumer<DanmakuReceivedEvent>> danmakuReceivedHandlers() {
        return danmakuReceivedHandlers;
    }

    @Override
    public void addReceivedHandlers(Consumer<DanmakuReceivedEvent> consumer) {
        danmakuReceivedHandlers.add(consumer);
    }

    @Override
    public Collection<Consumer<LiveEndEvent>> liveEndHandlers() {
        return this.danmakuClosedHandlers;
    }

    @Override
    public void addLiveEndHandlers(Consumer<LiveEndEvent> consumer) {
        this.danmakuClosedHandlers.add(consumer);
    }

    @Override
    public Collection<Consumer<LiveBeginEvent>> liveBeginHandlers() {
        return liveBeginHandlers;
    }

    @Override
    public void addLiveBeginHandler(Consumer<LiveBeginEvent> consumer) {
        liveBeginHandlers.add(consumer);
    }

    @Override
    public Collection<Consumer<OtherEvent>> otherHandlers() {
        return otherHandlers;
    }

    @Override
    public void addOtherHandlers(Consumer<OtherEvent> consumer) {
        otherHandlers.add(consumer);
    }

    @Override
    public void connect(int roomId) {
        if (!roomInit.getRoomId().equals(roomId)) {
            throw new IllegalArgumentException();
        }
        try {
            super.connectBlocking();
            log.info("Connect to {}:{} of Room {}, short id {}", uri.getHost(), uri.getPort(), roomInit.getRoomId(),
                     roomInit.getShortId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //FIXME Why always false here?
//        if (super.isConnecting()) {
//            log.info("Connect to {}:{} of Room {}, short id {}", uri.getHost(), uri.getPort(), roomInit.getRoomId(),roomInit.getShortId());
//        } else {
//            log.error("Fail to connect to {}:{} of Room {}, short id {}", uri.getHost(), uri.getPort(), roomInit.getRoomId(),roomInit.getShortId());
//        }
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        try {
            sendHelloAsync().get();
            sendHeartBeanAsync().get();
            timer.scheduleAtFixedRate(this::sendHeartBeanAsync, 10, 10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMessage(ByteBuffer bytes) {
//        log.debug(new String(bytes.array(),StandardCharsets.UTF_8));
        //FIXME Now the whole data package may contains more than one header-body data.
        //Meaning I may get something like [header,body,header.body...]
        //Need to find a way to solve this.
        var decodedHeader = WebSocketHeader.newInstance(bytes.array(), true);
        var handler = messageHandlers.get(decodedHeader.getProtocolVersion());
        Objects.requireNonNullElse(handler, (a, b) -> {}).accept(decodedHeader, bytes);
    }

    @Override
    public void onMessage(String s) {
        onMessage(ByteBuffer.wrap(s.getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("Connection to {}:{} of Room {} is closed", uri.getHost(), uri.getPort(), roomInit.getRoomId());
        this.timer.shutdownNow();
    }

    @Override
    public void onError(Exception e) {
        log.error(e.getMessage());
        log.error(Arrays.toString(e.getStackTrace()));
        log.debug("error");
        this.close();
    }

    protected Future<Boolean> sendHelloAsync() {
        JSONObject body = new JSONObject();
        body.put("uid", 0);
        body.put("roomid", roomInit.getRoomId());
        body.put("protover", 0);
        body.put("platform", "web");
        body.put("clientver", "2.6.25");
        body.put("type", 2);
        return sendMessageAsync(ActionType.Hello, body.toJSONString());
    }

    protected Future<Boolean> sendHeartBeanAsync() {
        return sendMessageAsync(ActionType.HeartBeat, "");
    }

    protected Future<Boolean> sendMessageAsync(ActionType actionType, String body) {
        return threadPoolExecutor.submit(() -> {
            log.debug("send " + actionType.name());
            String cbody = body == null ? "" : body;
            var payload = cbody.getBytes(StandardCharsets.UTF_8);
            var size = payload.length + HEADER_SIZE;
            var outputStream = new ByteArrayOutputStream();
            WebSocketHeader header = WebSocketHeader.newInstance(size, ProtocolVersion.ClientSend, actionType);
            outputStream.write(header.array());
            outputStream.write(payload);
            super.send(outputStream.toByteArray());
            return true;
        });

    }

    protected void handlePureJson(WebSocketHeader header, ByteBuffer byteBuffer) {
        log.debug(new String(byteBuffer.array()));
        var length = header.getTotalLength() - header.getHeaderLength();
        assert length <= Integer.MAX_VALUE;
        var data = byteBuffer.array();
        var bodyData = Arrays.copyOfRange(data, WebSocketHeader.BODY_OFFSET, data.length);
        var json = new String(bodyData, StandardCharsets.UTF_8);
        log.debug(json);
    }

    protected void handleCompressedData(WebSocketHeader header, ByteBuffer byteBuffer) {
        var compressedData = byteBuffer.array();
        Inflater inflater = new Inflater();
        long length = header.getTotalLength() - header.getHeaderLength();
        assert length <= Integer.MAX_VALUE;
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        inflater.setInput(compressedData, WebSocketHeader.BODY_OFFSET,
                          compressedData.length - header.getHeaderLength());
        int inflateLength = 0;
        try {
            while ((inflateLength = inflater.inflate(buffer, 0, buffer.length)) != 0) {
                baos.write(buffer, 0, inflateLength);
            }
            inflater.end();
        } catch (DataFormatException e) {
            log.error(e.getMessage());
            return;
        }
        handlePureJson(header, ByteBuffer.wrap(baos.toByteArray()));
    }
}
