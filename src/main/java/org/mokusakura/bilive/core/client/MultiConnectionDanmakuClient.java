package org.mokusakura.bilive.core.client;

import java.io.IOException;
/**
 * <p>
 * This interface is used to connect to multiple danmaku servers.
 * <p>
 * Unlike {@link TcpDanmakuClient},
 * this class is able to connect to multiple danmaku servers with only a few threads.
 * It usually shows higher performance and uses less memory.
 * <p>
 * Though this class usually only use one thread, we still don't promise messages' order.
 * <p>
 * In this class, you can use {@link #connect(long)} to connect to a danmaku server,
 * or use {@link #disconnect(long)} to disconnect from a danmaku server.
 *
 * @author MokuSakura
 */
public interface MultiConnectionDanmakuClient extends SubscribableClient {
    static MultiConnectionDanmakuClient newDefault() {
        return new MultiplexingDanmakuClient();
    }

    boolean disconnect(long roomId) throws IOException;
}
