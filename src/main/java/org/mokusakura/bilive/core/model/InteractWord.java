package org.mokusakura.bilive.core.model;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MokuSakura
 */
public class InteractWord extends AbstractDanmaku {

    @JSONField(unwrapped = true)
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

    public static InteractWord createInteractWord(String json) {
        InteractWord res = JSONObject.parseObject(json).getObject("data", InteractWord.class);
        res.setMessageType(MessageType.InteractWord);
        return res;
    }

    @Override
    @JSONField(name = "uname")
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    @JSONField(name = "uname")
    public InteractWord setUsername(String username) {
        super.setUsername(username);
        return this;
    }

    @Override
    @JSONField(name = "timestamp")
    public Long getTimestamp() {
        return super.getTimestamp();
    }

    @Override
    @JSONField(name = "timestamp")
    public InteractWord setTimestamp(Long timestamp) {
        super.setTimestamp(timestamp);
        return this;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Contribution getContribution() {
        return contribution;
    }

    public InteractWord setContribution(Contribution contribution) {
        this.contribution = contribution;
        return this;
    }

    public Integer getDmscore() {
        return dmscore;
    }

    public InteractWord setDmscore(Integer dmscore) {
        this.dmscore = dmscore;
        return this;
    }

    public FansMedal getFansMedal() {
        return fansMedal;
    }

    public InteractWord setFansMedal(FansMedal fansMedal) {
        this.fansMedal = fansMedal;
        return this;
    }

    public List<Integer> getIdentities() {
        return identities;
    }

    public InteractWord setIdentities(List<Integer> identities) {
        this.identities = identities;
        return this;
    }

    public Integer getIsSpread() {
        return isSpread;
    }

    public InteractWord setIsSpread(Integer isSpread) {
        this.isSpread = isSpread;
        return this;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public InteractWord setMsgType(Integer msgType) {
        this.msgType = msgType;
        return this;
    }

    public Integer getRoomid() {
        return roomid;
    }

    public InteractWord setRoomid(Integer roomid) {
        this.roomid = roomid;
        return this;
    }

    public Long getScore() {
        return score;
    }

    public InteractWord setScore(Long score) {
        this.score = score;
        return this;
    }

    public String getSpreadDesc() {
        return spreadDesc;
    }

    public InteractWord setSpreadDesc(String spreadDesc) {
        this.spreadDesc = spreadDesc;
        return this;
    }

    public String getSpreadInfo() {
        return spreadInfo;
    }

    public InteractWord setSpreadInfo(String spreadInfo) {
        this.spreadInfo = spreadInfo;
        return this;
    }

    public Integer getTailIcon() {
        return tailIcon;
    }

    public InteractWord setTailIcon(Integer tailIcon) {
        this.tailIcon = tailIcon;
        return this;
    }

    public Long getTriggerTime() {
        return triggerTime;
    }

    public InteractWord setTriggerTime(Long triggerTime) {
        this.triggerTime = triggerTime;
        return this;
    }

    public String getUnameColor() {
        return unameColor;
    }

    public InteractWord setUnameColor(String unameColor) {
        this.unameColor = unameColor;
        return this;
    }

    public static class FansMedal {

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

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
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

    public static class Contribution {

        private final Map<String, Object> additionalProperties = new HashMap<>();
        private Integer grade;

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
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
