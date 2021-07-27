package org.mokusakura.danmakurecorder;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mokusakura.danmakurecorder.api.BilibiliApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.Socket;
import java.util.zip.Inflater;

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

    @Test
    void testLink() throws InterruptedException {
        DanmakuClient build = danmakuClientBuilder.build(1314);
        Thread.sleep(100000);
    }

    @Test
    @SneakyThrows
    void testTcpLink() {
        var danmakuServerInfo = apiClient.getDanmakuServerInfo(1314);
        var host = danmakuServerInfo.getData().getHostList()[0].getHost();
        var port = danmakuServerInfo.getData().getHostList()[0].getPort();
        Socket socket = new Socket(host, port);
        var outputStream = socket.getOutputStream();
        var inputStream = socket.getInputStream();
        new Thread(() -> {
            try {
                var bytes = inputStream.readAllBytes();
                Inflater inflater = new Inflater();
                inflater.setInput(bytes);
                var decompressed = new byte[1024];
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
