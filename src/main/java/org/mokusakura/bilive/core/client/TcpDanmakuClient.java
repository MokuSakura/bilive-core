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
import org.mokusakura.bilive.core.model.*;
import org.mokusakura.bilive.core.model.BilibiliWebSocketHeader.ActionType;
import org.mokusakura.bilive.core.model.BilibiliWebSocketHeader.ProtocolVersion;
import org.mokusakura.bilive.core.protocol.BilibiliLiveMessageProtocolResolver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * @author MokuSakura
 */
@Log4j2
public class TcpDanmakuClient implements DanmakuClient {
    public static final int TRY_TIMES = 3;
    private final ScheduledExecutorService timer;
    private final BilibiliLiveApiClient apiClient;
    private final Collection<Consumer<MessageReceivedEvent>> messageReceivedListeners;
    private final Collection<Consumer<StatusChangedEvent>> statusChangedHandlers;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final BilibiliLiveMessageProtocolResolver bilibiliLiveMessageProtocolResolver;
    //    private final Map<Short, BiConsumer<BilibiliWebSocketHeader, ByteBuffer>> messageConverters;
    private final Lock lock;
    private ScheduledFuture<?> heartBeatTask;
    private DanmakuServerInfo.HostListItem hostListItem;
    private boolean closed;
    private SocketChannel socketChannel;
    private RoomInit roomInit;
    private DanmakuServerInfo danmakuServerInfo;


    public TcpDanmakuClient(BilibiliLiveApiClient apiClient,
                            BilibiliLiveMessageProtocolResolver bilibiliLiveMessageProtocolResolver) {
        this.apiClient = apiClient;
        timer = new ScheduledThreadPoolExecutor(10);
        messageReceivedListeners = new LinkedHashSet<>();
        statusChangedHandlers = new LinkedHashSet<>();
        threadPoolExecutor = new ThreadPoolExecutor(50, 50, 1000, TimeUnit.SECONDS, new ArrayBlockingQueue<>(50));
        lock = new ReentrantLock();
        closed = true;

        this.bilibiliLiveMessageProtocolResolver = bilibiliLiveMessageProtocolResolver;
    }


    @Override
    public void addMessageReceivedListener(Consumer<MessageReceivedEvent> consumer) {
        messageReceivedListeners.add(consumer);
    }

    @Override
    public void addStatusChangedListener(Consumer<StatusChangedEvent> consumer) {
        statusChangedHandlers.add(consumer);
    }

    @Override
    public boolean removeMessageReceivedListener(Consumer<MessageReceivedEvent> consumer) {
        return this.messageReceivedListeners.remove(consumer);
    }

    @Override
    public boolean removeStatusChangedListener(Consumer<StatusChangedEvent> consumer) {
        return this.statusChangedHandlers.remove(consumer);
    }

    @Override
    public void connect(long roomId) throws NoNetworkConnectionException, NoRoomFoundException {
        this.roomInit = apiClient.getRoomInit(roomId).getData();
        int tryTimes = 0;
        while (tryTimes++ < TRY_TIMES) {
            if (connectWithTrueRoomId(roomInit.getRoomId())) {
                return;
            }
        }
        throw new NoNetworkConnectionException();
    }

    @Override
    public boolean isConnected() {
        return !closed;
    }

    @Override
    public void disconnect() throws IOException {
        disconnectWithoutEvent();
    }

    @Override
    public void close() throws IOException {
        disconnect();
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
     * @param header the socket header
     * @param body   the socket body
     */
    protected void handleData(BilibiliWebSocketHeader header, ByteBuffer body) {
        List<GenericBilibiliMessage> messages = bilibiliLiveMessageProtocolResolver.create(
                new BilibiliWebSocketFrame(header, body));
        callListeners(messages);
    }

    protected void callListeners(List<GenericBilibiliMessage> messages) {
        for (GenericBilibiliMessage message : messages) {
            if (message instanceof LiveBeginModel || message instanceof LiveEndModel) {
                StatusChangedEvent statusChangedEvent = createStatusChangedEvent(message);
                for (Consumer<StatusChangedEvent> consumer : this.statusChangedHandlers) {
                    consumer.accept(statusChangedEvent);
                }
                continue;
            }
            MessageReceivedEvent event = createMessageEvent(message);
            try {
                for (Consumer<MessageReceivedEvent> consumer : this.messageReceivedListeners) {
                    consumer.accept(event);
                }
            } catch (Exception e) {
                log.error("{} {}", e.getMessage(), Arrays.toString(e.getStackTrace()));
            }
        }
    }

    private StatusChangedEvent createStatusChangedEvent(GenericBilibiliMessage message) {
        StatusChangedEvent event = new StatusChangedEvent()
                .setMessage(message)
                .setRoomId(roomInit.getRoomId())
                .setUid(roomInit.getUid());
        if (message instanceof LiveEndModel) {
            event.setStatus(StatusChangedEvent.Status.PREPARING);
        } else if (message instanceof LiveBeginModel) {
            event.setStatus(StatusChangedEvent.Status.BEGIN);
        }
        return event;
    }

    protected MessageReceivedEvent createMessageEvent(GenericBilibiliMessage message) {
        return new MessageReceivedEvent().setMessage(message);
    }

    protected void readThread() {
        try {
            // All unused data is stored here
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                try {
                    lock.lock();
                    if (!this.isConnected()) {
                        return;
                    }
                    int readSize = socketChannel.read(buffer);
                    // socket is closed
                    if (readSize == -1) {
                        throw new IOException();
                    }
                    buffer.flip();
                    // Not enough to be a complete header
                    if (buffer.remaining() < BilibiliWebSocketHeader.HEADER_LENGTH) {
                        continue;
                    }

                    int totalLength = buffer.getInt(BilibiliWebSocketHeader.TOTAL_LENGTH_OFFSET);
                    // Not enough to be a complete package
                    if (buffer.remaining() < totalLength) {
                        // Buffer capacity is not enough, so we allocate a new one with doubled capacity
                        if (buffer.limit() == buffer.capacity()) {
                            buffer = ByteBuffer.allocate(buffer.capacity() * 2)
                                    .put(buffer);
                        }
                        continue;

                    }
                    BilibiliWebSocketHeader header = BilibiliWebSocketHeader.newInstance(buffer, true);
                    byte[] body = new byte[header.getTotalLength() - header.getHeaderLength()];
                    buffer.get(body);
                    // Although we don't know whether the type of the body,
                    // but we can make sure an intact header-body is correctly extracted.
                    handleData(header, ByteBuffer.wrap(body));
                    buffer.compact();
                    // The reset data.


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

    private boolean connectWithTrueRoomId(Long roomId) throws NoNetworkConnectionException {
        try {
            lock.lock();
            if (this.isConnected()) {
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

    private void tryReconnect() throws IOException {

        int tryTimes = 0;
        while (tryTimes++ < TRY_TIMES) {
            log.info("Try reconnect to room {} short id {}. Try times: {}",
                     roomInit.getRoomId(),
                     roomInit.getShortId(),
                     tryTimes);
            try {
                if (connectWithTrueRoomId(roomInit.getRoomId())) {
                    return;
                }
            } catch (NoNetworkConnectionException e) {
                try {
                    Thread.sleep(Duration.ofSeconds(10).toMillis());
                } catch (InterruptedException ignored) {

                }
            }
        }
        disconnect();
        for (var handler : statusChangedHandlers) {
            StatusChangedEvent event = new StatusChangedEvent()
                    .setMessage(null)
                    .setStatus(StatusChangedEvent.Status.DISCONNECT)
                    .setRoomId(roomInit.getRoomId())
                    .setUid(roomInit.getUid());
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
