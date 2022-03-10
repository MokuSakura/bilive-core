package org.mokusakura.bilive.core.client;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.mokusakura.bilive.core.api.BilibiliLiveApiClient;
import org.mokusakura.bilive.core.api.model.DanmakuServerInfo;
import org.mokusakura.bilive.core.api.model.RoomInit;
import org.mokusakura.bilive.core.event.MessageReceivedEvent;
import org.mokusakura.bilive.core.event.StatusChangedEvent;
import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;
import org.mokusakura.bilive.core.exception.NoRoomFoundException;
import org.mokusakura.bilive.core.factory.BilibiliMessageFactory;
import org.mokusakura.bilive.core.model.*;
import org.mokusakura.bilive.core.model.BilibiliWebSocketHeader.ActionType;
import org.mokusakura.bilive.core.model.BilibiliWebSocketHeader.ProtocolVersion;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author MokuSakura
 */
@Log4j2
public class TcpDanmakuClient extends AbstractDanmakuClient {
    public static final int TRY_TIMES = 3;
    private final ScheduledExecutorService timer;


    private final ThreadPoolExecutor threadPoolExecutor;
    private final BilibiliMessageFactory bilibiliMessageFactory;
    private final Lock lock;
    private ScheduledFuture<?> heartBeatTask;
    private DanmakuServerInfo.HostListItem hostListItem;
    private boolean closed;
    private SocketChannel socketChannel;
    private RoomInit roomInit;
    private DanmakuServerInfo danmakuServerInfo;
    private final BilibiliLiveApiClient apiClient;


    public TcpDanmakuClient(BilibiliLiveApiClient apiClient, BilibiliMessageFactory bilibiliMessageFactory) {
        this.apiClient = apiClient;
        timer = new ScheduledThreadPoolExecutor(10);
        messageReceivedListeners = new LinkedHashSet<>();
        statusChangedHandlers = new LinkedHashSet<>();
        threadPoolExecutor = new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
        lock = new ReentrantLock();
        closed = true;

        this.bilibiliMessageFactory = bilibiliMessageFactory;
    }


    @Override
    public boolean isOpen() {
        return !closed;
    }

    @Override
    public void close() throws IOException {
        disconnectWithoutEvent();
    }

    @Override
    public Future<Boolean> sendMessageAsync(long roomId, BilibiliWebSocketFrame frame) {
        return threadPoolExecutor.submit(() -> {
            sendMessage(socketChannel, frame);
            return true;
        });
    }

    @Override
    protected long getTrueRoomId(long roomId) throws NoNetworkConnectionException, NoRoomFoundException {
        this.roomInit = apiClient.getRoomInit(roomId).getData();
        return roomInit.getRoomId();
    }

    @Override
    protected StatusChangedEvent createStatusChangedEvent(GenericStatusChangedModel message) {

        return new StatusChangedEvent(roomInit.getRoomId(), socketChannel,
                                      message.getStatus());
    }

    @Override
    protected MessageReceivedEvent createMessageEvent(GenericBilibiliMessage message) {
        return new MessageReceivedEvent(message, roomInit.getRoomId(), socketChannel);
    }

    @Override
    protected boolean connectToTrueRoomId(long roomId) throws NoNetworkConnectionException {
        try {
            lock.lock();
            if (this.isOpen()) {
                return false;
            }

            danmakuServerInfo = apiClient.getDanmakuServerInfo(roomId).getData();
            var hostListItems = Arrays.stream(danmakuServerInfo.getHostList())
                    .filter(hostListItem -> hostListItem.getHost() != null && hostListItem.getPort() != null)
                    .toArray(DanmakuServerInfo.HostListItem[]::new);
            if (hostListItems.length == 0) {
                return false;
            }
            hostListItem = hostListItems[0];
            var host = hostListItem.getHost();
            var port = hostListItem.getPort();
            try {
                // Connection is setup here
                socketChannel = SocketChannel.open();
                socketChannel.connect(new InetSocketAddress(host, port));
                sendHelloAsync().get();
                sendHeartBeatAsync().get();
                // Start read Thread
                new Thread(this::readThread).start();
                heartBeatTask = timer.scheduleAtFixedRate(() -> {
                    try {
                        this.sendHeartBeatAsync().get(10, TimeUnit.SECONDS);
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        log.error(Arrays.toString(e.getStackTrace()));
                    }
                }, 10, 30, TimeUnit.SECONDS);
                closed = false;
                log.info("Connect to {}:{} of Room {}, short id {}, with token {}", host, port, roomInit.getRoomId(),
                         roomInit.getShortId(), danmakuServerInfo.getToken());
                return true;
            } catch (IOException | InterruptedException | ExecutionException e) {
                log.error(e.getMessage());
            }
        } finally {
            lock.unlock();
        }
        return false;
    }

    protected Future<Boolean> sendHelloAsync() {
        HelloModel helloModel = HelloModel.newDefault(roomInit.getRoomId(), danmakuServerInfo.getToken());
        return sendMessageAsync(ActionType.Hello, JSONObject.toJSONString(helloModel));
    }

    protected Future<Boolean> sendHeartBeatAsync() {
        return sendMessageAsync(ActionType.HeartBeat, "");
    }

    protected Future<Boolean> sendMessageAsync(int actionType, String body) {
        return threadPoolExecutor.submit(() -> {
//            log.debug("send {}, {}", actionType, body);
            String cbody = body == null ? "" : body;
            var payload = cbody.getBytes(StandardCharsets.UTF_8);
            var size = payload.length + BilibiliWebSocketHeader.HEADER_LENGTH;
            BilibiliWebSocketHeader header = BilibiliWebSocketHeader.newInstance(size, ProtocolVersion.ClientSend,
                                                                                 actionType);
            socketChannel.write(ByteBuffer.wrap(header.array()));
            socketChannel.write(ByteBuffer.wrap(payload));
            return true;
        });

    }

    /**
     * <p>
     * This method will use {@code this.bilibiliMessageFactory}
     * to create {@link GenericBilibiliMessage} and call listeners.
     *
     * </p>
     *
     * @param frame An intact socket frame
     */
    protected void handleData(BilibiliWebSocketFrame frame) {
        List<GenericBilibiliMessage> messages = bilibiliMessageFactory.create(frame);
        callListeners(messages);
    }


    protected void readThread() {
        try {
            // All unused data is stored here
            ByteBuffer buffer = ByteBuffer.allocate(4096);

            while (true) {
                try {
                    lock.lock();
                    if (!this.isOpen()) {
                        return;
                    }
                    int readSize = socketChannel.read(buffer);
                    // socket is closed
                    if (readSize == -1) {
                        throw new IOException();
                    }
                    buffer.flip();
                    BilibiliWebSocketFrame frame;
                    ByteBuffer tempBuffer = buffer.asReadOnlyBuffer();
                    try {
                        frame = BilibiliWebSocketFrame.resolve(tempBuffer);
                        buffer.position(tempBuffer.position());
                    } catch (BufferUnderflowException e) {
                        buffer = ByteBuffer.allocate(buffer.capacity() * 2).put(buffer);
                        continue;
                    }
                    if (frame != null) {
                        handleData(frame);
                    }
                    // Although we don't know whether the type of the body,
                    // but we can make sure an intact header-body is correctly extracted.
                    // Then reset data.
                    buffer.compact();


                } finally {
                    lock.unlock();
                }
            }
        } catch (IOException e) {
            threadPoolExecutor.submit(() -> {
                try {
                    disconnectWithoutEvent();
                    tryReconnect();
                } catch (Exception ignored) {
                }
            });
        }
    }


    private void tryReconnect() throws IOException {

        int tryTimes = 0;
        while (tryTimes++ < TRY_TIMES) {
            log.info("Try reconnect to room {} short id {}. Try times: {}",
                     roomInit.getRoomId(),
                     roomInit.getShortId(),
                     tryTimes);
            try {
                if (connectToTrueRoomId(roomInit.getRoomId())) {
                    return;
                }
            } catch (NoNetworkConnectionException e) {
                try {
                    Thread.sleep(Duration.ofSeconds(10).toMillis());
                } catch (InterruptedException ignored) {

                }
            }
        }
        close();
        for (var handler : statusChangedHandlers) {
            StatusChangedEvent event = new StatusChangedEvent(roomInit.getRoomId(), socketChannel,
                                                              GenericStatusChangedModel.Status.DISCONNECT);
            handler.accept(event);
        }
    }

    private void disconnectWithoutEvent() throws IOException {
        try {
            lock.lock();
            if (closed) {
                return;
            }
            closed = true;
            log.info("Disconnect from {}:{} Room id {}, short id {}", hostListItem.getHost(), hostListItem.getPort(),
                     roomInit.getRoomId(), roomInit.getShortId());
            if (socketChannel != null) {
                socketChannel.close();
            }


        } catch (IOException e) {
            log.error("Error closing DanmakuClient");
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            throw e;
        } finally {
            socketChannel = null;
            heartBeatTask.cancel(true);
            lock.unlock();
        }
    }
}
