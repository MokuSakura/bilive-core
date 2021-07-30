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
        build.addReceivedHandlers(event -> onDanmaku(event.getAbstractDanmaku()));
        build.addDisconnectHandlers(event -> semaphore.release());
        ScheduledExecutorService timer = new ScheduledThreadPoolExecutor(1);
        build.connect(510);
        timer.scheduleAtFixedRate(this::printValue, 10, 60, TimeUnit.SECONDS);
        timer.scheduleAtFixedRate(
                () -> log.debug("Memory usage {}", Runtime.getRuntime().totalMemory() / (1024.0 * 1024.0)), 10, 60,
                TimeUnit.SECONDS);
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
            scModel.setPrice(scModel.getPrice() * 1000);
            log.debug("{} buy {} SC {}", scModel.getUsername(), scModel.getPrice(), scModel.getMessage());
            totalPrice += scModel.getPrice();
            userSentPrice.compute(scModel.getUsername(), (uid, price) ->
                    price == null ? scModel.getPrice() : price + scModel.getPrice());
        }
    }

    void printValue() {
        System.out.println("\u001b\u005b35mTotal price:" + totalPrice + "\u001b\u005b0m");
        System.out.println("\u001b\u005b35mTotal user count:" + interactTimes.size() + "\u001b\u005b0m");
        userSentPrice.entrySet()
                .stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(10)
                .forEach((entry) -> System.out.printf("\u001b\u005b35m{%s:%.2f}%n\u001b\u005b0m", entry.getKey(),
                                                      entry.getValue()));

    }
}
