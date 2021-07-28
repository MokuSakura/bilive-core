package org.mokusakura.danmakurecorder.api;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.mokusakura.danmakurecorder.exception.BilibiliApiCodeNotZeroException;
import org.mokusakura.danmakurecorder.exception.NoNetworkConnectionException;
import org.mokusakura.danmakurecorder.exception.NoRoomFoundException;
import org.mokusakura.danmakurecorder.model.*;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author MokuSakura
 */
@Slf4j
@Component
public class HttpDanmakuApiClient implements BilibiliApiClient, Closeable {
    public static final String HEADER_ACCEPT = "application/json, text/javascript, */*; q=0.01";
    public static final String HEADER_ORIGIN = "https://live.bilibili.com";
    public static final String HEADER_REFERER = "https://live.bilibili.com/";
    public static final String HEADER_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36";
    public static final String BILIBILI_LIVE_API_HOST = "api.live.bilibili.com";
    public static final String BILIBILI_LIVE_ROOM_INFO_PATH = "/room/v1/Room/get_info?id=%d";
    public static final String BILIBILI_LIVE_USER_INFO_PATH = "/live_user/v1/UserInfo/get_anchor_in_room?roomid=%d";
    public static final String BILIBILI_LIVE_DANMAKU_INFO_PATH = "/xlive/web-room/v1/index/getDanmuInfo?id=%d&type=0";
    public static final String BILIBILI_LIVE_STREAM_INFO_PATH = "/xlive/web-room/v2/index/getRoomPlayInfo?room_id=%d&protocol=0%%2C1&format=0%%2C2&codec=0%%2C1&qn=10000&platform=web&ptype=16";
    public static final String BILIBILI_LIVE_ROOM_INIT_PATH = "/room/v1/Room/room_init?id=%d";
    private final HttpClient httpClient;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(false);
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public HttpDanmakuApiClient(ApplicationContext context) {
        this.httpClient = context.getBean(HttpClient.class);
    }

    @Override
    public BilibiliApiResponse<RoomInfo> getRoomInfo(int roomId) throws NoRoomFoundException,
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
    public BilibiliApiResponse<RoomInit> getRoomInit(int roomId) throws NoRoomFoundException,
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
    public BilibiliApiResponse<UserInfo> getUserInfo(int roomId) throws NoNetworkConnectionException,
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
    public BilibiliApiResponse<StreamInfo> getStreamInfo(int roomId) throws NoNetworkConnectionException,
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
    public BilibiliApiResponse<DanmakuServerInfo> getDanmakuServerInfo(int roomId) throws NoNetworkConnectionException {
        try {
            return this.get(String.format(BILIBILI_LIVE_DANMAKU_INFO_PATH, roomId), DanmakuServerInfo.class);
        } catch (BilibiliApiCodeNotZeroException e) {
            log.error(
                    String.format("Unknown reason for code %d when getting danmaku info. Message from bilibili is: %s",
                                  e.getCode(), e.getBilibiliMessage()));
            return null;
        }

    }

    @Override
    public void close() {
        try {
            readWriteLock.writeLock().lock();
            closed.set(true);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private <T> BilibiliApiResponse<T> get(String path, Class<T> actualClass) throws NoNetworkConnectionException {
        try {
            readWriteLock.readLock().lock();
            if (closed.get()) {
                throw new RuntimeException("Http client is closed");
            }
            var request = HttpRequest.newBuilder()
                    .GET()
                    .uri(new URI("https://" + BILIBILI_LIVE_API_HOST + path))
                    .header("Accept", HEADER_ACCEPT)
                    .header("Origin", HEADER_ORIGIN)
                    .header("Referer", HEADER_REFERER)
                    .header("User-Agent", HEADER_USER_AGENT)
                    .build();
            log.debug(request.uri().toString());
            var response = httpClient.send(request,
                                           HttpResponse.BodyHandlers.ofString());
            var res = JSONObject.parseObject(response.body(),
                                             new TypeReference<BilibiliApiResponse<T>>(actualClass) {});
            if (!res.getCode().equals(0)) {
                throw new BilibiliApiCodeNotZeroException(request.uri().toString(), res.getMessage(), res.getCode());
            }
            return res;
        } catch (ConnectException e) {
            log.info("No network connection");
            log.debug(e.getClass().toString());
            throw new NoNetworkConnectionException();
        } catch (BilibiliApiCodeNotZeroException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unknown exception when getting room info.");
            log.error(e.getClass().toString());
            log.error(Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }
}
