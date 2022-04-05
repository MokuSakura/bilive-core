package org.mokusakura.bilive.core.event;

import org.mokusakura.bilive.core.model.LiveBeginMessage;

/**
 * @author MokuSakura
 */
public class LiveBeginEvent extends StatusChangedEvent<LiveBeginMessage> {
    public LiveBeginEvent(Class<LiveBeginMessage> messageClass) {
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
