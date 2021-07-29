package org.mokusakura.danmakurecorder;

import org.mokusakura.danmakurecorder.core.DanmakuClient;
import org.mokusakura.danmakurecorder.core.DanmakuClientBuilder;
import org.mokusakura.danmakurecorder.core.exception.NoNetworkConnectionException;
import org.mokusakura.danmakurecorder.core.exception.NoRoomFoundException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author MokuSakura
 */
@SpringBootApplication
public class DanmakuRecorderApp implements CommandLineRunner {
    private static final Lock lock = new ReentrantLock();
    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(DanmakuRecorderApp.class, args);
        var builder = applicationContext.getBean(DanmakuClientBuilder.class);
        DanmakuClient client = builder.build(103);
        try {
            client.connect(103);
        } catch (NoNetworkConnectionException | NoRoomFoundException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }).start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Closed");
            client.disconnect();
        }));
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
