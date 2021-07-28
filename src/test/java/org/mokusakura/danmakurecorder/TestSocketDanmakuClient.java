package org.mokusakura.danmakurecorder;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mokusakura.danmakurecorder.api.BilibiliApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author MokuSakura
 */
@SpringBootTest
@Slf4j
public class TestSocketDanmakuClient {
    @Autowired
    private DanmakuClientBuilder danmakuClientBuilder;
    @Autowired
    private BilibiliApiClient apiClient;

    @SneakyThrows
    @Test
    void testLink() throws InterruptedException {
        DanmakuClient build = danmakuClientBuilder.build(102);
        build.connect(102);
        Thread.sleep(100000);
    }
}
