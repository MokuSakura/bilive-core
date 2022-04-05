package org.mokusakura.bilive.core;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mokusakura.bilive.core.api.BilibiliLiveApiClient;
import org.mokusakura.bilive.core.api.HttpLiveApiClient;
import org.mokusakura.bilive.core.client.SubscribableClientImpl;
import org.mokusakura.bilive.core.event.SendGiftEvent;
import org.mokusakura.bilive.core.factory.BilibiliMessageFactoryDispatcher;
import org.mokusakura.bilive.core.factory.PopularityBilibiliMessageFactory;

import java.net.http.HttpClient;
import java.util.concurrent.Semaphore;

/**
 * @author MokuSakura
 */
@Log4j2
public class TestFactory {
    private final SubscribableClientImpl danmakuClient;
    private final BilibiliLiveApiClient bilibiliLiveApiClient;
    private final BilibiliMessageFactoryDispatcher bilibiliMessageFactory;
    private final HttpClient httpClient;
    private final Semaphore semaphore = new Semaphore(1);

    @SneakyThrows
    public TestFactory() {
        httpClient = HttpClient.newHttpClient();
        semaphore.acquire();
        bilibiliLiveApiClient = new HttpLiveApiClient(httpClient);
        bilibiliMessageFactory = BilibiliMessageFactoryDispatcher.createDefault();
        PopularityBilibiliMessageFactory popularityBilibiliMessageFactory = new PopularityBilibiliMessageFactory();
        bilibiliMessageFactory.register((short) 1, popularityBilibiliMessageFactory);
        danmakuClient = new SubscribableClientImpl();
        danmakuClient.subscribe(SendGiftEvent.class, e -> log.info(e.getMessage().getGiftName()));
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

    @SneakyThrows
    @Test
    void link() {
        danmakuClient.connect(213L);
        semaphore.acquire();
    }
}
