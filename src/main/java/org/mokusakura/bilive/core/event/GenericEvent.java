package org.mokusakura.bilive.core.event;


import org.mokusakura.bilive.core.client.DanmakuClient;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;


/**
 * @author MokuSakura
 */

public abstract class GenericEvent<E extends GenericBilibiliMessage> {
    private final Class<E> messageClass;
    private long roomId;
    private E message;
    private DanmakuClient danmakuClient;

    public GenericEvent(Class<E> messageClass) {
        this.messageClass = messageClass;
    }

    public String getEventName() {
        return messageClass.getSimpleName();
    }


    public long getRoomId() {
        return roomId;
    }

    public GenericEvent<E> setRoomId(long roomId) {
        this.roomId = roomId;
        return this;
    }

    public E getMessage() {
        return message;
    }

    public GenericEvent<E> setMessage(E message) {
        this.message = message;
        return this;
    }

    public Class<E> getMessageClass() {
        return messageClass;
    }

    public DanmakuClient getDanmakuClient() {
        return danmakuClient;
    }

    public GenericEvent<E> setDanmakuClient(DanmakuClient danmakuClient) {
        this.danmakuClient = danmakuClient;
        return this;
    }
}
