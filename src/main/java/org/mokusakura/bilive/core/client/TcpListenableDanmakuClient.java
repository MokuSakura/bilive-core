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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
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
public class TcpListenableDanmakuClient implements ListenableDanmakuClient {
    public static final int TRY_TIMES = 3;
    private final ScheduledExecutorService timer;
    private final BilibiliLiveApiClient apiClient;
    private final Collection<Consumer<MessageReceivedEvent>> messageReceivedListeners;
    private final Collection<Consumer<StatusChangedEvent>> statusChangedHandlers;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final BilibiliMessageFactory bilibiliMessageFactory;
    //    private final Map<Short, BiConsumer<BilibiliWebSocketHeader, ByteBuffer>> messageConverters;
    private final Lock lock;
    private ScheduledFuture<?> heartBeatTask;
    private DanmakuServerInfo.HostListItem hostListItem;
    private boolean closed;
    private Socket socket;
    private RoomInit roomInit;
    private DanmakuServerInfo danmakuServerInfo;
    private InputStream inputStream;
    private OutputStream outputStream;


    public TcpListenableDanmakuClient(BilibiliLiveApiClient apiClient, BilibiliMessageFactory bilibiliMessageFactory) {
        this.apiClient = apiClient;
        timer = new ScheduledThreadPoolExecutor(10);
        messageReceivedListeners = new LinkedHashSet<>();
        statusChangedHandlers = new LinkedHashSet<>();
        threadPoolExecutor = new ThreadPoolExecutor(50, 50, 1000, TimeUnit.SECONDS, new ArrayBlockingQueue<>(50));
        lock = new ReentrantLock();
        closed = true;

        this.bilibiliMessageFactory = bilibiliMessageFactory;
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
        return inputStream != null;
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
            outputStream.write(header.array());
            outputStream.write(payload);
            outputStream.flush();
            return true;
        });

    }

    /**
     * <p>
     * This is a recursion method.
     * This method will solve all the data. However, to handle successfully, data must be an intact package.
     * That means first 16 bytes can be decoded as a header, and the total length of data is equal to
     * the unsigned int value of the first 4 bytes of the data.
     * If ProtocolVersion is {@link ProtocolVersion#CompressedBuffer},
     * After decompress data, this method must be called with decompressed body data.
     * </p>
     *
     * @param data- Data to handle
     */
    protected void handleData(byte[] data) {
        BilibiliWebSocketHeader header = BilibiliWebSocketHeader.newInstance(
                Arrays.copyOfRange(data, 0, BilibiliWebSocketHeader.HEADER_LENGTH));
        byte[] body = Arrays.copyOfRange(data, BilibiliWebSocketHeader.BODY_OFFSET, data.length);
        List<GenericBilibiliMessage> messages = bilibiliMessageFactory.create(new BilibiliWebSocketFrame(header, body));
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
            byte[] data = new byte[0];
            while (socket != null) {
                try {
                    var readBuffer = new byte[1024 * 8];
                    var readSize = inputStream.read(readBuffer);
                    if (readSize == 0 || readSize == -1) {
                        throw new IOException();
                    }
                    // ***************Concat the data with read data**************
                    byte[] temp = new byte[readSize + data.length];
                    System.arraycopy(data, 0, temp, 0, data.length);
                    if (temp.length - data.length >= 0)
                        System.arraycopy(readBuffer, 0, temp, data.length, temp.length - data.length);
                    data = temp;
                    // ****************************Finish concat*******************************

                    // Data not enough to be a header.
                    // Continue reading.
                    if (data.length < 16) {
                        continue;
                    }

                    BilibiliWebSocketHeader header = BilibiliWebSocketHeader.newInstance(data, true);
                    // Body is not intact. Continue reading until the body is intact.
                    if (data.length < header.getTotalLength()) {
                        continue;
                    }
                    byte[] dataToProcess = Arrays.copyOfRange(data, 0, (int) header.getTotalLength());
                    data = Arrays.copyOfRange(data, (int) header.getTotalLength(), data.length);
                    // Although we don't whether the type of the body, but we can make sure an intact header-body is correctly extracted.
                    handleData(dataToProcess);
                    // The reset data.


                } catch (IOException e) {
                    log.error(e.getMessage());
                    log.error("{}", Arrays.toString(e.getStackTrace()));
                    throw e;
                }
            }
        } catch (IOException e) {
            threadPoolExecutor.execute(() -> {
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
            if (!closed) {
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
                socket = new Socket(host, port);
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                sendHelloAsync().get();
                sendHeartBeatAsync().get();
                // Start read Thread
                new Thread(this::readThread).start();
                heartBeatTask = timer.scheduleAtFixedRate(() -> {
                    try {
                        this.sendHeartBeatAsync().get(10, TimeUnit.SECONDS);
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        e.printStackTrace();
                    }
                }, 10, 30, TimeUnit.SECONDS);
                closed = false;
                log.info("Connect to {}:{} of Room {}, short id {}, with token {}", host, port, roomInit.getRoomId(),
                         roomInit.getShortId(), danmakuServerInfo.getToken());
                return true;
            } catch (IOException | InterruptedException | ExecutionException e) {
                log.error(e.getMessage());
            }
        } catch (NoNetworkConnectionException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return false;
    }

    private void tryReconnect() throws IOException {

        int tryTimes = 0;
        while (tryTimes++ < TRY_TIMES) {
            log.info("Try reconnect for the {} time to room {} short id {}",
                     tryTimes == 1 ? "1st" : tryTimes == 2 ? "2nd" : tryTimes == 3 ? "3rd" : tryTimes + "th",
                     roomInit.getRoomId(),
                     roomInit.getShortId());
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
            if (socket != null) {
                socket.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }

        } catch (IOException e) {
            log.error("Error closing DanmakuClient");
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            throw e;
        } finally {
            socket = null;
            inputStream = null;
            outputStream = null;
            heartBeatTask.cancel(true);
            lock.unlock();
        }
    }
}
