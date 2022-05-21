package org.mokusakura.bilive.core.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mokusakura.bilive.core.api.model.*;
import org.mokusakura.bilive.core.exception.BilibiliApiCodeNotZeroException;
import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;
import org.mokusakura.bilive.core.exception.NoRoomFoundException;

import java.net.http.HttpClient;

/**
 * @author MokuSakura
 */
public class HttpLiveApiClient extends AbstractBilibiliApiClient implements BilibiliLiveApiClient {

    private static final Logger log = LogManager.getLogger(HttpLiveApiClient.class);
    public static final String BILIBILI_LIVE_ROOM_INFO_PATH = "/room/v1/Room/get_info?id=%d";
    public static final String BILIBILI_LIVE_USER_INFO_PATH = "/live_user/v1/UserInfo/get_anchor_in_room?roomid=%d";
    public static final String BILIBILI_LIVE_DANMAKU_INFO_PATH = "/xlive/web-room/v1/index/getDanmuInfo?id=%d&type=0";
    public static final String BILIBILI_LIVE_STREAM_INFO_PATH = "/xlive/web-room/v2/index/getRoomPlayInfo?room_id=%d&protocol=0%%2C1&format=0%%2C2&codec=0%%2C1&qn=10000&platform=web&ptype=16";
    public static final String BILIBILI_LIVE_ROOM_INIT_PATH = "/room/v1/Room/room_init?id=%d";


    public HttpLiveApiClient(HttpClient httpClient) {
        super(httpClient);
    }

    public HttpLiveApiClient() {
        this(HttpClient.newBuilder().build());
    }

    public static BilibiliLiveApiClient getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public BilibiliApiResponse<RoomInfo> getRoomInfo(long roomId) throws NoRoomFoundException,
            NoNetworkConnectionException {
        try {
            return this.get(String.format(BILIBILI_LIVE_ROOM_INFO_PATH, roomId), RoomInfo.class);
        } catch (BilibiliApiCodeNotZeroException e) {
            if (e.getCode().equals(1)) {
                log.info(String.format("Room id %d is not found", roomId));
                throw new NoRoomFoundException(roomId);
            } else {
                log.error(String.format("Unknown code of %d when getting room info. Message from bilibili is: %s",
                                        e.getCode(), e.getBilibiliMessage()));
                throw e;
            }
        }
    }

    @Override
    public BilibiliApiResponse<RoomInit> getRoomInit(long roomId) throws NoRoomFoundException,
            NoNetworkConnectionException {
        try {
            return this.get(String.format(BILIBILI_LIVE_ROOM_INIT_PATH, roomId), RoomInit.class);
        } catch (BilibiliApiCodeNotZeroException e) {
            if (e.getCode().equals(1)) {
                log.info(String.format("Room id %d is not found", roomId));
                throw new NoRoomFoundException(roomId);
            } else {
                log.error(String.format("Unknown code of %d when getting room info. Message from bilibili is: %s",
                                        e.getCode(), e.getBilibiliMessage()));
                throw e;
            }
        }
    }

    @Override
    public BilibiliApiResponse<UserInfo> getUserInfo(long roomId) throws NoNetworkConnectionException,
            NoRoomFoundException {
        try {
            return this.get(String.format(BILIBILI_LIVE_USER_INFO_PATH, roomId), UserInfo.class);
        } catch (BilibiliApiCodeNotZeroException e) {
            if (e.getCode().equals(1)) {
                log.info(String.format("Room id %d is not found", roomId));
                throw new NoRoomFoundException(roomId);
            } else {
                log.error(String.format("Unknown code of %d when getting room info. Message from bilibili is: %s",
                                        e.getCode(), e.getBilibiliMessage()));
                throw e;
            }
        }
    }

    @Override
    public BilibiliApiResponse<StreamInfo> getStreamInfo(long roomId) throws NoNetworkConnectionException,
            NoRoomFoundException {
        try {
            return this.get(String.format(BILIBILI_LIVE_STREAM_INFO_PATH, roomId), StreamInfo.class);
        } catch (BilibiliApiCodeNotZeroException e) {
            if (e.getCode().equals(1)) {
                log.info(String.format("Room id %d is not found", roomId));
                throw new NoRoomFoundException(roomId);
            } else {
                log.error(String.format("Unknown code of %d when getting room info. Message from bilibili is: %s",
                                        e.getCode(), e.getBilibiliMessage()));
                throw e;
            }
        }
    }

    @Override
    public BilibiliApiResponse<DanmakuServerInfo> getDanmakuServerInfo(long roomId) throws
            NoNetworkConnectionException {
        try {
            return this.get(String.format(BILIBILI_LIVE_DANMAKU_INFO_PATH, roomId), DanmakuServerInfo.class);
        } catch (BilibiliApiCodeNotZeroException e) {
            log.error(
                    String.format("Unknown reason for code %d when getting danmaku info. Message from bilibili is: %s",
                                  e.getCode(), e.getBilibiliMessage()));
            return null;
        }

    }

    static class Holder {
        static final HttpLiveApiClient INSTANCE = new HttpLiveApiClient();
    }

}
