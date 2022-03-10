package org.mokusakura.bilive.core.model;

import com.alibaba.fastjson.JSON;
import lombok.*;
import lombok.experimental.Accessors;
import org.mokusakura.bilive.core.util.CloneUtils;

import java.io.Serializable;

/**
 * @author MokuSakura
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LiveEndModel extends GenericStatusChangedModel implements Serializable, Cloneable {
    private static final long serializationUID = -9874265486584L;
    private Long roomid;

    public static LiveEndModel createFromJson(String json) {
        var obj = JSON.parseObject(json);
        LiveEndModel res = new LiveEndModel();
        res.setStatus(Status.PREPARING);
        res.setMessageType(MessageType.LIVE_END);
        res.setRoomid(obj.getLongValue("roomid"));
        if (res.getRoomid() == null) {
            res.setRoomid(Long.valueOf(obj.getString("roomid")));
        }
        return res;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return CloneUtils.deepClone(this);
    }

}
