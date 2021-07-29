package org.mokusakura.danmakurecorder.core;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mokusakura.danmakurecorder.core.api.HttpDanmakuApiClient;
import org.mokusakura.danmakurecorder.core.client.TcpDanmakuClient;
import org.mokusakura.danmakurecorder.core.model.CommentModel;
import org.mokusakura.danmakurecorder.core.model.SendGiftModel;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author MokuSakura
 */
//@SpringBootTest
@Slf4j
public class TestSocketDanmakuClient {
    Double totalPrice = 0.0;

    public TestSocketDanmakuClient() {
    }

    @SneakyThrows
    @Test
    void testLink() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        var build = new TcpDanmakuClient(
                new HttpDanmakuApiClient(HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(20)).build()));
        build.addLiveBeginHandler(liveBeginEvent -> log.debug("Begin"));
        build.addLiveEndHandlers((a) -> semaphore.release());
        build.addReceivedHandlers(danmakuReceivedEvent -> {
            if (danmakuReceivedEvent.getAbstractDanmaku() instanceof CommentModel) {
//                log.debug(((CommentModel) danmakuReceivedEvent.getAbstractDanmaku()).getCommentText());
            }
            if (danmakuReceivedEvent.getAbstractDanmaku() instanceof SendGiftModel) {
                onGift((SendGiftModel) danmakuReceivedEvent.getAbstractDanmaku());
            }
        });
        ScheduledExecutorService timer = new ScheduledThreadPoolExecutor(1);
        timer.scheduleAtFixedRate(() -> log.info("{}", totalPrice), 30, 10, TimeUnit.SECONDS);
        build.connect(21652717);
        semaphore.acquire(1);
        timer.shutdownNow();
    }

    void onGift(SendGiftModel giftModel) {
        log.debug("{}:{}×{}", giftModel.getGiftType(), giftModel.getGiftName(), giftModel.getGiftNumber());
        if (Objects.equals(5, giftModel.getGiftType())) {
            return;
        }
        totalPrice += giftModel.getGiftPrice() * giftModel.getGiftNumber();
    }
}
