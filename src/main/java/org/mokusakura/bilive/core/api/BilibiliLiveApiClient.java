package org.mokusakura.bilive.core.api;

import org.mokusakura.bilive.core.api.model.*;
import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;
import org.mokusakura.bilive.core.exception.NoRoomFoundException;

/**
 * @author MokuSakura
 */
public interface BilibiliLiveApiClient extends BilibiliApiClient {
    BilibiliApiResponse<RoomInfo> getRoomInfo(long roomId) throws NoRoomFoundException, NoNetworkConnectionException;

    BilibiliApiResponse<RoomInit> getRoomInit(long roomId) throws NoRoomFoundException, NoNetworkConnectionException;

    BilibiliApiResponse<UserInfo> getUserInfo(long roomId) throws NoNetworkConnectionException, NoRoomFoundException;

    BilibiliApiResponse<StreamInfo> getStreamInfo(long roomId) throws NoNetworkConnectionException,
            NoRoomFoundException;

    BilibiliApiResponse<DanmakuServerInfo> getDanmakuServerInfo(long roomId) throws NoNetworkConnectionException;
}
