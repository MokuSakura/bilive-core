package org.mokusakura.bilive.core.client;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.mokusakura.bilive.core.api.HttpLiveApiClient;
import org.mokusakura.bilive.core.api.model.BilibiliApiResponse;
import org.mokusakura.bilive.core.event.BuyGuardEvent;
import org.mokusakura.bilive.core.event.CommentEvent;
import org.mokusakura.bilive.core.event.SendGiftEvent;
import org.mokusakura.bilive.core.event.SuperChatEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class TestMultiConnectionDanmakuClient {
    private static final Logger log = LogManager.getLogger();
    private final Semaphore semaphore = new Semaphore(1);
    private final AtomicInteger integer = new AtomicInteger(0);
    HttpLiveApiClient liveApiClient = new HttpLiveApiClient();
    List<MultiConnectionDanmakuClient> danmakuClients = new ArrayList<>();

    public TestMultiConnectionDanmakuClient() throws Exception {
        semaphore.acquire();
        for (int i = 0; i < 10; i++) {
            MultiConnectionDanmakuClient danmakuClient = new MultiConnectionDanmakuClient(liveApiClient);
            danmakuClient.subscribe(CommentEvent.class, e -> integer.incrementAndGet());
            danmakuClient.subscribe(SuperChatEvent.class, e -> integer.incrementAndGet());
            danmakuClient.subscribe(SendGiftEvent.class, e -> integer.incrementAndGet());
            danmakuClient.subscribe(BuyGuardEvent.class, e -> integer.incrementAndGet());
            danmakuClients.add(danmakuClient);
        }
        new Timer(true).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println(integer.get());
            }
        }, 0, 1000 * 10);

    }

    @Test
    public void testConnect() throws Exception {
        MultiConnectionDanmakuClient danmakuClient = danmakuClients.get(0);
//        danmakuClient.subscribe(CommentEvent.class,e->integer.incrementAndGet());
//        danmakuClient.subscribe(SuperChatEvent.class,e->integer.incrementAndGet());
//        danmakuClient.subscribe(BuyGuardEvent.class,e->integer.incrementAndGet());
//        danmakuClient.subscribe(SendGiftEvent.class,e->integer.incrementAndGet());
        danmakuClient.connect(555);
        semaphore.acquire();
    }

    @Test
    public void testMultiConnect() throws Exception {
        String url = "/xlive/web-interface/v1/second/getList?platform=web&parent_area_id=9&area_id=0&sort_type=sort_type_291&page=";

        int count = 0;
        try {
            for (int i = 0; i < 10; i++) {
                MultiConnectionDanmakuClient danmakuClient = danmakuClients.get(i);
                for (int page = 1; page <= 1; page++) {
                    String urlWithParam = url + page;
                    BilibiliApiResponse<JSONObject> hashMapBilibiliApiResponse = liveApiClient.get(urlWithParam,
                                                                                                   JSONObject.class);
                    JSONArray list = hashMapBilibiliApiResponse.getData().getJSONArray("list");
                    for (JSONObject jsonObject : list.toJavaList(JSONObject.class)) {
                        Integer roomid = jsonObject.getInteger("roomid");
                        danmakuClient.connect(roomid);
                        System.out.println(++count);
                    }
                }
            }

//            for(int i = 0;i < 10;i++){
//                MultiConnectionDanmakuClient danmakuClient = danmakuClients.get(i);
//                for(int j = 0;j < 100;j++){
//                    danmakuClient.connect(555);
//                    count++;
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(count);
        semaphore.acquire();
    }
}
