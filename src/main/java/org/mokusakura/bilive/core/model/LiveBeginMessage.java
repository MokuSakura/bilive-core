package org.mokusakura.bilive.core.model;

import com.alibaba.fastjson.JSONObject;
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

public class LiveBeginMessage extends StatusChangedMessage implements Serializable, Cloneable {
    private static final long serializationUID = 56426857468L;
    private Long roomid;
    private String liveKey;
    private String voiceBackground;
    private String subSessionKey;
    private String livePlatform;
    private String liveModel;

    public LiveBeginMessage(String messageType, String status) {
        super(messageType, status);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return CloneUtils.deepClone(this);
    }

    public LiveBeginMessage(String messageType, Long roomId, Long timestamp, String rawMessage, String status,
                            Long roomid, String liveKey, String voiceBackground, String subSessionKey,
                            String livePlatform, String liveModel) {
        super(messageType, roomId, timestamp, rawMessage, status);
        this.roomid = roomid;
        this.liveKey = liveKey;
        this.voiceBackground = voiceBackground;
        this.subSessionKey = subSessionKey;
        this.livePlatform = livePlatform;
        this.liveModel = liveModel;
    }

    public static LiveBeginMessage createFromJson(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);

        LiveBeginMessage res = jsonObject.toJavaObject(LiveBeginMessage.class);
        res.setStatus(Status.BEGIN)
                .setRawMessage(json)
                .setMessageType(MessageType.LIVE_START);

        if (res.getRoomid() == null) {
            try {
                res.setRoomid(Long.valueOf(jsonObject.getString("roomid")));
            } catch (Exception ignored) {
            }
        }
        return res;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LiveBeginMessage{");
        sb.append("roomid=").append(roomid);
        sb.append(", liveKey='").append(liveKey).append('\'');
        sb.append(", voiceBackground='").append(voiceBackground).append('\'');
        sb.append(", subSessionKey='").append(subSessionKey).append('\'');
        sb.append(", livePlatform='").append(livePlatform).append('\'');
        sb.append(", liveModel='").append(liveModel).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public Long getRoomid() {
        return roomid;
    }

    public LiveBeginMessage setRoomid(Long roomid) {
        this.roomid = roomid;
        return this;
    }

    public String getLiveKey() {
        return liveKey;
    }

    public LiveBeginMessage setLiveKey(String liveKey) {
        this.liveKey = liveKey;
        return this;
    }

    public String getVoiceBackground() {
        return voiceBackground;
    }

    public LiveBeginMessage setVoiceBackground(String voiceBackground) {
        this.voiceBackground = voiceBackground;
        return this;
    }

    public String getSubSessionKey() {
        return subSessionKey;
    }

    public LiveBeginMessage setSubSessionKey(String subSessionKey) {
        this.subSessionKey = subSessionKey;
        return this;
    }

    public String getLivePlatform() {
        return livePlatform;
    }

    public LiveBeginMessage setLivePlatform(String livePlatform) {
        this.livePlatform = livePlatform;
        return this;
    }

    public String getLiveModel() {
        return liveModel;
    }

    public LiveBeginMessage setLiveModel(String liveModel) {
        this.liveModel = liveModel;
        return this;
    }
}
