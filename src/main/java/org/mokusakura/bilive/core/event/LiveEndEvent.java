package org.mokusakura.bilive.core.event;

import org.mokusakura.bilive.core.model.LiveEndModel;

/**
 * @author MokuSakura
 */
public class LiveEndEvent {
    private LiveEndModel liveEndModel;

    public LiveEndEvent() {
    }

    public LiveEndEvent(LiveEndModel liveEndModel) {
        this.liveEndModel = liveEndModel;
    }

    public LiveEndModel getLiveEndModel() {
        return liveEndModel;
    }

    public LiveEndEvent setLiveEndModel(LiveEndModel liveEndModel) {
        this.liveEndModel = liveEndModel;
        return this;
    }
}