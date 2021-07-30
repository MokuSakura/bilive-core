package org.mokusakura.bilive.core.event;

import org.mokusakura.bilive.core.model.AbstractDanmaku;

/**
 * @author MokuSakura
 */
public class DanmakuReceivedEvent {
    private String danmakuJson;
    private Integer roomId;
    private AbstractDanmaku abstractDanmaku;

    public DanmakuReceivedEvent() {
    }

    public DanmakuReceivedEvent(String danmakuJson, Integer roomId,
                                AbstractDanmaku abstractDanmaku) {
        this.danmakuJson = danmakuJson;
        this.roomId = roomId;
        this.abstractDanmaku = abstractDanmaku;
    }

    public String getDanmakuJson() {
        return danmakuJson;
    }

    public DanmakuReceivedEvent setDanmakuJson(String danmakuJson) {
        this.danmakuJson = danmakuJson;
        return this;
    }

    public AbstractDanmaku getAbstractDanmaku() {
        return abstractDanmaku;
    }

    public DanmakuReceivedEvent setAbstractDanmaku(AbstractDanmaku abstractDanmaku) {
        this.abstractDanmaku = abstractDanmaku;
        return this;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public DanmakuReceivedEvent setRoomId(Integer roomId) {
        this.roomId = roomId;
        return this;
    }
}
