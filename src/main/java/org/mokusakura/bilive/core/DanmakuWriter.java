package org.mokusakura.bilive.core;

import org.mokusakura.bilive.core.model.AbstractDanmaku;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Future;

/**
 * @author MokuSakura
 */
public interface DanmakuWriter extends Closeable {
    Future<Void> writeAsync(AbstractDanmaku danmaku);

    void enable(String path) throws IOException;

}
