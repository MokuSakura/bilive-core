package org.mokusakura.danmakurecorder.core.protocol;

/**
 * @author MokuSakura
 */
public enum ActionType {
    Default(0), HeartBeat(2), Popularity(3), GlobalInfo(5), Hello(7), EnterRoom(8);
    private final int value;

    ActionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
