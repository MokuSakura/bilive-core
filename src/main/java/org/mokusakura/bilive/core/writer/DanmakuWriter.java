package org.mokusakura.bilive.core.writer;

import org.mokusakura.bilive.core.model.AbstractDanmaku;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author MokuSakura
 */
public interface DanmakuWriter extends Closeable {
    Future<Void> writeAsync(AbstractDanmaku danmaku);

    default void write(AbstractDanmaku danmaku) throws ExecutionException, InterruptedException {
        this.writeAsync(danmaku).get();
    }

    void enable(String path) throws IOException;

}
