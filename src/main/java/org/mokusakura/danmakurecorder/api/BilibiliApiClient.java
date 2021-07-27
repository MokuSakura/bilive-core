package org.mokusakura.danmakurecorder.api;

import org.mokusakura.danmakurecorder.exception.NoNetworkConnectionException;
import org.mokusakura.danmakurecorder.exception.NoRoomFoundException;
import org.mokusakura.danmakurecorder.model.*;

import java.io.Closeable;

/**
 * @author MokuSakura
 */
public interface BilibiliApiClient extends Closeable {
    BilibiliApiResponse<RoomInfo> getRoomInfo(int roomId) throws NoRoomFoundException, NoNetworkConnectionException;

    BilibiliApiResponse<RoomInit> getRoomInit(int roomId) throws NoRoomFoundException, NoNetworkConnectionException;

    BilibiliApiResponse<UserInfo> getUserInfo(int roomId) throws NoNetworkConnectionException, NoRoomFoundException;

    BilibiliApiResponse<StreamInfo> getStreamInfo(int roomId) throws NoNetworkConnectionException, NoRoomFoundException;

    BilibiliApiResponse<DanmakuServerInfo> getDanmakuServerInfo(int roomId) throws NoNetworkConnectionException;
}
