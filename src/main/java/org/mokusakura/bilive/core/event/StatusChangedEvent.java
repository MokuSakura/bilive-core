package org.mokusakura.bilive.core.event;

import org.mokusakura.bilive.core.model.GenericBilibiliMessage;

/**
 * @author MokuSakura
 */
public class StatusChangedEvent {
    protected String status;
    protected Long roomId;
    protected Long uid;
    protected GenericBilibiliMessage message;

    public String getStatus() {
        return status;
    }

    public StatusChangedEvent setStatus(String status) {
        this.status = status;
        return this;
    }

    public Long getRoomId() {
        return roomId;
    }

    public StatusChangedEvent setRoomId(Long roomId) {
        this.roomId = roomId;
        return this;
    }

    public Long getUid() {
        return uid;
    }

    public StatusChangedEvent setUid(Long uid) {
        this.uid = uid;
        return this;
    }

    public GenericBilibiliMessage getMessage() {
        return message;
    }

    public StatusChangedEvent setMessage(GenericBilibiliMessage message) {
        this.message = message;
        return this;
    }

    public static class Status {
        public static final String BEGIN = "Begin";
        public static final String PREPARING = "Preparing";
        public static final String DISCONNECT = "Disconnect";
    }
}
