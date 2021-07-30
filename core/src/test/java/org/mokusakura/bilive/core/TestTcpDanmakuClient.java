package org.mokusakura.bilive.core;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mokusakura.bilive.core.api.HttpDanmakuApiClient;
import org.mokusakura.bilive.core.model.*;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
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
public class TestTcpDanmakuClient {
    Double totalPrice = 0.0;
    Map<Integer, Integer> interactTimes = new HashMap<>();
    Map<String, Double> userSentPrice = new HashMap<>();

    public TestTcpDanmakuClient() {
    }

    @SneakyThrows
    @Test
    void testLink() {

        Semaphore semaphore = new Semaphore(0);
        var build = new TcpDanmakuClient(
                new HttpDanmakuApiClient(HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(20)).build()));
        build.addLiveBeginHandler(liveBeginEvent -> log.debug("Begin"));
        build.addReceivedHandlers((event) -> onDanmaku(event.getAbstractDanmaku()));
        ScheduledExecutorService timer = new ScheduledThreadPoolExecutor(1);
        build.connect(22634198);
        timer.scheduleAtFixedRate(this::printValue, 10, 60, TimeUnit.SECONDS);
        semaphore.acquire(1);

        timer.shutdownNow();
    }

    void onDanmaku(AbstractDanmaku danmaku) {
        interactTimes.compute(danmaku.getUid(), (uid, num) -> num == null ? 1 : num + 1);
        if (danmaku instanceof CommentModel) {

        } else if (danmaku instanceof SendGiftModel) {
            SendGiftModel giftModel = (SendGiftModel) danmaku;
            if (Objects.equals(5, giftModel.getGiftType())) {
                return;
            }
            log.debug("{} send {}×{}", giftModel.getUsername(), giftModel.getGiftName(), giftModel.getGiftNumber());
            userSentPrice.compute(giftModel.getUsername(), (uid, price) ->
                    price == null ?
                            giftModel.getGiftPrice() * giftModel.getGiftNumber() :
                            price + giftModel.getGiftPrice() * giftModel.getGiftNumber());
            totalPrice += giftModel.getGiftPrice() * giftModel.getGiftNumber();
        } else if (danmaku instanceof GuardBuyModel) {
            GuardBuyModel guardBuyModel = (GuardBuyModel) danmaku;
            log.debug("{} buy {}", guardBuyModel.getUid(), guardBuyModel.getGuardName());
            userSentPrice.compute(guardBuyModel.getUsername(), (uid, price) ->
                    price == null ? guardBuyModel.getGiftPrice() : price + guardBuyModel.getGiftPrice());
            totalPrice += guardBuyModel.getGiftPrice();
        } else if (danmaku instanceof SCModel) {
            SCModel scModel = (SCModel) danmaku;
            log.debug("{} buy {}￥SC {}", scModel.getUsername(), scModel.getPrice(), scModel.getMessage());
            totalPrice += scModel.getPrice();
            userSentPrice.compute(scModel.getUsername(), (uid, price) ->
                    price == null ? scModel.getPrice() : price + scModel.getPrice());
        }

    }

    void printValue() {
        System.out.println("\u001b\u005b35m" + totalPrice);
        int i = 0;
        for (Map.Entry<String, Double> entry : userSentPrice.entrySet()) {
            if (i++ > 10) {
                break;
            }
            System.out.printf("\u001b\u005b35m{%s:%.2f}%n", entry.getKey(), entry.getValue());
        }
    }
}
