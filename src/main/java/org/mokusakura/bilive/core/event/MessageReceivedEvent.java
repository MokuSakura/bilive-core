package org.mokusakura.bilive.core.event;

import org.mokusakura.bilive.core.model.GenericBilibiliMessage;

/**
 * @author MokuSakura
 */
public abstract class MessageReceivedEvent<E extends GenericBilibiliMessage> extends GenericEvent<E> {
    public MessageReceivedEvent(Class<E> messageClass) {
        super(messageClass);
    }
}
