package org.mokusakura.danmakurecorder.core.event;

import org.mokusakura.danmakurecorder.core.DanmakuClient;
import org.mokusakura.danmakurecorder.core.model.RoomInfo;
import org.mokusakura.danmakurecorder.core.model.RoomInit;

import java.net.URI;

/**
 * @author MokuSakura
 */
public class DanmakuClientClosedEvent {
    private DanmakuClient danmakuClient;
    private URI uri;
    private RoomInfo roomInfo;
    private RoomInit roomInit;

    public DanmakuClientClosedEvent(DanmakuClient danmakuClient, URI uri,
                                    RoomInfo roomInfo, RoomInit roomInit) {
        this.danmakuClient = danmakuClient;
        this.uri = uri;
        this.roomInfo = roomInfo;
        this.roomInit = roomInit;
    }

    public DanmakuClient getDanmakuClient() {
        return danmakuClient;
    }

    public DanmakuClientClosedEvent setDanmakuClient(DanmakuClient danmakuClient) {
        this.danmakuClient = danmakuClient;
        return this;
    }

    public URI getUri() {
        return uri;
    }

    public DanmakuClientClosedEvent setUri(URI uri) {
        this.uri = uri;
        return this;
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public DanmakuClientClosedEvent setRoomInfo(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
        return this;
    }

    public RoomInit getRoomInit() {
        return roomInit;
    }

    public DanmakuClientClosedEvent setRoomInit(RoomInit roomInit) {
        this.roomInit = roomInit;
        return this;
    }
}
