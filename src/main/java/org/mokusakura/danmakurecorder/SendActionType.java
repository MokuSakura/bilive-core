package org.mokusakura.danmakurecorder;

/**
 * @author MokuSakura
 */
public enum SendActionType {
    Hello(7), HeartBeat(2);
    private final int value;

    SendActionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
