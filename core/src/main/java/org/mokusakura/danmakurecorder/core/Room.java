package org.mokusakura.danmakurecorder.core;

import lombok.extern.slf4j.Slf4j;
import org.mokusakura.danmakurecorder.core.api.BilibiliApiClient;
import org.springframework.context.ApplicationContext;

/**
 * @author MokuSakura
 */
@Slf4j
public class Room {
    private final BilibiliApiClient danmakuApiClient;
    private final ApplicationContext applicationContext;
    private Integer roomId;

    public Room(BilibiliApiClient danmakuApiClient, ApplicationContext applicationContext) {
        this.danmakuApiClient = danmakuApiClient;
        this.applicationContext = applicationContext;
    }

    void connect(Integer shortId) {


    }
}
