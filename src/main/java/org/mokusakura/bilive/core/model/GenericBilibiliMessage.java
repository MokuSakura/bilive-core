package org.mokusakura.bilive.core.model;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * <p>
 * Generic Bilibili Message.<br/>
 * Using {@link GenericBilibiliMessageFactory#create(String)} to create a instance.<br/>
 * Notice that if more inherited classes are defined,
 * call {@link GenericBilibiliMessageFactory#register(String, Function)} to make sure an instance can be created
 * by calling {@link GenericBilibiliMessageFactory#create(String)}
 * from a JSONObject in which the value of key "cmd" is the first parameter of
 * {@link GenericBilibiliMessageFactory#register(String, Function)}
 * </p>
 *
 * @author MokuSakura
 */
@Slf4j
public abstract class GenericBilibiliMessage {

    private static boolean inited = false;
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
