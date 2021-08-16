package org.mokusakura.bilive.core.model;

import java.util.function.Function;

/**
 * <p>
 * Generic Bilibili Message.<br/>
 * Using {@link AbstractDanmakuFactory#create(String)} to create a instance.<br/>
 * Notice that if more inherited classes are defined,
 * call {@link AbstractDanmakuFactory#register(String, Function)} to make sure an instance can be created
 * by calling {@link AbstractDanmakuFactory#create(String)}
 * from a JSONObject in which the value of key "cmd" is the first parameter of
 * {@link AbstractDanmakuFactory#register(String, Function)}
 * </p>
 *
 * @author MokuSakura
 */
public abstract class GenericBilibiliMessage {

    protected MessageType messageType;
    protected String rawMessage;


    public MessageType getDanmakuType() {
        return messageType;
    }

    public GenericBilibiliMessage setDanmakuType(MessageType messageType) {
        this.messageType = messageType;
        return this;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public GenericBilibiliMessage setMessageType(MessageType messageType) {
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
