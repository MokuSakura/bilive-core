package org.mokusakura.bilive.core.client;

import org.mokusakura.bilive.core.model.GenericBilibiliMessage;

import java.time.Duration;

/**
 * @author MokuSakura
 * <p>
 * This interface doesn't ensure all message will preserved.
 * If next is called not so frequent while too many messages are retrieved, some messages may loss.
 * </p>
 */
public interface IterableDanmakuClient extends DanmakuClient {
    boolean isNextAvailable();


    Duration getGetWaitTime();

    IterableDanmakuClient setWaitTime(Duration waitTime);

    default GenericBilibiliMessage next() throws InterruptedException {
        return next(getGetWaitTime());
    }


    GenericBilibiliMessage next(Duration duration) throws InterruptedException;
}
