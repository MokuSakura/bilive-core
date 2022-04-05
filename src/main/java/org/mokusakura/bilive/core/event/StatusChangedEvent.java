package org.mokusakura.bilive.core.event;

import org.mokusakura.bilive.core.model.StatusChangedMessage;

/**
 * @author MokuSakura
 */
public abstract class StatusChangedEvent<E extends StatusChangedMessage> extends MessageReceivedEvent<E> {
    public StatusChangedEvent(Class<E> messageClass) {
        super(messageClass);
    }

    public boolean isExceptionCaused() {
        return getException() != null;
    }

    public abstract Exception getException();
}
