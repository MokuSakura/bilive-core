package org.mokusakura.bilive.core.event;

import org.mokusakura.bilive.core.model.LiveEndMessage;

/**
 * @author MokuSakura
 */
public class LiveEndEvent extends StatusChangedEvent<LiveEndMessage> {
    public LiveEndEvent(Class<LiveEndMessage> messageClass) {
        super(messageClass);
    }

    @Override
    public boolean isExceptionCaused() {
        return false;
    }

    @Override
    public Exception getException() {
        return null;
    }
}
