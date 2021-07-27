package org.mokusakura.danmakurecorder.exception;

/**
 * @author MokuSakura
 */
public class NoRoomFoundException extends Exception {
    private Integer roomId;

    public NoRoomFoundException(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public NoRoomFoundException setRoomId(Integer roomId) {
        this.roomId = roomId;
        return this;
    }
}
