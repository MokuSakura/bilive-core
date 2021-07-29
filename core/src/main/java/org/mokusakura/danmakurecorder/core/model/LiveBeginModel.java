package org.mokusakura.danmakurecorder.core.model;

import com.alibaba.fastjson.JSON;

import java.util.Objects;

/**
 * @author MokuSakura
 */
public class LiveBeginModel extends GenericBilibiliMessage {
    static {
        register("LIVE", LiveBeginModel::new);
    }

    protected Integer roomId;

    protected LiveBeginModel(String json) {
        var obj = JSON.parseObject(json);
        super.messageType = MessageType.LiveStart;
        var integerRoomId = obj.getInteger("roomid");
        this.roomId = Objects.requireNonNullElseGet(integerRoomId, () -> Integer.valueOf(obj.getString("roomid")));
    }

    public Integer getRoomId() {
        return roomId;
    }

    public LiveBeginModel setRoomId(Integer roomId) {
        this.roomId = roomId;
        return this;
    }
}
