package org.mokusakura.bilive.core.model;

import org.mokusakura.bilive.core.util.CloneUtils;

import java.io.Serializable;

/**
 * @author MokuSakura
 */
public class WelcomeMessage extends GenericBilibiliMessage implements Serializable, Cloneable {
    private static final long serializationUID = -26846535216354653L;
    private Integer uid;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return CloneUtils.deepClone(this);
    }

    public WelcomeMessage() {
        super(MessageType.WELCOME);
    }

    public WelcomeMessage(String messageType) {
        super(messageType);
    }

    public WelcomeMessage(String messageType, Long roomId, Long timestamp, String rawMessage, Integer uid) {
        super(messageType, roomId, timestamp, rawMessage);
        this.uid = uid;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WelcomeMessage{");
        sb.append("uid=").append(uid);
        sb.append('}');
        return sb.toString();
    }

    public Integer getUid() {
        return uid;
    }

    public WelcomeMessage setUid(Integer uid) {
        this.uid = uid;
        return this;
    }
}
