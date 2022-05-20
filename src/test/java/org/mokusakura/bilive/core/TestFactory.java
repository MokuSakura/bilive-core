package org.mokusakura.bilive.core;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.mokusakura.bilive.core.api.BilibiliLiveApiClient;
import org.mokusakura.bilive.core.api.HttpLiveApiClient;
import org.mokusakura.bilive.core.client.SubscribableClientImpl;
import org.mokusakura.bilive.core.event.CommentEvent;
import org.mokusakura.bilive.core.event.SendGiftEvent;
import org.mokusakura.bilive.core.factory.BilibiliMessageFactoryDispatcher;
import org.mokusakura.bilive.core.factory.PopularityBilibiliMessageFactory;
import org.mokusakura.bilive.core.listener.AsyncListener;
import org.mokusakura.bilive.core.listener.ParallelListener;

import java.net.http.HttpClient;
import java.util.concurrent.Semaphore;

/**
 * @author MokuSakura
 */
@SuppressWarnings("all")
public class TestFactory {
    private static final Logger log = LogManager.getLogger(TestFactory.class);
    private final SubscribableClientImpl danmakuClient;
    private final BilibiliLiveApiClient bilibiliLiveApiClient;
    private final BilibiliMessageFactoryDispatcher bilibiliMessageFactory;
    private final HttpClient httpClient;
    private final Semaphore semaphore = new Semaphore(1);


    public TestFactory() throws Exception {

        httpClient = HttpClient.newHttpClient();
        semaphore.acquire();
        bilibiliLiveApiClient = new HttpLiveApiClient(httpClient);
        bilibiliMessageFactory = BilibiliMessageFactoryDispatcher.createDefault();
        PopularityBilibiliMessageFactory popularityBilibiliMessageFactory = new PopularityBilibiliMessageFactory();
        bilibiliMessageFactory.register((short) 1, popularityBilibiliMessageFactory);
        danmakuClient = new SubscribableClientImpl();
//        danmakuClient.setLingerMillSeconds(1000000);
//        danmakuClient.subscribe(CommentEvent.class, e -> log.info(e.getMessage().getCommentText()));
        danmakuClient.subscribe(CommentEvent.class,
                                (ParallelListener<CommentEvent>) e -> log.info(e.getMessage().getCommentText()));
        danmakuClient.subscribe(SendGiftEvent.class,
                                (AsyncListener<SendGiftEvent>) e -> log.warn(e.getMessage().getPrice()));

//        danmakuClient.addMessageReceivedListener(event -> {
//            log.info(event.getMessage().getRawMessage());
//        });
//        danmakuClient.addStatusChangedListener(e -> {
//            if (e.getEventName().equals(StatusChangedMessage.Status.DISCONNECT) ||
//                    e.getEventName().equals(StatusChangedMessage.Status.PREPARING)) {
//                semaphore.release();
//            }
//        });
    }

    @Test
    void link() throws Exception {
        danmakuClient.connect(9170973);
        semaphore.acquire();
    }
}
