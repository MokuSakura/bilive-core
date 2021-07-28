package org.mokusakura.danmakurecorder;

import org.mokusakura.danmakurecorder.api.BilibiliApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author MokuSakura
 */
@Component
public class DanmakuClientBuilder {
    @Autowired
    private BilibiliApiClient apiClient;

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
