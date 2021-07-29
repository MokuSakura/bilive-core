package org.mokusakura.danmakurecorder.core.event;

import org.mokusakura.danmakurecorder.core.model.GenericBilibiliMessage;

/**
 * @author MokuSakura
 */
public class OtherEvent {
    private GenericBilibiliMessage message;
    private Integer roomId;

    public OtherEvent(GenericBilibiliMessage message, Integer roomId) {
        this.message = message;
        this.roomId = roomId;
    }

    public OtherEvent() {
    }

    public GenericBilibiliMessage getMessage() {
        return message;
    }

    public OtherEvent setMessage(GenericBilibiliMessage message) {
        this.message = message;
        return this;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public OtherEvent setRoomId(Integer roomId) {
        this.roomId = roomId;
        return this;
    }
}
