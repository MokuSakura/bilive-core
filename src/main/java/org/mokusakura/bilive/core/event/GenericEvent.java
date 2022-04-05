package org.mokusakura.bilive.core.event;


import org.mokusakura.bilive.core.model.GenericBilibiliMessage;


/**
 * @author MokuSakura
 */

public abstract class GenericEvent<E extends GenericBilibiliMessage> {
    private final Class<E> messageClass;
    private long roomId;
    private E message;

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
}
