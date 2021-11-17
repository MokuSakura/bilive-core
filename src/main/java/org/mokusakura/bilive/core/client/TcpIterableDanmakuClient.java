package org.mokusakura.bilive.core.client;

import org.mokusakura.bilive.core.api.BilibiliLiveApiClient;
import org.mokusakura.bilive.core.factory.BilibiliMessageFactory;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;

import java.time.Duration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author MokuSakura
 */
public class TcpIterableDanmakuClient extends AbstractTcpIterableDanmakuClient {
    public static final Duration DEFAULT_SET_WAIT_TIME = Duration.ofSeconds(10);
    private final MessageListener messageListener;
    private final Duration setWaitTime;

    public TcpIterableDanmakuClient(BilibiliLiveApiClient apiClient,
                                    BilibiliMessageFactory messageFactory,
                                    int queueSize,
                                    Duration getWaitTime,
                                    Duration setWaitTime) {
        super(apiClient, messageFactory, queueSize, getWaitTime);
        this.setWaitTime = setWaitTime;
        messageListener = new MessageListener();
    }

    public TcpIterableDanmakuClient(BilibiliLiveApiClient apiClient,
                                    BilibiliMessageFactory messageFactory) {
        this(apiClient,
             messageFactory,
             DEFAULT_QUEUE_SIZE,
             DEFAULT_GET_WAIT_TIME,
             DEFAULT_SET_WAIT_TIME);
    }

    @Override
    protected BlockingQueue<GenericBilibiliMessage> getBlockingQueue() {
        return new ArrayBlockingQueue<>(super.queueSize);
    }

    @Override
    protected Consumer<GenericBilibiliMessage> getMessageListener() {
        return this.messageListener;
    }

    protected class MessageListener implements Consumer<GenericBilibiliMessage> {
        @Override
        public void accept(GenericBilibiliMessage genericBilibiliMessage) {
            try {
                blockingQueue.offer(genericBilibiliMessage, setWaitTime.toMillis(), TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
