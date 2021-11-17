package org.mokusakura.bilive.core.client;

import org.mokusakura.bilive.core.api.BilibiliLiveApiClient;
import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;
import org.mokusakura.bilive.core.exception.NoRoomFoundException;
import org.mokusakura.bilive.core.factory.BilibiliMessageFactory;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author MokuSakura
 */
public abstract class AbstractTcpIterableDanmakuClient implements IterableDanmakuClient {
    public static final Duration DEFAULT_GET_WAIT_TIME = Duration.ofSeconds(30);
    public static final int DEFAULT_QUEUE_SIZE = 100;
    protected final TcpListenableDanmakuClient danmakuClient;
    protected final BilibiliLiveApiClient apiClient;
    protected final BilibiliMessageFactory messageFactory;
    protected final BlockingQueue<GenericBilibiliMessage> blockingQueue;
    protected final int queueSize;
    protected Duration getWaitTime;


    public AbstractTcpIterableDanmakuClient(BilibiliLiveApiClient apiClient,
                                            BilibiliMessageFactory messageFactory,
                                            int queueSize,
                                            Duration getWaitTime) {
        this.apiClient = apiClient;
        this.messageFactory = messageFactory;
        this.getWaitTime = getWaitTime;
        this.danmakuClient = new TcpListenableDanmakuClient(this.apiClient, this.messageFactory);
        this.queueSize = queueSize;
        this.blockingQueue = this.getBlockingQueue();
    }

    protected abstract Consumer<GenericBilibiliMessage> getMessageListener();

    protected abstract BlockingQueue<GenericBilibiliMessage> getBlockingQueue();

    @Override
    public void connect(long roomId) throws NoNetworkConnectionException, NoRoomFoundException {
        this.danmakuClient.addMessageReceivedListener(this.getMessageListener());
        this.danmakuClient.connect(roomId);
    }

    @Override
    public boolean isConnected() {
        return this.danmakuClient.isConnected();
    }

    @Override
    public void disconnect() throws IOException {
        this.danmakuClient.disconnect();
        this.danmakuClient.removeMessageReceivedListener(this.getMessageListener());
    }

    @Override
    public void close() throws IOException {
        this.danmakuClient.close();
    }

    @Override
    public boolean isNextAvailable() {
        return !blockingQueue.isEmpty();
    }

    @Override
    public GenericBilibiliMessage next(Duration duration) throws InterruptedException {
        return blockingQueue.poll(duration.toMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public Duration getGetWaitTime() {
        return getWaitTime;
    }

    @Override
    public IterableDanmakuClient setWaitTime(Duration waitTime) {
        this.getWaitTime = waitTime;
        return this;
    }
}
