package org.mokusakura.bilive.core;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mokusakura.bilive.core.api.BilibiliLiveApiClient;
import org.mokusakura.bilive.core.api.HttpLiveApiClient;
import org.mokusakura.bilive.core.client.DanmakuClient;
import org.mokusakura.bilive.core.client.TcpListenableDanmakuClient;
import org.mokusakura.bilive.core.factory.DefaultBilibiliMessageFactory;
import org.mokusakura.bilive.core.factory.PopularityBilibiliMessageFactory;

import java.net.http.HttpClient;

/**
 * @author MokuSakura
 */
@Log4j2
public class TestFactory {
    private final DanmakuClient danmakuClient;
    private final BilibiliLiveApiClient bilibiliLiveApiClient;
    private final DefaultBilibiliMessageFactory bilibiliMessageFactory;
    private final HttpClient httpClient;

    public TestFactory() {
        httpClient = HttpClient.newHttpClient();
        bilibiliLiveApiClient = new HttpLiveApiClient(httpClient);
        bilibiliMessageFactory = DefaultBilibiliMessageFactory.createDefault();
        PopularityBilibiliMessageFactory popularityBilibiliMessageFactory = new PopularityBilibiliMessageFactory();
        bilibiliMessageFactory.register((short) 1, popularityBilibiliMessageFactory);
        danmakuClient = new TcpListenableDanmakuClient(bilibiliLiveApiClient, bilibiliMessageFactory);
    }

    @SneakyThrows
    @Test
    public static void main(String[] args) {
        TestFactory factory = new TestFactory();
        factory.link();
    }

    @SneakyThrows
    void link() {
        danmakuClient.connect(54897);
    }
}
