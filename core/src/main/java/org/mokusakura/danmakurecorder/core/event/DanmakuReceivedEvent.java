package org.mokusakura.danmakurecorder.core.event;

import org.mokusakura.danmakurecorder.core.model.DanmakuModelBase;

/**
 * @author MokuSakura
 */
public class DanmakuReceivedEvent {
    private String danmakuJson;
    private DanmakuModelBase danmakuModelBase;

    public DanmakuReceivedEvent() {
    }


    public String getDanmakuRaw() {
        return danmakuJson;
    }

    public DanmakuReceivedEvent setDanmakuRaw(String danmakuJson) {
        this.danmakuJson = danmakuJson;
        return this;
    }

    public String getDanmakuJson() {
        return danmakuJson;
    }

    public DanmakuReceivedEvent setDanmakuJson(String danmakuJson) {
        this.danmakuJson = danmakuJson;
        return this;
    }

    public DanmakuModelBase getDanmakuModelBase() {
        return danmakuModelBase;
    }

    public DanmakuReceivedEvent setDanmakuModelBase(
            DanmakuModelBase danmakuModelBase) {
        this.danmakuModelBase = danmakuModelBase;
        return this;
    }
}
