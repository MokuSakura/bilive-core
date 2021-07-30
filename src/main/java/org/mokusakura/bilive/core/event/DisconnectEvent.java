package org.mokusakura.bilive.core.event;

/**
 * @author MokuSakura
 */
public class DisconnectEvent {
    private Integer roomId;

    public DisconnectEvent(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public DisconnectEvent setRoomId(Integer roomId) {
        this.roomId = roomId;
        return this;
    }
}
