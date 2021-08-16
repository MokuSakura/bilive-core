package org.mokusakura.bilive.core.event;

/**
 * @author MokuSakura
 */
public class StatusChangedEvent {
    protected Status status;
    protected Long roomId;
    protected Long uid;
    protected String message;

    public Status getStatus() {
        return status;
    }

    public StatusChangedEvent setStatus(Status status) {
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

    public String getMessage() {
        return message;
    }

    public StatusChangedEvent setMessage(String message) {
        this.message = message;
        return this;
    }

    public enum Status {
        Begin, End, Disconnect
    }
}
