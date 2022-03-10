package org.mokusakura.bilive.core.client;

import lombok.extern.log4j.Log4j2;
import org.mokusakura.bilive.core.api.BilibiliLiveApiClient;
import org.mokusakura.bilive.core.api.model.DanmakuServerInfo;
import org.mokusakura.bilive.core.event.MessageReceivedEvent;
import org.mokusakura.bilive.core.event.StatusChangedEvent;
import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;
import org.mokusakura.bilive.core.exception.NoRoomFoundException;
import org.mokusakura.bilive.core.model.BilibiliWebSocketFrame;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;
import org.mokusakura.bilive.core.model.GenericStatusChangedModel;

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
 * @author MokuSakura
 */
@Log4j2
public class MultiConnectionDanmakuClient extends AbstractDanmakuClient {
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
        return 0;
    }

    @Override
    StatusChangedEvent createStatusChangedEvent(GenericStatusChangedModel model) {
        return null;
    }

    @Override
    MessageReceivedEvent createMessageEvent(GenericBilibiliMessage message) {
        return null;
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
