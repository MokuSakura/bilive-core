package org.mokusakura.bilive.core.writer;

import org.mokusakura.bilive.core.model.AbstractDanmaku;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;

/**
 * @author MokuSakura
 */
public interface DanmakuWriter extends Closeable {

    void write(AbstractDanmaku danmaku);

    boolean enable(Properties properties) throws IOException;
}
