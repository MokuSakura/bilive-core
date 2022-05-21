package org.mokusakura.bilive.core.client;

import com.alibaba.fastjson.JSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mokusakura.bilive.core.api.BilibiliLiveApiClient;
import org.mokusakura.bilive.core.api.model.BilibiliApiResponse;
import org.mokusakura.bilive.core.api.model.DanmakuServerInfo;
import org.mokusakura.bilive.core.api.model.RoomInit;
import org.mokusakura.bilive.core.event.DisconnectedEvent;
import org.mokusakura.bilive.core.event.GenericEvent;
import org.mokusakura.bilive.core.event.MessageReceivedEvent;
import org.mokusakura.bilive.core.event.StatusChangedEvent;
import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;
import org.mokusakura.bilive.core.exception.NoRoomFoundException;
import org.mokusakura.bilive.core.listener.Listener;
import org.mokusakura.bilive.core.model.BilibiliWebSocketFrame;
import org.mokusakura.bilive.core.model.BilibiliWebSocketHeader;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;
import org.mokusakura.bilive.core.model.HelloMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * <p>
 * This class is used to connect to multiple danmaku servers.
 * <p>
 * Unlike {@link TcpDanmakuClient},
 * this class is able to connect to multiple danmaku servers with only a few threads.
 * It usually shows higher performance when connecting to multiple danmaku servers.
 * Since multiple threads will be created to handle one connection in {@link TcpDanmakuClient} and thus cost more CPU time,
 * this class will use only a few threads to handle multiple connections.
 * <p>
 * Though this class usually only use one thread, we still don't promise messages' order.
 * <p>
 * In this class, you can use {@link #connect(long)} to connect to a danmaku server,
 * or use {@link #disconnect(long)} to disconnect from a danmaku server.
 *
 * @author MokuSakura
 */
public class MultiConnectionDanmakuClient extends AbstractDanmakuClient implements SubscribableClient {
    private static final Logger log = LogManager.getLogger(MultiConnectionDanmakuClient.class);
    private final Map<Long, SocketChannel> socketChannelMap = new ConcurrentHashMap<>(16);
    private Selector selector;
    private final BilibiliLiveApiClient apiClient;

    @SuppressWarnings("rawtypes")
    private final Map<Class, List<Listener>> listenerMap = new ConcurrentHashMap<>();

    public MultiConnectionDanmakuClient(BilibiliLiveApiClient apiClient) {
        this.apiClient = apiClient;
        try {
            selector = SelectorProvider.provider().openSelector();
            new ReaderThread().start();
        } catch (IOException e) {
            log.error("{}, {}", e.getMessage(), Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public <E extends GenericEvent<?>> void subscribe(Class<E> eventClass, Listener<E> listener) {
        getListeners(eventClass).add(listener);
    }

    @Override
    public <E extends GenericEvent<?>> void unsubscribe(Class<E> eventClass, Listener<E> listener) {
        getListeners(eventClass).remove(listener);
    }

    @Override
    public <E extends GenericEvent<?>> void unsubscribeAll(Class<E> eventClass) {
        getListeners(eventClass).clear();
    }

    @SuppressWarnings("rawtypes")
    private synchronized List<Listener> getListeners(Class eventClass) {
        return listenerMap.computeIfAbsent(eventClass, k -> new LinkedList<>());
    }

    @Override
    public void unsubscribeAll() {
        listenerMap.clear();
    }

    @Override
    public void addMessageReceivedListener(Listener<MessageReceivedEvent<?>> consumer) {

    }

    @Override
    public void addStatusChangedListener(Listener<StatusChangedEvent<?>> consumer) {

    }

    @Override
    public Listener<MessageReceivedEvent<?>> removeMessageReceivedListener(Listener<MessageReceivedEvent<?>> consumer) {
        return null;
    }

    @Override
    public Listener<StatusChangedEvent<?>> removeStatusChangedListener(Listener<StatusChangedEvent<?>> consumer) {
        return null;
    }

    public boolean disconnect(long roomId) throws IOException {
        SocketChannel remove = socketChannelMap.remove(roomId);
        if (remove != null) {
            remove.close();
        }
        return true;
    }

    @Override
    boolean connectToTrueRoomId(long roomId) throws NoNetworkConnectionException {
        SocketChannel channel = null;
        try {
            channel = socketChannelMap.putIfAbsent(roomId, SocketChannel.open());
        } catch (IOException e) {
            log.error("{}, {}", e.getMessage(), Arrays.toString(e.getStackTrace()));
        }
        if (channel != null) {
            return false;
        }
        channel = socketChannelMap.get(roomId);

        try {

            BilibiliApiResponse<DanmakuServerInfo> resp = apiClient.getDanmakuServerInfo(roomId);
            DanmakuServerInfo data = resp.getData();

            //TODO Should use a selection rule here
            String host = data.getHostList()[0].getHost();
            int port = data.getHostList()[0].getPort();
            channel.connect(new InetSocketAddress(host, port));

            HelloMessage helloMessage = HelloMessage.newDefault(roomId, data.getToken());
            ByteBuffer payload = ByteBuffer.wrap(JSON.toJSONBytes(helloMessage));
            BilibiliWebSocketFrame frame = new BilibiliWebSocketFrame(
                    new BilibiliWebSocketHeader(payload.remaining() + BilibiliWebSocketHeader.HEADER_LENGTH,
                                                BilibiliWebSocketHeader.HEADER_LENGTH,
                                                BilibiliWebSocketHeader.ProtocolVersion.ClientSend,
                                                BilibiliWebSocketHeader.ActionType.Hello, 0), payload);
            DanmakuClient.sendMessage(channel, frame);
            DanmakuClient.sendHeartBeat(channel);
            channel.configureBlocking(false);
            log.info("Connect to {}:{} of Room {}, with token {}", host, port, roomId, data.getToken());
            channel.register(selector, SelectionKey.OP_READ, constructAttachmentsFor(roomId));
            startHeartBeat();
            return true;
        } catch (IOException e) {
            log.error("{}, {}", e.getMessage(), Arrays.toString(e.getStackTrace()));
            return false;
        }

    }

    @Override
    long getTrueRoomId(long roomId) throws NoNetworkConnectionException, NoRoomFoundException {
        BilibiliApiResponse<RoomInit> roomInit = apiClient.getRoomInit(roomId);
        return roomInit.getData().getRoomId();
    }

    @Override
    public Future<Boolean> sendMessageAsync(long roomId, BilibiliWebSocketFrame frame) {
        return null;
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public void close() throws IOException {

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void callListeners(long roomId, List<GenericEvent<?>> events) {
        for (GenericEvent event : events) {
            List<Listener> listeners = getListeners(event.getClass());
            for (Listener listener : listeners) {
                listener.onEvent(event);
            }
        }
    }

    protected Map<String, Object> constructAttachmentsFor(long roomId) {
        Map<String, Object> res = new TreeMap<>();
        res.put("roomId", roomId);
        res.put("byteBuffer", ByteBuffer.allocate(1024));
        return res;
    }

    protected void heartBeatTask() {
        List<Map.Entry<Long, SocketChannel>> set = new ArrayList<>(socketChannelMap.entrySet());
        for (Map.Entry<Long, SocketChannel> entry : set) {
            try {
                DanmakuClient.sendHeartBeat(entry.getValue());
            } catch (IOException e) {
                log.error("{} {}", e.getMessage(), Arrays.toString(e.getStackTrace()));
                try {
                    disconnect(entry.getKey());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                DisconnectedEvent event = new DisconnectedEvent();
                event.setException(e)
                        .setRoomId(entry.getKey());
                for (var listener : statusChangedHandlers) {
                    listener.onEvent(event);
                }
            } catch (Exception e) {
                log.error("{} {}", e.getMessage(), Arrays.toString(e.getStackTrace()));
            }
        }
    }

    private Map<String, Object> getAttachment(SelectionKey selectionKey) {
        try {
            // Shouldn't be problem here
            //noinspection unchecked
            return (Map<String, Object>) selectionKey.attachment();
        } catch (ClassCastException e) {
            throw new IllegalStateException(
                    "Unexpected type of attachment for SelectionKey Found. Expect Map, " +
                            selectionKey.attachment().getClass().getName() +
                            " found.");
        }
    }

    private class ReaderThread extends Thread {
        @Override
        public void run() {
            while (isOpen()) {
                try {
                    selector.select(this::onSelect, 1000L);
                } catch (IOException e) {
                    log.error("{} {}", e.getMessage(), Arrays.toString(e.getStackTrace()));
                }
            }
        }

        private void onSelect(SelectionKey selectionKey) {
            Map<String, Object> attachment = getAttachment(selectionKey);
            Long roomId = (Long) attachment.get("roomId");
            SocketChannel channel = (SocketChannel) selectionKey.channel();
            ByteBuffer byteBuffer = (ByteBuffer) attachment.get("byteBuffer");
            int readSize;
            try {
                readSize = channel.read(byteBuffer);

                if (readSize == -1) {
                    disconnect(roomId);
                }
                byteBuffer.flip();
                while (true) {
                    ByteBuffer tempBuffer = byteBuffer.asReadOnlyBuffer();
                    BilibiliWebSocketFrame resolve = BilibiliWebSocketFrame.resolve(tempBuffer);
                    byteBuffer.position(tempBuffer.position());
                    // not enough
                    if (resolve != null) {
                        if (resolve.getWebSocketBody().limit() == resolve.getWebSocketBody().position()) {
                            log.error("Error!");
                        }
                        List<GenericBilibiliMessage> genericBilibiliMessages = createMessages(resolve);
                        List<GenericEvent<?>> events = createEvents(roomId, genericBilibiliMessages);
                        callListeners(roomId, events);
                    } else {
                        break;
                    }
                }


                byteBuffer.compact();

            } catch (BufferUnderflowException e) {
                ByteBuffer tempBuffer = ByteBuffer.allocate(byteBuffer.capacity() << 1);
                tempBuffer.put(byteBuffer);
                byteBuffer = tempBuffer;
                attachment.put("byteBuffer", byteBuffer);
            } catch (Exception e) {
                log.error("Room {}: {} {}", roomId, e.getMessage(), Arrays.toString(e.getStackTrace()));
                try {
                    disconnect(roomId);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                selectionKey.cancel();
            }
        }
    }

}
