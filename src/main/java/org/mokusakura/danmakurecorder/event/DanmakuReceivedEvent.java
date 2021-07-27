package org.mokusakura.danmakurecorder.event;

import org.mokusakura.danmakurecorder.model.DanmakuModelBase;

/**
 * @author MokuSakura
 */
public class DanmakuReceivedEvent {
    private DanmakuModelBase danmakuModel;

    public DanmakuModelBase getDanmakuModel() {
        return danmakuModel;
    }

    public DanmakuReceivedEvent setDanmakuModel(DanmakuModelBase danmakuModel) {
        this.danmakuModel = danmakuModel;
        return this;
    }
}
