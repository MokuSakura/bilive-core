package org.mokusakura.bilive;

import org.springframework.boot.CommandLineRunner;
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

    }

    @Override
    public void run(String... args) throws Exception {

    }
}
