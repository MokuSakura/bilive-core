package org.mokusakura.bilive.core.model;

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
