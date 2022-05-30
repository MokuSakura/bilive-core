package org.mokusakura.bilive.core.client;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.mokusakura.bilive.core.api.HttpLiveApiClient;
import org.mokusakura.bilive.core.api.model.BilibiliApiResponse;
import org.mokusakura.bilive.core.event.*;
import org.mokusakura.bilive.core.writer.MessageWriter;
import org.mokusakura.bilive.core.writer.XmlMessageWriter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class TestMultiConnectionDanmakuClient {
    private static final Logger log = LogManager.getLogger();
    private final Semaphore semaphore = new Semaphore(1);
    private final AtomicInteger integer = new AtomicInteger(0);
    HttpLiveApiClient liveApiClient = new HttpLiveApiClient();
    List<MultiplexingDanmakuClient> danmakuClients = new ArrayList<>();
    final MessageWriter messageWriter = new XmlMessageWriter();
    DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss");

    public TestMultiConnectionDanmakuClient() throws Exception {
        semaphore.acquire();
        for (int i = 0; i < 10; i++) {
            MultiplexingDanmakuClient danmakuClient = new MultiplexingDanmakuClient(liveApiClient);
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
        MultiConnectionDanmakuClient danmakuClient = MultiConnectionDanmakuClient.newDefault();


        danmakuClient.subscribe(CommentEvent.class, e -> {
            if (messageWriter == null) {
                return;
            }
            integer.incrementAndGet();
            messageWriter.write(e.getMessage());
        });
        danmakuClient.subscribe(SuperChatEvent.class, e -> {
            if (messageWriter == null) {
                return;
            }
            integer.incrementAndGet();
            messageWriter.write(e.getMessage());
        });
        danmakuClient.subscribe(BuyGuardEvent.class, e -> {
            if (messageWriter == null) {
                return;
            }
            integer.incrementAndGet();
            messageWriter.write(e.getMessage());
        });
        danmakuClient.subscribe(SendGiftEvent.class, e -> {
            if (messageWriter == null) {
                return;
            }
            integer.incrementAndGet();
            messageWriter.write(e.getMessage());
        });
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                danmakuClient.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                messageWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        Properties properties = new Properties();
        properties.load(this.getClass().getClassLoader().getResourceAsStream("test/writer.properties"));
        properties.put("roomId", "5265");
        properties.put("time", System.currentTimeMillis());
        Date date = new Date(System.currentTimeMillis());

        properties.put("path",
                       "C:\\Users\\98479\\Desktop\\test\\" + 5265 + "__" + dateFormat.format(date) +
                               ".xml");
        properties.put("bufferSize", "16384");
        messageWriter.enable(properties);
        danmakuClient.subscribe(LiveEndEvent.class, e -> {
            try {
                System.out.println("End");
                messageWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        System.out.println(danmakuClient.connect(5265));
        semaphore.acquire();
    }

    @Test
    public void testMultiConnect() throws Exception {
        String url = "/xlive/web-interface/v1/second/getList?platform=web&parent_area_id=9&area_id=0&sort_type=sort_type_291&page=";

        int count = 0;
        try {
            for (int i = 0; i < 10; i++) {
                MultiplexingDanmakuClient danmakuClient = danmakuClients.get(i);
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
