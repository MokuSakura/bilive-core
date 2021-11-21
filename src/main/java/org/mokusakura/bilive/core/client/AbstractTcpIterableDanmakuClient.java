package org.mokusakura.bilive.core.client;

import org.mokusakura.bilive.core.api.BilibiliLiveApiClient;
import org.mokusakura.bilive.core.event.MessageReceivedEvent;
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
    protected final ListenableDanmakuClient danmakuClient;
    protected final BilibiliLiveApiClient apiClient;
    protected final BilibiliMessageFactory messageFactory;
    protected final BlockingQueue<MessageReceivedEvent> blockingQueue;
    protected final int queueSize;
    protected Duration getWaitTime;


    public AbstractTcpIterableDanmakuClient(BilibiliLiveApiClient apiClient,
                                            BilibiliMessageFactory messageFactory,
                                            int queueSize,
                                            Duration getWaitTime) {
        assert queueSize != 0;
        this.apiClient = apiClient;
        this.messageFactory = messageFactory;
        this.getWaitTime = getWaitTime;
        this.danmakuClient = this.getListenableDanmakuClient();
        this.queueSize = queueSize;
        this.blockingQueue = this.getBlockingQueue();
    }

    /**
     * <p>
     * This method will be called only when the {@link #connect(long)} is called.
     * So make sure this method won't throw any exception while derived class is not constructed.
     * </p>
     *
     * @return message listener that will be used in {@link ListenableDanmakuClient}
     */
    protected abstract Consumer<MessageReceivedEvent> getMessageListener();

    /**
     * <p>
     * This method will be called only when the constructor of {@link AbstractTcpIterableDanmakuClient} is called.
     * So make sure this method won't throw any exception while derived class is not constructed.
     * </p>
     *
     * @return BlockingQueue that will be used to store messages.
     */
    protected abstract BlockingQueue<MessageReceivedEvent> getBlockingQueue();

    /**
     * <p>
     * This method will be called only when the constructor of {@link AbstractTcpIterableDanmakuClient} is called.
     * So make sure this method won't throw any exception while derived class is not constructed.
     * </p>
     *
     * @return ListenableDanmakuClient that will be used.
     */
    protected ListenableDanmakuClient getListenableDanmakuClient() {
        return new TcpListenableDanmakuClient(this.apiClient, this.messageFactory);
    }

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
        MessageReceivedEvent event = blockingQueue.poll(duration.toMillis(), TimeUnit.MILLISECONDS);
        return event == null ? null : event.getMessage();
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
