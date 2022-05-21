package org.mokusakura.bilive.core;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.mokusakura.bilive.core.api.BilibiliLiveApiClient;
import org.mokusakura.bilive.core.api.HttpLiveApiClient;
import org.mokusakura.bilive.core.client.SubscribableClientImpl;
import org.mokusakura.bilive.core.event.BuyGuardEvent;
import org.mokusakura.bilive.core.event.CommentEvent;
import org.mokusakura.bilive.core.event.SendGiftEvent;
import org.mokusakura.bilive.core.event.SuperChatEvent;
import org.mokusakura.bilive.core.factory.BilibiliMessageFactoryDispatcher;
import org.mokusakura.bilive.core.factory.PopularityBilibiliMessageFactory;
import org.mokusakura.bilive.core.listener.AsyncListener;
import org.mokusakura.bilive.core.listener.ParallelListener;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

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
    private final List<SubscribableClientImpl> danmakuClients = new ArrayList<>();
    private final AtomicInteger integer = new AtomicInteger(0);

    public TestFactory() throws Exception {

        httpClient = HttpClient.newHttpClient();
        semaphore.acquire();
        bilibiliLiveApiClient = new HttpLiveApiClient(httpClient);
        bilibiliMessageFactory = BilibiliMessageFactoryDispatcher.newDefault();
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
        danmakuClient.connect(958617);
        semaphore.acquire();
    }

    @Test
    void testMultiConnect() throws Exception {
        String url1 = "/xlive/web-interface/v1/second/getList?platform=web&parent_area_id=";
        String url2 = "&area_id=0&page=";
        int count = 0;
        int[] areaIds = {1, 2, 3, 5, 6, 9, 10, 11, 13};
        try {
            for (int i = 0; i < 1000; i++) {
                SubscribableClientImpl danmakuClient = new SubscribableClientImpl();
                danmakuClient.subscribe(CommentEvent.class, e -> {
                    integer.incrementAndGet();
                });
                danmakuClient.subscribe(SendGiftEvent.class, e -> {
                    integer.incrementAndGet();
                });
                danmakuClient.subscribe(SuperChatEvent.class, e -> {
                    integer.incrementAndGet();
                });
                danmakuClient.subscribe(BuyGuardEvent.class, e -> {
                    integer.incrementAndGet();
                });
                danmakuClient.connect(555);
                count++;
            }
//            for (int areaId : areaIds) {
//                inner:
//                for (int page = 1; page <= 5; page++) {
//
//                    String urlWithParam = url1 + areaId + url2 + page;
//                    BilibiliApiResponse<JSONObject> hashMapBilibiliApiResponse = bilibiliLiveApiClient.get(urlWithParam,
//                                                                                                           JSONObject.class);
//                    JSONArray list = hashMapBilibiliApiResponse.getData().getJSONArray("list");
//                    for (JSONObject jsonObject : list.toJavaList(JSONObject.class)) {
//                        SubscribableClientImpl danmakuClient = new SubscribableClientImpl();
//                        danmakuClient.subscribe(CommentEvent.class, e -> {
//                            integer.incrementAndGet();
//                        });
//                        danmakuClient.subscribe(SendGiftEvent.class, e -> {
//                            integer.incrementAndGet();
//                        });
//                        danmakuClient.subscribe(SuperChatEvent.class, e -> {
//                            integer.incrementAndGet();
//                        });
//                        danmakuClient.subscribe(BuyGuardEvent.class, e -> {
//                            integer.incrementAndGet();
//                        });
//                        Integer roomid = jsonObject.getInteger("roomid");
//                        danmakuClient.connect(roomid);
//                        count++;
//                    }
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(count);
        new Timer(true).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println(integer.get());
            }
        }, 0, 10000);
        semaphore.acquire();
    }

}
