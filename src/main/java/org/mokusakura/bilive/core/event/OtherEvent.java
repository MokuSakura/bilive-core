package org.mokusakura.bilive.core.event;

import org.mokusakura.bilive.core.model.GenericBilibiliMessage;

/**
 * @author MokuSakura
 */
public class OtherEvent {
    private GenericBilibiliMessage message;
    private Long roomId;

    public OtherEvent(GenericBilibiliMessage message, Long roomId) {
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

    public Long getRoomId() {
        return roomId;
    }

    public OtherEvent setRoomId(Long roomId) {
        this.roomId = roomId;
        return this;
    }
}
