package org.mokusakura.bilive.core.client;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mokusakura.bilive.core.api.BilibiliLiveApiClient;
import org.mokusakura.bilive.core.api.HttpLiveApiClient;
import org.mokusakura.bilive.core.api.model.DanmakuServerInfo;
import org.mokusakura.bilive.core.api.model.RoomInit;
import org.mokusakura.bilive.core.event.DisconnectedEvent;
import org.mokusakura.bilive.core.event.GenericEvent;
import org.mokusakura.bilive.core.event.MessageReceivedEvent;
import org.mokusakura.bilive.core.event.StatusChangedEvent;
import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;
import org.mokusakura.bilive.core.exception.NoRoomFoundException;
import org.mokusakura.bilive.core.factory.BilibiliMessageFactory;
import org.mokusakura.bilive.core.factory.BilibiliMessageFactoryDispatcher;
import org.mokusakura.bilive.core.factory.EventFactoryDispatcher;
import org.mokusakura.bilive.core.listener.Listener;
import org.mokusakura.bilive.core.model.BilibiliWebSocketFrame;
import org.mokusakura.bilive.core.model.BilibiliWebSocketHeader;
import org.mokusakura.bilive.core.model.BilibiliWebSocketHeader.ActionType;
import org.mokusakura.bilive.core.model.BilibiliWebSocketHeader.DataFormat;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;
import org.mokusakura.bilive.core.model.HelloMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * This class provides a tcp connection to bilibili live danmaku server.
 * <p>
 * This class is thread-safe, but if any member field like {@link BilibiliLiveApiClient}
 * or {@link BilibiliMessageFactory} is not thread-safe, it will cause unexpected behavior.
 * <p>
 * This class will use a new thread which is not a daemon thread to connect to the server.
 * And {@link #sendMessageAsync(long, BilibiliWebSocketFrame)}  will use a thread in ThreadPoolExecutor to send message.
 * Consider using{@link MultiplexingDanmakuClient} if you want to connect to multiple danmaku servers.
 * <p>
 * In this implementation, {@link #sendMessageAsync(long, BilibiliWebSocketFrame)}
 * will ignore room id if the instance is connected to a room.
 * If not, message will not be sent.
 * <p>
 * This class ensures all message will be received by listeners.
 * <p>
 * This class don't promise the the message's order.
 *
 * @author MokuSakura
 */
public class TcpDanmakuClient extends AbstractDanmakuClient {
    public static final int TRY_TIMES = 3;
    private static final ExecutorService SHARED = Executors.newCachedThreadPool();
    private static final Logger log = LogManager.getLogger(TcpDanmakuClient.class);
    private final ExecutorService executorService;
    private final Lock lock;
    private DanmakuServerInfo.HostListItem hostListItem;
    private boolean closed = true;
    private SocketChannel socketChannel;
    private RoomInit roomInit;
    private DanmakuServerInfo danmakuServerInfo;
    private final BilibiliLiveApiClient apiClient;

    public TcpDanmakuClient(
            Collection<Listener<MessageReceivedEvent<?>>> messageReceivedListeners,
            Collection<Listener<StatusChangedEvent<?>>> statusChangedHandlers,
            EventFactoryDispatcher eventFactory,
            BilibiliMessageFactory bilibiliMessageFactory,
            ExecutorService executorService,
            BilibiliLiveApiClient apiClient) {
        super(messageReceivedListeners,
              statusChangedHandlers,
              eventFactory,
              bilibiliMessageFactory);
        this.executorService = executorService;
        this.apiClient = apiClient;
        this.lock = new ReentrantLock();
    }

    public TcpDanmakuClient(EventFactoryDispatcher eventFactory,
                            BilibiliMessageFactory bilibiliMessageFactory,
                            ExecutorService executorService,
                            BilibiliLiveApiClient apiClient) {
        this(new ArrayList<>(),
             new ArrayList<>(),
             eventFactory,
             bilibiliMessageFactory,
             executorService,
             apiClient);
    }


    public TcpDanmakuClient() {
        this(EventFactoryDispatcher.getShared(),
             BilibiliMessageFactoryDispatcher.getShared());
    }

    public TcpDanmakuClient(EventFactoryDispatcher eventFactory,
                            BilibiliMessageFactory bilibiliMessageFactory) {
        this(eventFactory,
             bilibiliMessageFactory,
             SHARED,
             HttpLiveApiClient.getShared());
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
        return executorService.submit(() -> {
            DanmakuClient.sendMessage(socketChannel, frame);
            return true;
        });
    }

    @Override
    protected long getTrueRoomId(long roomId) throws NoNetworkConnectionException, NoRoomFoundException {
        this.roomInit = apiClient.getRoomInit(roomId).getData();
        return roomInit.getRoomId();
    }

    @Override
    protected void heartBeatTask() {
        try {
            DanmakuClient.sendHeartBeat(socketChannel);
        } catch (IOException e) {
            log.error("{} {}", e.getMessage(), Arrays.toString(e.getStackTrace()));
            try {
                close();
            } catch (IOException ex) {
                log.error("{} {}", ex.getMessage(), Arrays.toString(ex.getStackTrace()));
            }
            DisconnectedEvent event = new DisconnectedEvent();
            event.setException(e)
                    .setRoomId(roomInit.getRoomId());
            for (var listener : statusChangedHandlers) {
                listener.onEvent(event);
            }
        }
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
                // Connection is set up here
                socketChannel = SocketChannel.open();
                socketChannel.connect(new InetSocketAddress(host, port));
                sendHelloAsync().get();
                sendHeartBeatAsync().get();
                // Start read Thread
                new Thread(this::readThread).start();
                startHeartBeat();
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
        HelloMessage helloMessage = HelloMessage.newDefault(roomInit.getRoomId(), danmakuServerInfo.getToken());
        return sendMessageAsync(ActionType.Hello, JSONObject.toJSONString(helloMessage));
    }

    protected Future<Boolean> sendHeartBeatAsync() {
        return sendMessageAsync(ActionType.HeartBeat, "");
    }

    protected Future<Boolean> sendMessageAsync(int actionType, String body) {
        return executorService.submit(() -> {
//            log.debug("send {}, {}", actionType, body);
            String cbody = body == null ? "" : body;
            var payload = cbody.getBytes(StandardCharsets.UTF_8);
            var size = payload.length + BilibiliWebSocketHeader.HEADER_LENGTH;
            BilibiliWebSocketHeader header = BilibiliWebSocketHeader.newInstance(size, DataFormat.ClientSend,
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
        if (messages.isEmpty()) {
            return;
        }
        List<GenericEvent<?>> events = createEvents(roomInit.getRoomId(), messages);
        if (events.isEmpty()) {
            return;
        }
        callListeners(roomInit.getRoomId(), events);
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
            executorService.submit(() -> {
                try {
                    disconnectWithoutEvent();
                    tryReconnect();
                } catch (Exception ignored) {
                }
            });
        }
    }


    private void tryReconnect() throws IOException {
        long firstTryTime = System.currentTimeMillis();
        long lastTryTime = firstTryTime;
        int tryTimes = 0;
        Exception exception = null;
        while (tryTimes++ < TRY_TIMES) {
            log.info("Try reconnect to room {} short id {}. Try times: {}",
                     roomInit.getRoomId(),
                     roomInit.getShortId(),
                     tryTimes);
            try {
                lastTryTime = System.currentTimeMillis();
                if (connectToTrueRoomId(roomInit.getRoomId())) {
                    return;
                }
            } catch (NoNetworkConnectionException e) {
                exception = e;
                try {
                    Thread.sleep(Duration.ofSeconds(10).toMillis());
                } catch (InterruptedException ignored) {

                }
            }
        }
        close();
        DisconnectedEvent disconnectedEvent = new DisconnectedEvent();
        disconnectedEvent.setFirstTryTime(firstTryTime)
                .setLastTryTime(lastTryTime)
                .setTryCount(tryTimes)
                .setException(exception)
                .setRoomId(roomInit.getRoomId());
        for (var handler : statusChangedHandlers) {
            handler.onEvent(disconnectedEvent);
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
            stopHeartBeat();
            lock.unlock();
        }
    }
}
