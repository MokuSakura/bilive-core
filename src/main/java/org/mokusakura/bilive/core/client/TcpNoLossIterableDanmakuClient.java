package org.mokusakura.bilive.core.client;

import org.mokusakura.bilive.core.api.BilibiliLiveApiClient;
import org.mokusakura.bilive.core.event.MessageReceivedEvent;
import org.mokusakura.bilive.core.factory.BilibiliMessageFactory;

import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

/**
 * @author MokuSakura
 */
public class TcpNoLossIterableDanmakuClient extends TcpIterableDanmakuClient implements NoLossIterableDanmakuClient {
    private final MessageListener messageListener;

    public TcpNoLossIterableDanmakuClient(BilibiliLiveApiClient apiClient,
                                          BilibiliMessageFactory messageFactory,
                                          Duration getWaitTime) {
        super(apiClient, messageFactory, 10000, getWaitTime, Duration.ofSeconds(0));
        this.messageListener = new MessageListener();
    }

    public TcpNoLossIterableDanmakuClient(BilibiliLiveApiClient apiClient,
                                          BilibiliMessageFactory messageFactory) {
        super(apiClient, messageFactory);
        this.messageListener = new MessageListener();
    }

    @Override
    protected Consumer<MessageReceivedEvent> getMessageListener() {
        return this.messageListener;
    }

    @Override
    protected BlockingQueue<MessageReceivedEvent> getBlockingQueue() {
        return new LinkedBlockingQueue<>();
    }

    protected class NoLossMessageListener implements Consumer<MessageReceivedEvent> {
        @Override
        public void accept(MessageReceivedEvent genericBilibiliMessage) {
            try {
                blockingQueue.put(genericBilibiliMessage);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
