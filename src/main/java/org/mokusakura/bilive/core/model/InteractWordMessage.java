package org.mokusakura.bilive.core.model;

import com.alibaba.fastjson.JSONObject;
import org.mokusakura.bilive.core.util.CloneUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MokuSakura
 */

public class InteractWordMessage extends GenericBilibiliMessage implements Serializable, Cloneable {
    private static final long serialVersionUID = 32342356535445L;

    private final Map<String, Object> additionalProperties = new HashMap<>();
    private Contribution contribution;
    private Integer dmscore;
    private FansMedal fansMedal;
    private List<Integer> identities = new ArrayList<>();
    private Integer isSpread;
    private Integer msgType;
    private Integer roomid;
    private Long score;
    private String spreadDesc;
    private String spreadInfo;
    private Integer tailIcon;
    private Long triggerTime;
    private String unameColor;

    public InteractWordMessage() {
        this(MessageType.INTERACT_WORD);
    }

    public InteractWordMessage(String messageType) {
        super(messageType);
    }

    public InteractWordMessage(String messageType, Long roomId, Long timestamp, String rawMessage,
                               Contribution contribution, Integer dmscore,
                               FansMedal fansMedal, List<Integer> identities, Integer isSpread, Integer msgType,
                               Integer roomid, Long score, String spreadDesc, String spreadInfo, Integer tailIcon,
                               Long triggerTime, String unameColor) {
        super(messageType, roomId, timestamp, rawMessage);
        this.contribution = contribution;
        this.dmscore = dmscore;
        this.fansMedal = fansMedal;
        this.identities = identities;
        this.isSpread = isSpread;
        this.msgType = msgType;
        this.roomid = roomid;
        this.score = score;
        this.spreadDesc = spreadDesc;
        this.spreadInfo = spreadInfo;
        this.tailIcon = tailIcon;
        this.triggerTime = triggerTime;
        this.unameColor = unameColor;
    }

    public static InteractWordMessage createFromJson(String json) {
        JSONObject obj = JSONObject.parseObject(json);
        JSONObject dataObject = obj.getJSONObject("data");
        if (dataObject != null) {
            obj = dataObject;
        }
        InteractWordMessage res = obj.toJavaObject(InteractWordMessage.class);
        res.setMessageType(MessageType.INTERACT_WORD);
        res.setRawMessage(json);
        return res;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return CloneUtils.deepClone(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("InteractWordMessage{");
        sb.append("additionalProperties=").append(additionalProperties);
        sb.append(", contribution=").append(contribution);
        sb.append(", dmscore=").append(dmscore);
        sb.append(", fansMedal=").append(fansMedal);
        sb.append(", identities=").append(identities);
        sb.append(", isSpread=").append(isSpread);
        sb.append(", msgType=").append(msgType);
        sb.append(", roomid=").append(roomid);
        sb.append(", score=").append(score);
        sb.append(", spreadDesc='").append(spreadDesc).append('\'');
        sb.append(", spreadInfo='").append(spreadInfo).append('\'');
        sb.append(", tailIcon=").append(tailIcon);
        sb.append(", triggerTime=").append(triggerTime);
        sb.append(", unameColor='").append(unameColor).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public Contribution getContribution() {
        return contribution;
    }

    public InteractWordMessage setContribution(
            Contribution contribution) {
        this.contribution = contribution;
        return this;
    }

    public Integer getDmscore() {
        return dmscore;
    }

    public InteractWordMessage setDmscore(Integer dmscore) {
        this.dmscore = dmscore;
        return this;
    }

    public FansMedal getFansMedal() {
        return fansMedal;
    }

    public InteractWordMessage setFansMedal(FansMedal fansMedal) {
        this.fansMedal = fansMedal;
        return this;
    }

    public List<Integer> getIdentities() {
        return identities;
    }

    public InteractWordMessage setIdentities(List<Integer> identities) {
        this.identities = identities;
        return this;
    }

    public Integer getIsSpread() {
        return isSpread;
    }

    public InteractWordMessage setIsSpread(Integer isSpread) {
        this.isSpread = isSpread;
        return this;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public InteractWordMessage setMsgType(Integer msgType) {
        this.msgType = msgType;
        return this;
    }

    public Integer getRoomid() {
        return roomid;
    }

    public InteractWordMessage setRoomid(Integer roomid) {
        this.roomid = roomid;
        return this;
    }

    public Long getScore() {
        return score;
    }

    public InteractWordMessage setScore(Long score) {
        this.score = score;
        return this;
    }

    public String getSpreadDesc() {
        return spreadDesc;
    }

    public InteractWordMessage setSpreadDesc(String spreadDesc) {
        this.spreadDesc = spreadDesc;
        return this;
    }

    public String getSpreadInfo() {
        return spreadInfo;
    }

    public InteractWordMessage setSpreadInfo(String spreadInfo) {
        this.spreadInfo = spreadInfo;
        return this;
    }

    public Integer getTailIcon() {
        return tailIcon;
    }

    public InteractWordMessage setTailIcon(Integer tailIcon) {
        this.tailIcon = tailIcon;
        return this;
    }

    public Long getTriggerTime() {
        return triggerTime;
    }

    public InteractWordMessage setTriggerTime(Long triggerTime) {
        this.triggerTime = triggerTime;
        return this;
    }

    public String getUnameColor() {
        return unameColor;
    }

    public InteractWordMessage setUnameColor(String unameColor) {
        this.unameColor = unameColor;
        return this;
    }

    public static class FansMedal implements Serializable, Cloneable {
        private static final long serialVersionUID = -35422342356535445L;
        private final Map<String, Object> additionalProperties = new HashMap<>();
        private Integer anchorRoomid;
        private Integer guardLevel;
        private Integer iconId;
        private Integer isLighted;
        private Integer medalColor;
        private Integer medalColorBorder;
        private Integer medalColorEnd;
        private Integer medalColorStart;
        private Integer medalLevel;
        private String medalName;
        private Integer score;
        private String special;
        private Integer targetId;

        @Override
        public Object clone() throws CloneNotSupportedException {
            return CloneUtils.deepClone(this);
        }

        public FansMedal() {
        }

        public FansMedal(Integer anchorRoomid, Integer guardLevel, Integer iconId, Integer isLighted,
                         Integer medalColor, Integer medalColorBorder, Integer medalColorEnd, Integer medalColorStart,
                         Integer medalLevel, String medalName, Integer score, String special, Integer targetId) {
            this.anchorRoomid = anchorRoomid;
            this.guardLevel = guardLevel;
            this.iconId = iconId;
            this.isLighted = isLighted;
            this.medalColor = medalColor;
            this.medalColorBorder = medalColorBorder;
            this.medalColorEnd = medalColorEnd;
            this.medalColorStart = medalColorStart;
            this.medalLevel = medalLevel;
            this.medalName = medalName;
            this.score = score;
            this.special = special;
            this.targetId = targetId;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

        public Map<String, Object> getAdditionalProperties() {
            return additionalProperties;
        }

        public Integer getAnchorRoomid() {
            return anchorRoomid;
        }

        public FansMedal setAnchorRoomid(Integer anchorRoomid) {
            this.anchorRoomid = anchorRoomid;
            return this;
        }

        public Integer getGuardLevel() {
            return guardLevel;
        }

        public FansMedal setGuardLevel(Integer guardLevel) {
            this.guardLevel = guardLevel;
            return this;
        }

        public Integer getIconId() {
            return iconId;
        }

        public FansMedal setIconId(Integer iconId) {
            this.iconId = iconId;
            return this;
        }

        public Integer getIsLighted() {
            return isLighted;
        }

        public FansMedal setIsLighted(Integer isLighted) {
            this.isLighted = isLighted;
            return this;
        }

        public Integer getMedalColor() {
            return medalColor;
        }

        public FansMedal setMedalColor(Integer medalColor) {
            this.medalColor = medalColor;
            return this;
        }

        public Integer getMedalColorBorder() {
            return medalColorBorder;
        }

        public FansMedal setMedalColorBorder(Integer medalColorBorder) {
            this.medalColorBorder = medalColorBorder;
            return this;
        }

        public Integer getMedalColorEnd() {
            return medalColorEnd;
        }

        public FansMedal setMedalColorEnd(Integer medalColorEnd) {
            this.medalColorEnd = medalColorEnd;
            return this;
        }

        public Integer getMedalColorStart() {
            return medalColorStart;
        }

        public FansMedal setMedalColorStart(Integer medalColorStart) {
            this.medalColorStart = medalColorStart;
            return this;
        }

        public Integer getMedalLevel() {
            return medalLevel;
        }

        public FansMedal setMedalLevel(Integer medalLevel) {
            this.medalLevel = medalLevel;
            return this;
        }

        public String getMedalName() {
            return medalName;
        }

        public FansMedal setMedalName(String medalName) {
            this.medalName = medalName;
            return this;
        }

        public Integer getScore() {
            return score;
        }

        public FansMedal setScore(Integer score) {
            this.score = score;
            return this;
        }

        public String getSpecial() {
            return special;
        }

        public FansMedal setSpecial(String special) {
            this.special = special;
            return this;
        }

        public Integer getTargetId() {
            return targetId;
        }

        public FansMedal setTargetId(Integer targetId) {
            this.targetId = targetId;
            return this;
        }
    }

    public static class Contribution implements Serializable, Cloneable {
        private static final long serialVersionUID = -6524654642687L;
        private final Map<String, Object> additionalProperties = new HashMap<>();
        private Integer grade;

        @Override
        public Object clone() throws CloneNotSupportedException {
            return CloneUtils.deepClone(this);
        }

        public Contribution(Integer grade) {
            this.grade = grade;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Contribution{");
            sb.append("additionalProperties=").append(additionalProperties);
            sb.append(", grade=").append(grade);
            sb.append('}');
            return sb.toString();
        }

        public Map<String, Object> getAdditionalProperties() {
            return additionalProperties;
        }

        public Integer getGrade() {
            return grade;
        }

        public Contribution setGrade(Integer grade) {
            this.grade = grade;
            return this;
        }
    }


}
