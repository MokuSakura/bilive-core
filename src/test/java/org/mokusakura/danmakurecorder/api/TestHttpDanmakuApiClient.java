package org.mokusakura.danmakurecorder.api;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mokusakura.danmakurecorder.exception.NoNetworkConnectionException;
import org.mokusakura.danmakurecorder.exception.NoRoomFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

/**
 * @author MokuSakura
 */
@SpringBootTest
@Slf4j
public class TestHttpDanmakuApiClient {
    private static final Integer NOT_EXISTS_ROOM_ID = 4065;
    private static final Integer EXISTS_ROOM_ID = 477;
    @Autowired
    private HttpDanmakuApiClient danmakuClient;

    @Test
    @SneakyThrows
    public void testGetRoomInfo() {
        try {
            danmakuClient.getRoomInfo(NOT_EXISTS_ROOM_ID);
            Assert.isTrue(false, "This room id is may used now. You can change the room id above to make another test");
        } catch (NoRoomFoundException ignored) {
        } catch (NoNetworkConnectionException e) {
            Assert.isTrue(false,
                          "Please connect to the internet to test the application. If you sure that you have connected to the internet, there is something wrong when invoking apis. Please contact the author.");
        }
        var roomInfo = danmakuClient.getRoomInfo(EXISTS_ROOM_ID);
        Assert.notNull(roomInfo, "");
        Assert.isTrue(roomInfo.getCode().equals(0), "");
        Assert.notNull(roomInfo.getData().getShortId(), "");
    }

    @Test
    @SneakyThrows
    public void testGetDanmakuServer() {

        var danmakuServer = danmakuClient.getDanmakuServerInfo(EXISTS_ROOM_ID);
        Assert.isTrue(danmakuServer.getCode().equals(0), "");
        Assert.notEmpty(danmakuServer.getData().getHostList(), "");
        Assert.notEmpty(danmakuServer.getData().getHostList(), "");
        Assert.notNull(danmakuServer.getData().getHostList()[0].getHost(), "");
        Assert.notNull(danmakuServer.getData().getHostList()[0].getPort(), "");
        log.debug(danmakuServer.getData().getHostList()[0].getHost());
        log.debug(danmakuServer.getData().getHostList()[0].getPort().toString());
    }
}
