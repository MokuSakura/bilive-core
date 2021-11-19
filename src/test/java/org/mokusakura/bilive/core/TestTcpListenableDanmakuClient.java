package org.mokusakura.bilive.core;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mokusakura.bilive.core.api.BilibiliLiveApiClient;
import org.mokusakura.bilive.core.api.HttpLiveApiClient;
import org.mokusakura.bilive.core.api.model.RoomInfo;
import org.mokusakura.bilive.core.client.ListenableDanmakuClient;
import org.mokusakura.bilive.core.client.TcpListenableDanmakuClient;
import org.mokusakura.bilive.core.event.StatusChangedEvent;
import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;
import org.mokusakura.bilive.core.exception.NoRoomFoundException;
import org.mokusakura.bilive.core.factory.BilibiliMessageFactory;
import org.mokusakura.bilive.core.factory.DefaultBilibiliMessageFactory;
import org.mokusakura.bilive.core.model.*;
import org.mokusakura.bilive.core.writer.DanmakuWriter;
import org.mokusakura.bilive.core.writer.XmlDanmakuWriter;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author MokuSakura
 */
//@SpringBootTest

@Log4j2
@SuppressWarnings("all")
public class TestTcpListenableDanmakuClient {
    private MonitorApp monitorApp;
    private long roomId = 5060952;
    private String basicPath = "";

    @SneakyThrows
    public TestTcpListenableDanmakuClient() {
        monitorApp = new MonitorApp(new String[]{String.valueOf(roomId), basicPath});

    }

    @Test
    @SneakyThrows
    void testLink() {
        Semaphore semaphore = new Semaphore(1);
        monitorApp.link();
        semaphore.acquire(2);
    }
}

@Log4j2
class MonitorApp {
    private final Map<String, Integer> interactTimes = new HashMap<>();
    private final Map<String, Double> userSentPrice = new HashMap<>();
    private final Set<Long> enteredRoomUsers = new LinkedHashSet<>();
    private final DanmakuWriter writer = new XmlDanmakuWriter();
    private final BilibiliLiveApiClient apiClient = new HttpLiveApiClient(
            HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(20)).build());
    private final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
    private double totalPrice;
    private Semaphore semaphore;
    private Integer connectRoomId = 5265;
    private RoomInfo roomInfo;
    private String basicPath = System.getProperty("user.dir");
    private boolean begin = true;
    private ListenableDanmakuClient danmakuClient;

    public MonitorApp() {
    }

    @SneakyThrows
    public MonitorApp(String[] arg) {
        connectRoomId = Integer.valueOf(arg[0]);
        basicPath = arg[1];
        roomInfo = apiClient.getRoomInfo(connectRoomId).getData();
        semaphore = new Semaphore(1);
        semaphore.acquire();
        BilibiliMessageFactory[] factories = new BilibiliMessageFactory[]{new Factory0(),
                new Factory1(),
                new Factory3(),
                new Factory4(),
                new Factory5(),
                new Factory6(),
                new Factory7(),
                new Factory8(),
                new Factory9(),
                new Factory10()};
        Map<Short, BilibiliMessageFactory> registerMap = new HashMap<>();
        for (int i = 0; i < factories.length; i++) {
            registerMap.put((short) (i >= 2 ? i + 1 : i), factories[i]);
        }
        BilibiliMessageFactory messageFactory = DefaultBilibiliMessageFactory.createDefault(registerMap);
        danmakuClient = new TcpListenableDanmakuClient(apiClient, messageFactory);
        danmakuClient.addMessageReceivedListener(event -> onMessage(event.getMessage()));
        danmakuClient.addStatusChangedListener(this::onStatusChanged);
    }

    public static void main(String[] args) {
        MonitorApp app;
        if (args.length <= 1) {
            app = new MonitorApp();
        } else {
            app = new MonitorApp(args);
        }
        app.link();
    }

    void link() {
        try {
            ScheduledExecutorService timer = new ScheduledThreadPoolExecutor(1);
            if (Objects.equals(1, roomInfo.getLiveStatus())) {
                begin = true;
                String path = String.format("%s%s%d%s%d_%s.xml",
                                            basicPath,
                                            File.separator,
                                            roomInfo.getUid(),
                                            File.separator,
                                            roomInfo.getShortId() == null ||
                                                    Objects.equals(0L,
                                                                   roomInfo.getShortId()) ? roomInfo.getRoomId() :
                                                    roomInfo.getShortId(),
                                            format.format(new java.util.Date(System.currentTimeMillis())));
                try {
                    writer.enable(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            timer.scheduleAtFixedRate(this::printValue, 10, 60, TimeUnit.SECONDS);
            Runtime.getRuntime().addShutdownHook(new Thread(this::onEnd));
            danmakuClient.connect(connectRoomId);
            semaphore.acquire(1);
        } catch (NoNetworkConnectionException | NoRoomFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void onStatusChanged(StatusChangedEvent event) {
        switch (event.getStatus()) {
            case Begin:
                synchronized (this) {
                    if (begin) {
                        return;
                    }
                    begin = true;
                    String path = String.format("%s%s%d%s%d_%s.xml",
                                                basicPath,
                                                File.separator,
                                                roomInfo.getUid(),
                                                File.separator,
                                                roomInfo.getShortId() == null ||
                                                        Objects.equals(0L,
                                                                       roomInfo.getShortId()) ? roomInfo.getRoomId() :
                                                        roomInfo.getShortId(),
                                                format.format(new java.util.Date(System.currentTimeMillis())));
                    try {
                        writer.enable(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case End:
                onEnd();
                break;
            case Disconnect:
                onDisconnect();
                break;
        }
    }

    @SneakyThrows
    private void onMessage(GenericBilibiliMessage message) {
        if (!(message instanceof AbstractDanmaku)) {
            return;
        }
        AbstractDanmaku danmaku = (AbstractDanmaku) message;
        synchronized (this) {
            if (!begin) {
                return;
            }
        }
        interactTimes.compute(danmaku.getUsername(), (uid, num) -> num == null ? 1 : num + 1);
        enteredRoomUsers.add(danmaku.getUid());
        writer.writeAsync(danmaku);
        if (danmaku instanceof SendGiftModel) {
            SendGiftModel giftModel = (SendGiftModel) danmaku;
            if (Objects.equals(5, giftModel.getGiftType())) {
                return;
            }
//            log.debug("{} send {}×{}", giftModel.getUsername(), giftModel.getGiftName(), giftModel.getGiftNumber());
            userSentPrice.compute(giftModel.getUsername(), (uid, price) ->
                    price == null ?
                            giftModel.getPrice() * giftModel.getNum() :
                            price + giftModel.getPrice() * giftModel.getNum());
            totalPrice += giftModel.getPrice() * giftModel.getNum();
        } else if (danmaku instanceof GuardBuyModel) {
            GuardBuyModel guardBuyModel = (GuardBuyModel) danmaku;

            log.debug("{} buy {}", guardBuyModel.getUsername(), guardBuyModel.getGuardName());
            userSentPrice.compute(guardBuyModel.getUsername(), (uid, price) ->
                    price == null ? guardBuyModel.getGiftPrice() : price + guardBuyModel.getGiftPrice());
            totalPrice += guardBuyModel.getGiftPrice();
        } else if (danmaku instanceof SCModel) {
            SCModel scModel = (SCModel) danmaku;
//            scModel.setPrice(scModel.getPrice() * 1000);
//            log.debug("{} buy {} SC {}", scModel.getUsername(), scModel.getPrice(), scModel.getMessage());
            totalPrice += scModel.getPrice() * 1000;
            userSentPrice.compute(scModel.getUsername(), (uid, price) ->
                    price == null ? scModel.getPrice() : price + scModel.getPrice() * 1000);
        }
    }

    private void onEnd() {
        try {
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onDisconnect() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            boolean succeed = false;
            while (!succeed) {
                try {
                    danmakuClient.connect(connectRoomId);
                    String path = String.format("%s%s%d%s%d_%s.xml",
                                                basicPath,
                                                File.separator,
                                                roomInfo.getUid(),
                                                File.separator,
                                                roomInfo.getShortId() == null ||
                                                        Objects.equals(0L,
                                                                       roomInfo.getShortId()) ? roomInfo.getRoomId() :
                                                        roomInfo.getShortId(),
                                                format.format(new java.util.Date(System.currentTimeMillis())));
                    writer.enable(path);
                    succeed = true;
                } catch (NoNetworkConnectionException | NoRoomFoundException | IOException e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(Duration.ofSeconds(10L).toMillis());
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }
        }).start();

    }

    void printValue() {
        System.out.println("\u001b\u005b35mTotal price:" + decimalFormat.format(totalPrice) + "\u001b\u005b0m");
        System.out.println("\u001b\u005b35mTotal user count:" + interactTimes.size() + "\u001b\u005b0m");
        System.out.println(
                "\u001b\u005b35mTotal entered room user count:" + enteredRoomUsers.size() + "\u001b\u005b0m");
        System.out.println("\u001b\u005b35mTotal spent money user count:" + userSentPrice.size() + "\u001b\u005b0m");
//        System.out.println(
//                "\u001b\u005b35mTotal user with 水溅跃 medal count:" + userWithLiyuuMedal.size() + "\u001b\u005b0m");

        userSentPrice.entrySet()
                .stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(10)
                .forEach((entry) -> System.out.printf("\u001b\u005b35m{%s:%.2f}%n\u001b\u005b0m", entry.getKey(),
                                                      entry.getValue()));

    }
}

@Log4j2
class Factory0 implements BilibiliMessageFactory {
    @Override
    public List<GenericBilibiliMessage> create(BilibiliWebSocketFrame frame) {
        log.debug("{}: {}", frame.getWebSocketHeader().getProtocolVersion(), new String(frame.getWebSocketBody(),
                                                                                        StandardCharsets.UTF_8));
        return new ArrayList<>();
    }
}

@Log4j2
class Factory1 implements BilibiliMessageFactory {
    @Override
    public List<GenericBilibiliMessage> create(BilibiliWebSocketFrame frame) {
        log.debug("{}: {}", frame.getWebSocketHeader().getProtocolVersion(), new String(frame.getWebSocketBody()));
        return new ArrayList<>();
    }
}

@Log4j2
class Factory3 implements BilibiliMessageFactory {
    @Override
    public List<GenericBilibiliMessage> create(BilibiliWebSocketFrame frame) {
        log.debug("{}: {}", frame.getWebSocketHeader().getProtocolVersion(), new String(frame.getWebSocketBody()));
        return new ArrayList<>();
    }
}

@Log4j2
class Factory4 implements BilibiliMessageFactory {
    @Override
    public List<GenericBilibiliMessage> create(BilibiliWebSocketFrame frame) {
        log.debug("{}: {}", frame.getWebSocketHeader().getProtocolVersion(), new String(frame.getWebSocketBody()));
        return new ArrayList<>();
    }
}

@Log4j2
class Factory5 implements BilibiliMessageFactory {
    @Override
    public List<GenericBilibiliMessage> create(BilibiliWebSocketFrame frame) {
        log.debug("{}: {}", frame.getWebSocketHeader().getProtocolVersion(), new String(frame.getWebSocketBody()));
        return new ArrayList<>();
    }
}

@Log4j2
class Factory6 implements BilibiliMessageFactory {
    @Override
    public List<GenericBilibiliMessage> create(BilibiliWebSocketFrame frame) {
        log.debug("{}: {}", frame.getWebSocketHeader().getProtocolVersion(), new String(frame.getWebSocketBody()));
        return new ArrayList<>();
    }
}

@Log4j2
class Factory7 implements BilibiliMessageFactory {
    @Override
    public List<GenericBilibiliMessage> create(BilibiliWebSocketFrame frame) {
        log.debug("{}: {}", frame.getWebSocketHeader().getProtocolVersion(), new String(frame.getWebSocketBody()));
        return new ArrayList<>();
    }
}

@Log4j2
class Factory8 implements BilibiliMessageFactory {
    @Override
    public List<GenericBilibiliMessage> create(BilibiliWebSocketFrame frame) {
        log.debug("{}: {}", frame.getWebSocketHeader().getProtocolVersion(), new String(frame.getWebSocketBody()));
        return new ArrayList<>();
    }
}

@Log4j2
class Factory9 implements BilibiliMessageFactory {
    @Override
    public List<GenericBilibiliMessage> create(BilibiliWebSocketFrame frame) {
        log.debug("{}: {}", frame.getWebSocketHeader().getProtocolVersion(), new String(frame.getWebSocketBody()));
        return new ArrayList<>();
    }
}

@Log4j2
class Factory10 implements BilibiliMessageFactory {
    @Override
    public List<GenericBilibiliMessage> create(BilibiliWebSocketFrame frame) {
        log.debug("{}: {}", frame.getWebSocketHeader().getProtocolVersion(), new String(frame.getWebSocketBody()));
        return new ArrayList<>();
    }
}

