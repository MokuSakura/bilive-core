package org.mokusakura.bilive.core;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mokusakura.bilive.core.api.BilibiliApiClient;
import org.mokusakura.bilive.core.api.HttpDanmakuApiClient;
import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;
import org.mokusakura.bilive.core.exception.NoRoomFoundException;
import org.mokusakura.bilive.core.model.*;

import java.io.IOException;
import java.net.http.HttpClient;
import java.sql.Date;
import java.text.SimpleDateFormat;
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
@Log4j2
public class TestTcpDanmakuClient {
    Double totalPrice = 0.0;
    Map<Integer, Integer> interactTimes = new HashMap<>();
    Map<String, Double> userSentPrice = new HashMap<>();
    private final BilibiliApiClient apiClient = new HttpDanmakuApiClient(
            HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(20)).build());
    private final Integer connectRoomId = 22571958;
    DanmakuWriter writer = new BasicDanmakuWriter();
    private RoomInfo roomInfo;

    public TestTcpDanmakuClient() {
    }

    @Test
    void testLink() {
        while (true) {
            try {

                roomInfo = apiClient.getRoomInfo(connectRoomId).getData();
                Semaphore semaphore = new Semaphore(0);
                var build = new TcpDanmakuClient(apiClient);
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                writer.enable(String.format("C:\\Users\\MokuSakura\\Documents\\%d_%s_%s.xml",
                                            roomInfo.getShortId() == null || Objects.equals(0,
                                                                                            roomInfo.getShortId()) ? roomInfo.getRoomId() : roomInfo.getShortId(),
                                            roomInfo.getTitle(),
                                            format.format(new Date(System.currentTimeMillis()))));
                build.addLiveBeginHandler(liveBeginEvent -> log.debug("Begin"));
                build.addLiveEndHandlers(liveEndEvent -> {
                    try {
                        writer.close();
                        build.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                build.addReceivedHandlers(event -> onDanmaku(event.getAbstractDanmaku()));
                build.addDisconnectHandlers(event -> {
                    new Thread(() -> {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        while (true) {
                            try {
                                build.connect(event.getRoomId());
                                if (build.isConnected()) {
                                    return;
                                }
                            } catch (NoNetworkConnectionException | NoRoomFoundException e) {
                                e.printStackTrace();
                                try {
                                    Thread.sleep(10000);
                                } catch (InterruptedException interruptedException) {
                                    interruptedException.printStackTrace();
                                }
                            }
                        }
                    }).start();

                });
                ScheduledExecutorService timer = new ScheduledThreadPoolExecutor(1);
                build.connect(connectRoomId);
                timer.scheduleAtFixedRate(this::printValue, 10, 60, TimeUnit.SECONDS);
                timer.scheduleAtFixedRate(
                        () -> log.debug("Memory usage {}", Runtime.getRuntime().totalMemory() / (1024.0 * 1024.0)), 10,
                        60,
                        TimeUnit.SECONDS);
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try {
                        build.close();
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    semaphore.release();
                }));
                semaphore.acquire(1);
                timer.shutdownNow();
            } catch (NoNetworkConnectionException | NoRoomFoundException | IOException | InterruptedException e) {

            }
        }
    }

    @SneakyThrows
    void onDanmaku(AbstractDanmaku danmaku) {
        interactTimes.compute(danmaku.getUid(), (uid, num) -> num == null ? 1 : num + 1);
        writer.writeAsync(danmaku);
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
