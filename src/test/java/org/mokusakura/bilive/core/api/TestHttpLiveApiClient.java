package org.mokusakura.bilive.core.api;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;
import org.mokusakura.bilive.core.exception.NoRoomFoundException;

import java.net.http.HttpClient;

/**
 * @author MokuSakura
 */

public class TestHttpLiveApiClient {
    private static final Logger log = LogManager.getLogger(TestHttpLiveApiClient.class);
    private static final Integer NOT_EXISTS_ROOM_ID = 4065;
    private static final Integer EXISTS_ROOM_ID = 477;
    private final BilibiliLiveApiClient danmakuClient;

    public TestHttpLiveApiClient() {
        danmakuClient = new HttpLiveApiClient(HttpClient.newHttpClient());
    }

    @Test
    public void testGetRoomInfo() {
        try {
            danmakuClient.getRoomInfo(NOT_EXISTS_ROOM_ID);
            //"This room id is may used now. You can change the room id above to make another test");
            assert false;

            var roomInfo = danmakuClient.getRoomInfo(EXISTS_ROOM_ID);
            assert roomInfo != null;
            assert roomInfo.getCode().equals(0);
            assert roomInfo.getData().getShortId() != null;
        } catch (NoRoomFoundException ignored) {
        } catch (NoNetworkConnectionException e) {
//            "Please connect to the internet to test the application. If you sure that you have connected to the internet, there is something wrong when invoking apis. Please contact the author."
            assert false;

        }
    }

    @Test
    public void testGetDanmakuServer() {
        try {
            var danmakuServer = danmakuClient.getDanmakuServerInfo(EXISTS_ROOM_ID);
            assert danmakuServer.getCode().equals(0);
            assert danmakuServer.getData().getHostList().length != 0;
            assert danmakuServer.getData().getHostList()[0].getHost() != null;
            assert danmakuServer.getData().getHostList()[0].getPort() != null;
            log.debug(danmakuServer.getData().getHostList()[0].getHost());
            log.debug(danmakuServer.getData().getHostList()[0].getPort().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
