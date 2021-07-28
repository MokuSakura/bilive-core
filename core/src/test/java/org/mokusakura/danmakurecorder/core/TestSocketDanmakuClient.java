package org.mokusakura.danmakurecorder.core;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mokusakura.danmakurecorder.core.api.HttpDanmakuApiClient;

import java.net.http.HttpClient;

/**
 * @author MokuSakura
 */
//@SpringBootTest
@Slf4j
public class TestSocketDanmakuClient {

    private final DanmakuClientBuilder danmakuClientBuilder;

    public TestSocketDanmakuClient() {
        danmakuClientBuilder = new DanmakuClientBuilder(new HttpDanmakuApiClient(HttpClient.newHttpClient()));
    }

    @SneakyThrows
    @Test
    void testLink() throws InterruptedException {
        DanmakuClient build = danmakuClientBuilder.build(103);
        build.connect(103);
        Thread.sleep(100000);
    }
}
