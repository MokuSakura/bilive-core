package org.mokusakura.bilive.core.writer;

import org.mokusakura.bilive.core.model.GenericBilibiliMessage;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;

/**
 * @author MokuSakura
 */
public interface MessageWriter extends Closeable {

    void write(GenericBilibiliMessage danmaku);

    boolean enable(Properties properties) throws IOException;
}
