package org.mokusakura.danmakurecorder;

import org.mokusakura.danmakurecorder.api.BilibiliApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BilibiliDanmakuRecorderApplication implements CommandLineRunner {
    BilibiliApiClient bilibiliApiClient;

    @Autowired
    public BilibiliDanmakuRecorderApplication(BilibiliApiClient bilibiliApiClient) {
        this.bilibiliApiClient = bilibiliApiClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(BilibiliDanmakuRecorderApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
