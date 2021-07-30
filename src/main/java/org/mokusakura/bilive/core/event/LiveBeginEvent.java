package org.mokusakura.bilive.core.event;

import org.mokusakura.bilive.core.model.LiveBeginModel;

/**
 * @author MokuSakura
 */
public class LiveBeginEvent {
    private LiveBeginModel liveBeginModel;

    public LiveBeginEvent() {
    }

    public LiveBeginEvent(LiveBeginModel liveBeginModel) {
        this.liveBeginModel = liveBeginModel;
    }

    public LiveBeginModel getLiveBeginModel() {
        return liveBeginModel;
    }

    public LiveBeginEvent setLiveBeginModel(LiveBeginModel liveBeginModel) {
        this.liveBeginModel = liveBeginModel;
        return this;
    }
}
