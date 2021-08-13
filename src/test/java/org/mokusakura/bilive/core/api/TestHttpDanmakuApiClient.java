package org.mokusakura.bilive.core.api;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;
import org.mokusakura.bilive.core.exception.NoRoomFoundException;

import java.net.http.HttpClient;

/**
 * @author MokuSakura
 */
@Log4j2
public class TestHttpDanmakuApiClient {
    private static final Integer NOT_EXISTS_ROOM_ID = 4065;
    private static final Integer EXISTS_ROOM_ID = 477;
    private final BilibiliApiClient danmakuClient;

    public TestHttpDanmakuApiClient() {
        danmakuClient = new HttpDanmakuApiClient(HttpClient.newHttpClient());
    }

    @Test
    @SneakyThrows
    public void testGetRoomInfo() {
        try {
            danmakuClient.getRoomInfo(NOT_EXISTS_ROOM_ID);
            //"This room id is may used now. You can change the room id above to make another test");
            assert false;
        } catch (NoRoomFoundException ignored) {
        } catch (NoNetworkConnectionException e) {
//            "Please connect to the internet to test the application. If you sure that you have connected to the internet, there is something wrong when invoking apis. Please contact the author."
            assert false;

        }
        var roomInfo = danmakuClient.getRoomInfo(EXISTS_ROOM_ID);
        assert roomInfo != null;
        assert roomInfo.getCode().equals(0);
        assert roomInfo.getData().getShortId() != null;
    }

    @Test
    @SneakyThrows
    public void testGetDanmakuServer() {

        var danmakuServer = danmakuClient.getDanmakuServerInfo(EXISTS_ROOM_ID);
        assert danmakuServer.getCode().equals(0);
        assert danmakuServer.getData().getHostList().length != 0;
        assert danmakuServer.getData().getHostList()[0].getHost() != null;
        assert danmakuServer.getData().getHostList()[0].getPort() != null;
        log.debug(danmakuServer.getData().getHostList()[0].getHost());
        log.debug(danmakuServer.getData().getHostList()[0].getPort().toString());
    }
}
