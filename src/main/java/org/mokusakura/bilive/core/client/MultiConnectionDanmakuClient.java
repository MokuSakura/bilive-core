package org.mokusakura.bilive.core.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mokusakura.bilive.core.api.BilibiliLiveApiClient;
import org.mokusakura.bilive.core.api.model.BilibiliApiResponse;
import org.mokusakura.bilive.core.api.model.DanmakuServerInfo;
import org.mokusakura.bilive.core.api.model.RoomInit;
import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;
import org.mokusakura.bilive.core.exception.NoRoomFoundException;
import org.mokusakura.bilive.core.model.BilibiliWebSocketFrame;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Arrays;
import java.util.Map;
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
}
