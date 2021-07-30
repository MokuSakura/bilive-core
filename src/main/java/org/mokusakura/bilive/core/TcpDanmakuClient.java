package org.mokusakura.bilive.core;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.mokusakura.bilive.core.api.BilibiliApiClient;
import org.mokusakura.bilive.core.event.DanmakuReceivedEvent;
import org.mokusakura.bilive.core.event.LiveBeginEvent;
import org.mokusakura.bilive.core.event.LiveEndEvent;
import org.mokusakura.bilive.core.event.OtherEvent;
import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;
import org.mokusakura.bilive.core.exception.NoRoomFoundException;
import org.mokusakura.bilive.core.model.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.zip.Inflater;

/**
 * @author MokuSakura
 */
@Slf4j
public class TcpDanmakuClient implements DanmakuClient {

    private final ScheduledExecutorService timer;
    private final BilibiliApiClient apiClient;
    private final Set<Consumer<DanmakuReceivedEvent>> danmakuReceivedHandlers;
    private final Set<Consumer<LiveEndEvent>> liveEndHandlers;
    private final Set<Consumer<LiveBeginEvent>> liveBeginHandlers;
    private final Set<Consumer<OtherEvent>> otherHandlers;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final Map<ProtocolVersion, BiConsumer<WebSocketHeader, ByteBuffer>> messageHandlers;
    private final Lock lock;
    private DanmakuServerInfo.HostListItem hostListItem;
    private boolean closed;
    private Socket socket;
    private RoomInit roomInit;
    private DanmakuServerInfo danmakuServerInfo;
    private InputStream inputStream;
    private OutputStream outputStream;

    public TcpDanmakuClient(BilibiliApiClient apiClient) {
        this.apiClient = apiClient;
        timer = new ScheduledThreadPoolExecutor(10);
        danmakuReceivedHandlers = new LinkedHashSet<>();
        liveEndHandlers = new LinkedHashSet<>();
        liveBeginHandlers = new LinkedHashSet<>();
        otherHandlers = new LinkedHashSet<>();
        threadPoolExecutor = new ThreadPoolExecutor(50, 50, 1000, TimeUnit.SECONDS, new ArrayBlockingQueue<>(50));
        messageHandlers = new LinkedHashMap<>();
        lock = new ReentrantLock();
        closed = true;
        messageHandlers.put(ProtocolVersion.PureJson, this::handleNormalData);
        messageHandlers.put(ProtocolVersion.CompressedBuffer, this::handleCompressedData);
        messageHandlers.put(ProtocolVersion.CompressedBuffer2, this::handleCompressedData);
    }

    @Override
    public Collection<Consumer<DanmakuReceivedEvent>> danmakuReceivedHandlers() {
        return danmakuReceivedHandlers;
    }

    @Override
    public Collection<Consumer<LiveEndEvent>> liveEndHandlers() {
        return liveEndHandlers;
    }

    @Override
    public Collection<Consumer<LiveBeginEvent>> liveBeginHandlers() {
        return liveBeginHandlers;
    }

    @Override
    public Collection<Consumer<OtherEvent>> otherHandlers() {
        return otherHandlers;
    }

    @Override
    public void addLiveBeginHandler(Consumer<LiveBeginEvent> consumer) {
        liveBeginHandlers.add(consumer);
    }

    @Override
    public void addReceivedHandlers(Consumer<DanmakuReceivedEvent> consumer) {
        danmakuReceivedHandlers.add(consumer);
    }

    @Override
    public void addLiveEndHandlers(Consumer<LiveEndEvent> consumer) {
        liveEndHandlers.add(consumer);
    }

    @Override
    public void addOtherHandlers(Consumer<OtherEvent> consumer) {
        otherHandlers.add(consumer);
    }

    @Override
    public void connect(int roomId) throws NoNetworkConnectionException, NoRoomFoundException {
        try {
            lock.lock();
            if (!closed) {
                return;
            }
            this.roomInit = apiClient.getRoomInit(roomId).getData();
            danmakuServerInfo = apiClient.getDanmakuServerInfo(roomInit.getRoomId()).getData();
            var hostListItems = Arrays.stream(danmakuServerInfo.getHostList())
                    .filter(hostListItem -> hostListItem.getHost() != null && hostListItem.getPort() != null)
                    .toArray(DanmakuServerInfo.HostListItem[]::new);
            if (hostListItems.length == 0) {
                return;
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
                sendHeartBeanAsync().get();
                // Start read Thread
                new Thread(this::readThread).start();
                timer.scheduleAtFixedRate(this::sendHeartBeanAsync, 10, 30, TimeUnit.SECONDS);
                log.info("Connect to {}:{} of Room {}, short id {}, with token {}", host, port, roomInit.getRoomId(),
                         roomInit.getShortId(), danmakuServerInfo.getToken());
                closed = false;
            } catch (IOException | InterruptedException | ExecutionException e) {
                log.error(e.getMessage());
            }
        } finally {
            lock.unlock();
        }

    }

    @Override
    public boolean isConnected() {
        return inputStream != null;
    }

    @Override
    public void disconnect() throws IOException {
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
            timer.shutdownNow();
            lock.unlock();
        }
    }

    @Override
    public void close() throws IOException {
        disconnect();
    }

    protected void handleNormalData(WebSocketHeader header, ByteBuffer byteBuffer) {
        if (header.getActionType() != ActionType.GlobalInfo) {
            return;
        }
        var json = new String(byteBuffer.array(), StandardCharsets.UTF_8);
        try {
            GenericBilibiliMessage message = GenericBilibiliMessageFactory.getInstance().create(json);
            if (message == null) {
                return;
            }
            if (message instanceof AbstractDanmaku) {
                danmakuReceivedHandlers.forEach((action) -> action.accept(
                        new DanmakuReceivedEvent(json, this.roomInit.getRoomId(), (AbstractDanmaku) message)));

            } else if (message instanceof LiveBeginModel) {
                liveBeginHandlers.forEach(
                        (action) -> action.accept(new LiveBeginEvent((LiveBeginModel) message)));

            } else if (message instanceof LiveEndModel) {
                liveEndHandlers.forEach(
                        (action) -> action.accept(new LiveEndEvent((LiveEndModel) message)));

            } else {
                otherHandlers.forEach(
                        (action) -> action.accept(new OtherEvent(message, roomInit.getRoomId())));

            }
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    protected void handleCompressedData(WebSocketHeader header, ByteBuffer byteBuffer) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Inflater inflater = new Inflater();
            byte[] buffer = new byte[1024];
            inflater.setInput(byteBuffer.array(), 0, byteBuffer.array().length);
            int inflateLength;

            while ((inflateLength = inflater.inflate(buffer, 0, buffer.length)) != 0) {
                baos.write(buffer, 0, inflateLength);
            }
            inflater.end();
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()));
            log.error(e.getMessage());
            return;
        }
        var decompressedData = baos.toByteArray();
        handleData(decompressedData);
//        handlePureJson(header, Arrays.copyOfRange(decompressedData, 0, decompressedData.length));
    }

    protected Future<Boolean> sendHelloAsync() {
        JSONObject body = new JSONObject();
        body.put("uid", 0);
        body.put("roomid", roomInit.getRoomId());
        body.put("protover", 0);
        body.put("platform", "web");
        body.put("clientver", "2.6.25");
        body.put("type", 2);
        body.put("key", danmakuServerInfo.getToken());
        return sendMessageAsync(ActionType.Hello, body.toJSONString());
    }

    protected Future<Boolean> sendHeartBeanAsync() {
        return sendMessageAsync(ActionType.HeartBeat, "");
    }

    protected Future<Boolean> sendMessageAsync(ActionType actionType, String body) {
        return threadPoolExecutor.submit(() -> {
            log.debug("send {}, {}", actionType, body);
            String cbody = body == null ? "" : body;
            var payload = cbody.getBytes(StandardCharsets.UTF_8);
            var size = payload.length + WebSocketHeader.HEADER_LENGTH;
            WebSocketHeader header = WebSocketHeader.newInstance(size, ProtocolVersion.ClientSend, actionType);
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
     * then {@link TcpDanmakuClient#handleCompressedData(WebSocketHeader, ByteBuffer)} will be executed.
     * After decompress data, this method must be called with decompressed body data.
     * If ProtocolVersion is {@link ProtocolVersion#PureJson},
     * then {@link TcpDanmakuClient#handleNormalData(WebSocketHeader, ByteBuffer)} will be executed.
     * </p>
     *
     * @param data- Data to handle
     */
    protected void handleData(byte[] data) {
        // Basic offset. If more than one data package is contained in data,
        // offset will the begin index of the next data package
        int offset = 0;
        while (true) {
            // first 16 bytes. Header data
            byte[] headSlice = Arrays.copyOfRange(data, offset, offset + WebSocketHeader.HEADER_LENGTH);

            var decodedHeader = WebSocketHeader.newInstance(headSlice, true);
            // Begin from the 16th byte, end at the index of the 4 bytes unsigned int value of the header.
            byte[] bodySlice = Arrays.copyOfRange(data, offset + WebSocketHeader.BODY_OFFSET,
                                                  offset + (int) decodedHeader.getTotalLength());
            var handler = messageHandlers.get(decodedHeader.getProtocolVersion());
            Objects.requireNonNullElse(handler, (a, b) -> {})
                    .accept(decodedHeader, ByteBuffer.wrap(bodySlice));
            if (offset + bodySlice.length + headSlice.length == data.length) {
                return;
            }
            offset += bodySlice.length + headSlice.length;
        }

    }

    protected void readThread() {

        try {
            // All unused data is stored here
            byte[] data = new byte[0];
            while (socket != null) {
                try {
                    var readBuffer = new byte[1024 * 8];
                    var readSize = inputStream.read(readBuffer);
                    if (readSize == 0) {
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

                    WebSocketHeader header = WebSocketHeader.newInstance(data, true);
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
                    throw e;
                } catch (Exception e) {
                    log.error(e.getMessage());
                    log.error(Arrays.toString(e.getStackTrace()));
                }
            }
        } catch (IOException e) {
            try {
                disconnect();
            } catch (IOException ignored) {
            }
        }
    }
}