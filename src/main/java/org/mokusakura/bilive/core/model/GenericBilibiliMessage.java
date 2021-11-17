package org.mokusakura.bilive.core.model;

import org.mokusakura.bilive.core.factory.BilibiliMessageFactory;

/**
 * <p>
 * Generic Bilibili Message.<br/>
 * Using {@link BilibiliMessageFactory#create(BilibiliWebSocketFrame)} )} to create a instance.<br/>
 * </p>
 *
 * @author MokuSakura
 */
public abstract class GenericBilibiliMessage {

    protected String messageType;
    protected String rawMessage;

    public String getMessageType() {
        return messageType;
    }

    public GenericBilibiliMessage setMessageType(String messageType) {
        this.messageType = messageType;
        return this;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public GenericBilibiliMessage setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
        return this;
    }
}
