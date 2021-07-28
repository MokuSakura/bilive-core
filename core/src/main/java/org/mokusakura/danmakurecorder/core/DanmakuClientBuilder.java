package org.mokusakura.danmakurecorder.core;

import org.mokusakura.danmakurecorder.core.api.BilibiliApiClient;
import org.springframework.stereotype.Component;

/**
 * @author MokuSakura
 */
@Component
public class DanmakuClientBuilder {

    private final BilibiliApiClient apiClient;

    public DanmakuClientBuilder(BilibiliApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public DanmakuClient build(int id) {
        DanmakuClient danmakuClient = null;
        try {
            danmakuClient = new TcpDanmakuClient(apiClient);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return danmakuClient;
    }


}
