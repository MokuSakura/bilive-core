//package org.mokusakura.bilive.core.client;
//
//import com.alibaba.fastjson.JSONObject;
//import lombok.extern.log4j.Log4j2;
//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.handshake.ServerHandshake;
//import org.mokusakura.bilive.core.api.model.RoomInfo;
//import org.mokusakura.bilive.core.api.model.RoomInit;
//import org.mokusakura.bilive.core.event.MessageReceivedEvent;
//import org.mokusakura.bilive.core.event.StatusChangedEvent;
//import org.mokusakura.bilive.core.model.BilibiliWebSocketHeader;
//import org.mokusakura.bilive.core.model.BilibiliWebSocketHeader.ActionType;
//import org.mokusakura.bilive.core.model.BilibiliWebSocketHeader.ProtocolVersion;
//
//import java.io.ByteArrayOutputStream;
//import java.net.URI;
//import java.nio.ByteBuffer;
//import java.nio.charset.StandardCharsets;
//import java.util.*;
//import java.util.concurrent.*;
//import java.util.function.BiConsumer;
//import java.util.function.Consumer;
//import java.util.zip.DataFormatException;
//import java.util.zip.Inflater;
//
///**
// * @author MokuSakura
// * <p>
// * This is not completed yet.
// * </p>
// */
//@Log4j2
//public class SocketDanmakuClient extends WebSocketClient implements DanmakuClient {
//    public static final int HEADER_SIZE = 16;
//    private final Map<Short, BiConsumer<BilibiliWebSocketHeader, ByteBuffer>> messageConverters;
//    private final Set<Consumer<MessageReceivedEvent>> messageReceivedHandlers;
//    private final Set<Consumer<StatusChangedEvent>> statusChangedListeners;
//    private final RoomInfo roomInfo;
//    private final RoomInit roomInit;
//    private final URI uri;
//    private final ThreadPoolExecutor threadPoolExecutor;
//    private final ScheduledThreadPoolExecutor timer;
//
//
//    public SocketDanmakuClient(URI uri, RoomInit roomInit, RoomInfo roomInfo) {
//        super(uri);
//        this.uri = uri;
//        this.roomInit = roomInit;
//        this.roomInfo = roomInfo;
//        messageConverters = new HashMap<>();
//        messageConverters.put(ProtocolVersion.PureJson, this::handlePureJson);
//        messageConverters.put(ProtocolVersion.CompressedBuffer, this::handleCompressedData);
//        timer = new ScheduledThreadPoolExecutor(10, (ThreadFactory) Thread::new);
//        messageReceivedHandlers = new LinkedHashSet<>();
//        statusChangedListeners = new LinkedHashSet<>();
//        threadPoolExecutor = new ThreadPoolExecutor(10, 10, 1000, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
//
//    }
//
//
//    @Override
//    public void onOpen(ServerHandshake serverHandshake) {
//        try {
//            sendHelloAsync().get();
//            sendHeartBeanAsync().get();
//            timer.scheduleAtFixedRate(this::sendHeartBeanAsync, 10, 10, TimeUnit.SECONDS);
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    public void onMessage(ByteBuffer bytes) {
////        log.debug(new String(bytes.array(),StandardCharsets.UTF_8));
//        //FIXME Now the whole data package may contains more than one header-body data.
//        //Meaning I may get something like [header,body,header.body...]
//        //Need to find a way to solve this.
//        var decodedHeader = BilibiliWebSocketHeader.newInstance(bytes.array(), true);
//        var handler = messageConverters.get(decodedHeader.getProtocolVersion());
//        Objects.requireNonNullElse(handler, (a, b) -> {}).accept(decodedHeader, bytes);
//    }
//
//    @Override
//    public void onMessage(String s) {
//        onMessage(ByteBuffer.wrap(s.getBytes(StandardCharsets.UTF_8)));
//    }
//
//    @Override
//    public void onClose(int i, String s, boolean b) {
//        log.info("Connection to {}:{} of Room {} is closed", uri.getHost(), uri.getPort(), roomInit.getRoomId());
//        this.timer.shutdownNow();
//    }
//
//    @Override
//    public void onError(Exception e) {
//        log.error(e.getMessage());
//        log.error(Arrays.toString(e.getStackTrace()));
//        log.debug("error");
//        this.close();
//    }
//
//    protected Future<Boolean> sendHelloAsync() {
//        JSONObject body = new JSONObject();
//        body.put("uid", 0);
//        body.put("roomid", roomInit.getRoomId());
//        body.put("protover", 0);
//        body.put("platform", "web");
//        body.put("clientver", "2.6.25");
//        body.put("type", 2);
//        return sendMessageAsync(ActionType.Hello, body.toJSONString());
//    }
//
//    protected Future<Boolean> sendHeartBeanAsync() {
//        return sendMessageAsync(ActionType.HeartBeat, "");
//    }
//
//    protected Future<Boolean> sendMessageAsync(int actionType, String body) {
//        return threadPoolExecutor.submit(() -> {
//            log.debug("send " + actionType);
//            String cbody = body == null ? "" : body;
//            var payload = cbody.getBytes(StandardCharsets.UTF_8);
//            var size = payload.length + HEADER_SIZE;
//            var outputStream = new ByteArrayOutputStream();
//            BilibiliWebSocketHeader header = BilibiliWebSocketHeader.newInstance(size, ProtocolVersion.ClientSend,
//                                                                                 actionType);
//            outputStream.write(header.array());
//            outputStream.write(payload);
//            super.send(outputStream.toByteArray());
//            return true;
//        });
//
//    }
//
//    protected void handlePureJson(BilibiliWebSocketHeader header, ByteBuffer byteBuffer) {
//        log.debug(new String(byteBuffer.array()));
//        var length = header.getTotalLength() - header.getHeaderLength();
//        assert length <= Integer.MAX_VALUE;
//        var data = byteBuffer.array();
//        var bodyData = Arrays.copyOfRange(data, BilibiliWebSocketHeader.BODY_OFFSET, data.length);
//        var json = new String(bodyData, StandardCharsets.UTF_8);
//        log.debug(json);
//    }
//
//    protected void handleCompressedData(BilibiliWebSocketHeader header, ByteBuffer byteBuffer) {
//        var compressedData = byteBuffer.array();
//        Inflater inflater = new Inflater();
//        long length = header.getTotalLength() - header.getHeaderLength();
//        assert length <= Integer.MAX_VALUE;
//        byte[] buffer = new byte[1024];
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        inflater.setInput(compressedData, BilibiliWebSocketHeader.BODY_OFFSET,
//                          compressedData.length - header.getHeaderLength());
//        int inflateLength;
//        try {
//            while ((inflateLength = inflater.inflate(buffer, 0, buffer.length)) != 0) {
//                baos.write(buffer, 0, inflateLength);
//            }
//            inflater.end();
//        } catch (DataFormatException e) {
//            log.error(e.getMessage());
//            return;
//        }
//        handlePureJson(header, ByteBuffer.wrap(baos.toByteArray()));
//    }
//}
