package org.mokusakura.bilive.core.model;

import com.alibaba.fastjson.JSONObject;
import lombok.*;
import lombok.experimental.Accessors;
import org.mokusakura.bilive.core.util.CloneUtils;

import java.io.Serializable;

/**
 * @author MokuSakura
 * {
 * "cmd": "LIVE",
 * "live_key": "183098067901036888",
 * "voice_background": "",
 * "sub_session_key": "183098067901036888sub_time:1637288805",
 * "live_platform": "pc_link",
 * "live_model": 0,
 * "roomid": 5060952
 * }
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LiveBeginModel extends GenericStatusChangedModel implements Serializable, Cloneable {
    private static final long serializationUID = 56426857468L;
    private Long roomid;
    private String liveKey;
    private String voiceBackground;
    private String subSessionKey;
    private String livePlatform;
    private String liveModel;

    public static LiveBeginModel createFromJson(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);

        LiveBeginModel res = jsonObject.toJavaObject(LiveBeginModel.class);
        res.setStatus(Status.BEGIN)
                .setRawMessage(json)
                .setMessageType(MessageType.LIVE_START);

        if (res.getRoomid() == null) {
            res.setRoomid(Long.valueOf(jsonObject.getString("roomid")));
        }
        return res;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return CloneUtils.deepClone(this);
    }
}
