package org.mokusakura.bilive.core.api;

import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;
import org.mokusakura.bilive.core.exception.NoRoomFoundException;
import org.mokusakura.bilive.core.model.*;

/**
 * @author MokuSakura
 */
public interface BilibiliApiClient {
    BilibiliApiResponse<RoomInfo> getRoomInfo(int roomId) throws NoRoomFoundException, NoNetworkConnectionException;

    BilibiliApiResponse<RoomInit> getRoomInit(int roomId) throws NoRoomFoundException, NoNetworkConnectionException;

    BilibiliApiResponse<UserInfo> getUserInfo(int roomId) throws NoNetworkConnectionException, NoRoomFoundException;

    BilibiliApiResponse<StreamInfo> getStreamInfo(int roomId) throws NoNetworkConnectionException, NoRoomFoundException;

    BilibiliApiResponse<DanmakuServerInfo> getDanmakuServerInfo(int roomId) throws NoNetworkConnectionException;
}
