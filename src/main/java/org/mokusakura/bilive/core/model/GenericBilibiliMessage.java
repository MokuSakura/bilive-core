package org.mokusakura.bilive.core.model;

import org.mokusakura.bilive.core.factory.BilibiliMessageFactory;
import org.mokusakura.bilive.core.util.CloneUtils;

import java.io.Serializable;

/**
 * <p>
 * Generic Bilibili Message.
 * Using {@link BilibiliMessageFactory#create(BilibiliWebSocketFrame)} )} to create a instance.
 * </p>
 *
 * @author MokuSakura
 */

public abstract class GenericBilibiliMessage implements Serializable, Cloneable {
    public static final long serializationUID = -3584268486724L;
    private String messageType;
    private Long roomId;
    private Long timestamp;
    private String rawMessage;

    public GenericBilibiliMessage(String messageType) {
        this.messageType = messageType;
    }

    public GenericBilibiliMessage(String messageType, Long roomId, Long timestamp, String rawMessage) {
        this.messageType = messageType;
        this.roomId = roomId;
        this.timestamp = timestamp;
        this.rawMessage = rawMessage;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return CloneUtils.deepClone(this);
    }

    public String getMessageType() {
        return messageType;
    }

    public GenericBilibiliMessage setMessageType(String messageType) {
        this.messageType = messageType;
        return this;
    }

    public Long getRoomId() {
        return roomId;
    }

    public GenericBilibiliMessage setRoomId(Long roomId) {
        this.roomId = roomId;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public GenericBilibiliMessage setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
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
