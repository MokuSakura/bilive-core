package org.mokusakura.danmakurecorder;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author MokuSakura
 */
@SpringBootTest
public class TestSocketDanmakuClient {
    @Autowired
    private DanmakuClientBuilder danmakuClientBuilder;

    @Test
    void testLink() throws InterruptedException {
        DanmakuClient build = danmakuClientBuilder.build(22528847);
        Thread.sleep(100000);
    }
}
