package org.mokusakura.danmakurecorder;

import org.mokusakura.danmakurecorder.api.BilibiliApiClient;
import org.mokusakura.danmakurecorder.exception.NoNetworkConnectionException;
import org.mokusakura.danmakurecorder.exception.NoRoomFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * @author MokuSakura
 */
@Component
public class DanmakuClientBuilder {
    @Autowired
    private BilibiliApiClient apiClient;

    public DanmakuClient build(int shortId) {
        DanmakuClient danmakuClient = null;
        try {
            var roomInit = this.apiClient.getRoomInit(shortId);
            var roomInfo = this.apiClient.getRoomInfo(shortId);
            danmakuClient = new SocketDanmakuClient(URI.create("wss://broadcastlv.chat.bilibili.com:2245/sub"),
                                                    roomInit.getData(), roomInfo.getData());
        } catch (NoRoomFoundException | NoNetworkConnectionException e) {
            e.printStackTrace();
        }
        return danmakuClient;
    }


}
