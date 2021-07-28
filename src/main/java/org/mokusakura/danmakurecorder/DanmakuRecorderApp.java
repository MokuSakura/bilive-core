package org.mokusakura.danmakurecorder;

import org.mokusakura.danmakurecorder.core.DanmakuClientBuilder;
import org.mokusakura.danmakurecorder.core.exception.NoNetworkConnectionException;
import org.mokusakura.danmakurecorder.core.exception.NoRoomFoundException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author MokuSakura
 */
@SpringBootApplication
public class DanmakuRecorderApp implements CommandLineRunner {
    public static void main(String[] args) {
        var applicationContext = SpringApplication.run(DanmakuRecorderApp.class, args);
        var danmakuClientBuilder = applicationContext.getBean(DanmakuClientBuilder.class);

        try {
            var build = danmakuClientBuilder.build(103);
            build.connect(103);
            Thread.sleep(100000);
        } catch (NoNetworkConnectionException | NoRoomFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
