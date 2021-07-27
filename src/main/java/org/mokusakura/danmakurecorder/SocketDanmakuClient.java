package org.mokusakura.danmakurecorder;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.mokusakura.danmakurecorder.event.DanmakuClientClosedEvent;
import org.mokusakura.danmakurecorder.event.DanmakuReceivedEvent;
import org.mokusakura.danmakurecorder.model.RoomInfo;
import org.mokusakura.danmakurecorder.model.RoomInit;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * @author MokuSakura
 */
@Slf4j
public class SocketDanmakuClient extends WebSocketClient implements DanmakuClient {
    public static final int HEADER_SIZE = 16;
    private final Set<Consumer<DanmakuReceivedEvent>> danmakuReceivedHandlers;
    private final Set<Consumer<DanmakuClientClosedEvent>> danmakuClosedHandlers;
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
        timer = new ScheduledThreadPoolExecutor(10, (ThreadFactory) Thread::new);
        danmakuReceivedHandlers = new LinkedHashSet<>();
        danmakuClosedHandlers = new LinkedHashSet<>();
        threadPoolExecutor = new ThreadPoolExecutor(10, 10, 1000, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));

        try {
            super.connectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
    public Collection<Consumer<DanmakuClientClosedEvent>> danmakuClientClosedHandlers() {
        return this.danmakuClosedHandlers;
    }

    @Override
    public void addClosedHandlers(Consumer<DanmakuClientClosedEvent> consumer) {
        this.danmakuClosedHandlers.add(consumer);
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
        if (super.isConnecting()) {
            log.info("Connect to {}:{} of Room {}", uri.getHost(), uri.getPort(), roomInit.getRoomId());
        } else {
            log.error("Fail to connect to {}:{} of Room {}", uri.getHost(), uri.getPort(), roomInit.getRoomId());
        }
    }

    @Override
    public void onMessage(String s) {
        onMessage(ByteBuffer.wrap(s.getBytes()));
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
    }


    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("Connection to {}:{} of Room {} is closed", uri.getHost(), uri.getPort(), roomInit.getRoomId());
        DanmakuClientClosedEvent event = new DanmakuClientClosedEvent(this, this.uri, this.roomInfo, this.roomInit);
        for (var consumer : danmakuClosedHandlers) {
            threadPoolExecutor.execute(() -> consumer.accept(event));
        }
    }

    @Override
    public void onError(Exception e) {
        log.error(Arrays.toString(e.getStackTrace()));
        log.debug("error");
    }

    private Future<Boolean> sendHelloAsync() {
        JSONObject body = new JSONObject();
        body.put("uid", 0);
        body.put("roomid", roomInit.getRoomId());
        body.put("protover", 1);
        body.put("platform", "web");
        body.put("clientver", "1.4.0");
        body.put("type", 2);
        return sendMessageAsync(SendActionType.Hello, body.toJSONString());
    }

    private Future<Boolean> sendHeartBeanAsync() {
        return sendMessageAsync(SendActionType.HeartBeat, "");
    }

    private Future<Boolean> sendMessageAsync(SendActionType actionType, String body) {
        return threadPoolExecutor.submit(() -> {
            log.debug("send " + actionType.name());
            var payload =
                    body == null ? "".getBytes(StandardCharsets.UTF_8) : body.getBytes(StandardCharsets.UTF_8);
            var size = payload.length + HEADER_SIZE;
            var headBuffer = ByteBuffer.allocate(16);
            var outputStream = new ByteArrayOutputStream();
            // Head information
            // 0-3 size
            // 4-5 unknown always 16
            // 6-7 unknown always 1
            // 8-11 action type
            // 12-15 unknown always 1
            headBuffer.putInt(0, size);
            headBuffer.putShort(4, (short) 16);
            headBuffer.putShort(6, (short) 1);
            headBuffer.putInt(8, actionType.getValue());
            headBuffer.putInt(12, 1);
            outputStream.write(headBuffer.array());
            outputStream.write(payload);
            send(outputStream.toByteArray());
            return true;
        });

    }

}
