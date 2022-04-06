package org.mokusakura.bilive.core.model;

import com.alibaba.fastjson.JSON;
import org.mokusakura.bilive.core.util.CloneUtils;

import java.io.Serializable;

/**
 * @author MokuSakura
 */
public class LiveEndMessage extends StatusChangedMessage implements Serializable, Cloneable {
    private static final long serializationUID = -9874265486584L;
    private Long roomid;

    public LiveEndMessage(String messageType, String status) {
        super(messageType, status);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return CloneUtils.deepClone(this);
    }

    public LiveEndMessage(String messageType, Long roomId, Long timestamp, String rawMessage, String status,
                          Long roomid) {
        super(messageType, roomId, timestamp, rawMessage, status);
        this.roomid = roomid;
    }

    public static LiveEndMessage createFromJson(String json) {
        var obj = JSON.parseObject(json);
        LiveEndMessage res = new LiveEndMessage(MessageType.LIVE_END, Status.PREPARING);
        res.setRoomid(obj.getLongValue("roomid"));
        if (res.getRoomid() == null) {
            res.setRoomid(Long.valueOf(obj.getString("roomid")));
        }
        return res;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LiveEndMessage{");
        sb.append("roomid=").append(roomid);
        sb.append('}');
        return sb.toString();
    }

    public Long getRoomid() {
        return roomid;
    }

    public LiveEndMessage setRoomid(Long roomid) {
        this.roomid = roomid;
        return this;
    }
}
