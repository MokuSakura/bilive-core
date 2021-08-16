package org.mokusakura.bilive.core.event;

import org.mokusakura.bilive.core.model.AbstractDanmaku;

/**
 * @author MokuSakura
 */
public class DanmakuReceivedEvent {
    private String danmakuJson;
    private Long roomId;
    private AbstractDanmaku abstractDanmaku;

    public DanmakuReceivedEvent() {
    }

    public DanmakuReceivedEvent(String danmakuJson, Long roomId,
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

    public Long getRoomId() {
        return roomId;
    }

    public DanmakuReceivedEvent setRoomId(Long roomId) {
        this.roomId = roomId;
        return this;
    }
}
