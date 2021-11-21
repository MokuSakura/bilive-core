package org.mokusakura.bilive.core.event;

import org.mokusakura.bilive.core.model.GenericBilibiliMessage;

/**
 * @author MokuSakura
 */
public class MessageReceivedEvent {
    private GenericBilibiliMessage message;

    public GenericBilibiliMessage getMessage() {
        return message;
    }

    public MessageReceivedEvent setMessage(GenericBilibiliMessage message) {
        this.message = message;
        return this;
    }
}
