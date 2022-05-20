package org.mokusakura.bilive.core.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mokusakura.bilive.core.api.BilibiliLiveApiClient;
import org.mokusakura.bilive.core.api.model.BilibiliApiResponse;
import org.mokusakura.bilive.core.api.model.DanmakuServerInfo;
import org.mokusakura.bilive.core.api.model.RoomInit;
import org.mokusakura.bilive.core.event.GenericEvent;
import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;
import org.mokusakura.bilive.core.exception.NoRoomFoundException;
import org.mokusakura.bilive.core.model.BilibiliWebSocketFrame;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
public class MultiConnectionDanmakuClient extends AbstractDanmakuClient {
    private static final Logger log = LogManager.getLogger(MultiConnectionDanmakuClient.class);
    private Map<Long, SocketChannel> socketChannelMap = new ConcurrentHashMap<>(16);
    private Selector selector;
    private BilibiliLiveApiClient apiClient;

    public MultiConnectionDanmakuClient(BilibiliLiveApiClient apiClient) {
        this.apiClient = apiClient;
        try {
            selector = SelectorProvider.provider().openSelector();
        } catch (IOException e) {
            log.error("{}, {}", e.getMessage(), Arrays.toString(e.getStackTrace()));
        }
    }

    public boolean disconnect(long roomId) {
        return false;
    }

    @Override
    boolean connectToTrueRoomId(long roomId) throws NoNetworkConnectionException {
        SocketChannel channel = null;
        try {
            channel = socketChannelMap.putIfAbsent(roomId, SocketChannel.open());
        } catch (IOException e) {
            log.error("{}, {}", e.getMessage(), Arrays.toString(e.getStackTrace()));
        }
        if (channel == null) {
            return false;
        }
        try {
            SelectionKey register = channel.register(selector, SelectionKey.OP_READ);

            DanmakuServerInfo data = apiClient.getDanmakuServerInfo(roomId).getData();
            register.attach(constructAttachmentsFor(roomId));
            //TODO Should use a selection rule here
            channel.connect(new InetSocketAddress(data.getHostList()[0].getHost(), data.getHostList()[0].getPort()));

            channel.configureBlocking(false);
        } catch (IOException e) {
            log.error("{}, {}", e.getMessage(), Arrays.toString(e.getStackTrace()));
        }
        return true;
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
        return false;
    }

    @Override
    public void close() throws IOException {

    }

    protected Map<String, Object> constructAttachmentsFor(long roomId) {
        Map<String, Object> res = new TreeMap<>();
        res.put("roomId", roomId);
        res.put("byteBuffer", ByteBuffer.allocate(1024));
        return res;
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

    class ReaderThread extends Thread {
        @Override
        public void run() {
            while (isOpen()) {
                try {
                    selector.select(this::onSelect);
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
            try {
                int readSize = channel.read(byteBuffer);
                ByteBuffer tempBuffer = byteBuffer.asReadOnlyBuffer();
                BilibiliWebSocketFrame resolve = BilibiliWebSocketFrame.resolve(tempBuffer);
                // not enough
                if (resolve == null) {
                    return;
                }
                List<GenericBilibiliMessage> genericBilibiliMessages = createMessages(resolve);
                List<GenericEvent<?>> events = createEvents(roomId, genericBilibiliMessages);
                callListeners(roomId, events);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (BufferUnderflowException e) {
                ByteBuffer tempBuffer = ByteBuffer.allocate(byteBuffer.capacity() << 1);
                tempBuffer.put(byteBuffer);
                byteBuffer = tempBuffer;
                attachment.put("byteBuffer", byteBuffer);
            }
        }
    }
}
