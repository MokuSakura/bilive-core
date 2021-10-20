package org.mokusakura.bilive.core.model;

import com.alibaba.fastjson.JSON;

import java.util.Objects;

/**
 * @author MokuSakura
 */
public class LiveEndModel extends GenericBilibiliMessage {

    private Integer roomId;

    public LiveEndModel(String json) {
        var obj = JSON.parseObject(json);
        super.messageType = MessageType.LiveEnd;
        var integerRoomId = obj.getInteger("roomid");
        this.roomId = Objects.requireNonNullElseGet(integerRoomId, () -> Integer.valueOf(obj.getString("roomid")));
    }

    public Integer getRoomId() {
        return roomId;
    }

    public LiveEndModel setRoomId(Integer roomId) {
        this.roomId = roomId;
        return this;
    }
}
