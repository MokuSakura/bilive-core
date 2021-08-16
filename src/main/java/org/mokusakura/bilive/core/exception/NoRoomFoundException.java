package org.mokusakura.bilive.core.exception;

/**
 * @author MokuSakura
 */
public class NoRoomFoundException extends Exception {
    private Long roomId;

    public NoRoomFoundException(Long roomId) {
        this.roomId = roomId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public NoRoomFoundException setRoomId(Long roomId) {
        this.roomId = roomId;
        return this;
    }
}
