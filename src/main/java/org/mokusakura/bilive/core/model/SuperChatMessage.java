package org.mokusakura.bilive.core.model;

import com.alibaba.fastjson.JSONObject;
import org.mokusakura.bilive.core.util.CloneUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MokuSakura
 */
public class SuperChatMessage extends GenericBilibiliMessage implements Serializable, Cloneable {

    private final static long serialVersionUID = 8275693861288435948L;

    private final Map<String, Object> additionalProperties = new HashMap<>();
    private String backgroundBottomColor;
    private String backgroundColor;
    private String backgroundColorEnd;
    private String backgroundColorStart;
    private String backgroundIcon;
    private String backgroundImage;
    private String backgroundPriceColor;
    private Double colorPoint;
    private Long dmscore;
    private Long endTime;
    private Gift gift;
    private Long id;
    private Long isRanked;
    private String isSendAudit;
    private MedalInfo medalInfo;
    private String message;
    private String messageFontColor;
    private String messageTrans;
    private Long price;
    private Long rate;
    private Long startTime;
    private Long time;
    private String token;
    private Long transMark;
    private Long ts;
    private Long uid;
    private UserInfo userInfo;

    public SuperChatMessage() {
        super(MessageType.SUPER_CHAT);
    }

    public SuperChatMessage(String messageType) {
        super(messageType);
    }

    public SuperChatMessage(String messageType, Long roomId, Long timestamp, String rawMessage,
                            String backgroundBottomColor, String backgroundColor, String backgroundColorEnd,
                            String backgroundColorStart, String backgroundIcon, String backgroundImage,
                            String backgroundPriceColor, Double colorPoint, Long dmscore, Long endTime,
                            Gift gift, Long id, Long isRanked, String isSendAudit,
                            MedalInfo medalInfo, String message, String messageFontColor, String messageTrans,
                            Long price, Long rate, Long startTime, Long time, String token, Long transMark,
                            Long ts, Long uid, UserInfo userInfo) {
        super(messageType, roomId, timestamp, rawMessage);
        this.backgroundBottomColor = backgroundBottomColor;
        this.backgroundColor = backgroundColor;
        this.backgroundColorEnd = backgroundColorEnd;
        this.backgroundColorStart = backgroundColorStart;
        this.backgroundIcon = backgroundIcon;
        this.backgroundImage = backgroundImage;
        this.backgroundPriceColor = backgroundPriceColor;
        this.colorPoint = colorPoint;
        this.dmscore = dmscore;
        this.endTime = endTime;
        this.gift = gift;
        this.id = id;
        this.isRanked = isRanked;
        this.isSendAudit = isSendAudit;
        this.medalInfo = medalInfo;
        this.message = message;
        this.messageFontColor = messageFontColor;
        this.messageTrans = messageTrans;
        this.price = price;
        this.rate = rate;
        this.startTime = startTime;
        this.time = time;
        this.token = token;
        this.transMark = transMark;
        this.ts = ts;
        this.uid = uid;
        this.userInfo = userInfo;
    }

    public static SuperChatMessage createFromJson(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONObject dataObject = jsonObject.getJSONObject("data");
        if (dataObject != null) {
            jsonObject = dataObject;
        }
        SuperChatMessage res = jsonObject.toJavaObject(SuperChatMessage.class);
        res.setRawMessage(json);
        res.setMessageType(MessageType.SUPER_CHAT);
        return res;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return CloneUtils.deepClone(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SuperChatMessage{");
        sb.append("additionalProperties=").append(additionalProperties);
        sb.append(", backgroundBottomColor='").append(backgroundBottomColor).append('\'');
        sb.append(", backgroundColor='").append(backgroundColor).append('\'');
        sb.append(", backgroundColorEnd='").append(backgroundColorEnd).append('\'');
        sb.append(", backgroundColorStart='").append(backgroundColorStart).append('\'');
        sb.append(", backgroundIcon='").append(backgroundIcon).append('\'');
        sb.append(", backgroundImage='").append(backgroundImage).append('\'');
        sb.append(", backgroundPriceColor='").append(backgroundPriceColor).append('\'');
        sb.append(", colorPoint=").append(colorPoint);
        sb.append(", dmscore=").append(dmscore);
        sb.append(", endTime=").append(endTime);
        sb.append(", gift=").append(gift);
        sb.append(", id=").append(id);
        sb.append(", isRanked=").append(isRanked);
        sb.append(", isSendAudit='").append(isSendAudit).append('\'');
        sb.append(", medalInfo=").append(medalInfo);
        sb.append(", message='").append(message).append('\'');
        sb.append(", messageFontColor='").append(messageFontColor).append('\'');
        sb.append(", messageTrans='").append(messageTrans).append('\'');
        sb.append(", price=").append(price);
        sb.append(", rate=").append(rate);
        sb.append(", startTime=").append(startTime);
        sb.append(", time=").append(time);
        sb.append(", token='").append(token).append('\'');
        sb.append(", transMark=").append(transMark);
        sb.append(", ts=").append(ts);
        sb.append(", uid=").append(uid);
        sb.append(", userInfo=").append(userInfo);
        sb.append('}');
        return sb.toString();
    }

    public void addAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public String getBackgroundBottomColor() {
        return backgroundBottomColor;
    }

    public SuperChatMessage setBackgroundBottomColor(String backgroundBottomColor) {
        this.backgroundBottomColor = backgroundBottomColor;
        return this;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public SuperChatMessage setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public String getBackgroundColorEnd() {
        return backgroundColorEnd;
    }

    public SuperChatMessage setBackgroundColorEnd(String backgroundColorEnd) {
        this.backgroundColorEnd = backgroundColorEnd;
        return this;
    }

    public String getBackgroundColorStart() {
        return backgroundColorStart;
    }

    public SuperChatMessage setBackgroundColorStart(String backgroundColorStart) {
        this.backgroundColorStart = backgroundColorStart;
        return this;
    }

    public String getBackgroundIcon() {
        return backgroundIcon;
    }

    public SuperChatMessage setBackgroundIcon(String backgroundIcon) {
        this.backgroundIcon = backgroundIcon;
        return this;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public SuperChatMessage setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
        return this;
    }

    public String getBackgroundPriceColor() {
        return backgroundPriceColor;
    }

    public SuperChatMessage setBackgroundPriceColor(String backgroundPriceColor) {
        this.backgroundPriceColor = backgroundPriceColor;
        return this;
    }

    public Double getColorPoint() {
        return colorPoint;
    }

    public SuperChatMessage setColorPoint(Double colorPoint) {
        this.colorPoint = colorPoint;
        return this;
    }

    public Long getDmscore() {
        return dmscore;
    }

    public SuperChatMessage setDmscore(Long dmscore) {
        this.dmscore = dmscore;
        return this;
    }

    public Long getEndTime() {
        return endTime;
    }

    public SuperChatMessage setEndTime(Long endTime) {
        this.endTime = endTime;
        return this;
    }

    public Gift getGift() {
        return gift;
    }

    public SuperChatMessage setGift(Gift gift) {
        this.gift = gift;
        return this;
    }

    public Long getId() {
        return id;
    }

    public SuperChatMessage setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getIsRanked() {
        return isRanked;
    }

    public SuperChatMessage setIsRanked(Long isRanked) {
        this.isRanked = isRanked;
        return this;
    }

    public String getIsSendAudit() {
        return isSendAudit;
    }

    public SuperChatMessage setIsSendAudit(String isSendAudit) {
        this.isSendAudit = isSendAudit;
        return this;
    }

    public MedalInfo getMedalInfo() {
        return medalInfo;
    }

    public SuperChatMessage setMedalInfo(MedalInfo medalInfo) {
        this.medalInfo = medalInfo;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public SuperChatMessage setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getMessageFontColor() {
        return messageFontColor;
    }

    public SuperChatMessage setMessageFontColor(String messageFontColor) {
        this.messageFontColor = messageFontColor;
        return this;
    }

    public String getMessageTrans() {
        return messageTrans;
    }

    public SuperChatMessage setMessageTrans(String messageTrans) {
        this.messageTrans = messageTrans;
        return this;
    }

    public Long getPrice() {
        return price;
    }

    public SuperChatMessage setPrice(Long price) {
        this.price = price;
        return this;
    }

    public Long getRate() {
        return rate;
    }

    public SuperChatMessage setRate(Long rate) {
        this.rate = rate;
        return this;
    }

    public Long getStartTime() {
        return startTime;
    }

    public SuperChatMessage setStartTime(Long startTime) {
        this.startTime = startTime;
        return this;
    }

    public Long getTime() {
        return time;
    }

    public SuperChatMessage setTime(Long time) {
        this.time = time;
        return this;
    }

    public String getToken() {
        return token;
    }

    public SuperChatMessage setToken(String token) {
        this.token = token;
        return this;
    }

    public Long getTransMark() {
        return transMark;
    }

    public SuperChatMessage setTransMark(Long transMark) {
        this.transMark = transMark;
        return this;
    }

    public Long getTs() {
        return ts;
    }

    public SuperChatMessage setTs(Long ts) {
        this.ts = ts;
        return this;
    }

    public Long getUid() {
        return uid;
    }

    public SuperChatMessage setUid(Long uid) {
        this.uid = uid;
        return this;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public SuperChatMessage setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        return this;
    }

    public static class Gift implements Serializable, Cloneable {
        private final static long serialVersionUID = 1540792054227797718L;
        private final Map<String, Object> additionalProperties = new HashMap<>();
        private Long giftId;
        private String giftName;
        private Long num;


        @Override
        public Object clone() throws CloneNotSupportedException {
            return CloneUtils.deepClone(this);
        }

        public Gift() {
        }

        public Gift(Long giftId, String giftName, Long num) {
            this.giftId = giftId;
            this.giftName = giftName;
            this.num = num;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Gift{");
            sb.append("additionalProperties=").append(additionalProperties);
            sb.append(", giftId=").append(giftId);
            sb.append(", giftName='").append(giftName).append('\'');
            sb.append(", num=").append(num);
            sb.append('}');
            return sb.toString();
        }

        public void addAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

        public Map<String, Object> getAdditionalProperties() {
            return additionalProperties;
        }

        public Long getGiftId() {
            return giftId;
        }

        public Gift setGiftId(Long giftId) {
            this.giftId = giftId;
            return this;
        }

        public String getGiftName() {
            return giftName;
        }

        public Gift setGiftName(String giftName) {
            this.giftName = giftName;
            return this;
        }

        public Long getNum() {
            return num;
        }

        public Gift setNum(Long num) {
            this.num = num;
            return this;
        }
    }

    public static class MedalInfo implements Serializable, Cloneable {
        private final static long serialVersionUID = 3204661273937089018L;
        private final Map<String, Object> additionalProperties = new HashMap<>();
        private Long anchorRoomid;
        private String anchorUname;
        private Long guardLevel;
        private Long iconId;
        private Long isLighted;
        private String medalColor;
        private Long medalColorBorder;
        private Long medalColorEnd;
        private Long medalColorStart;
        private Long medalLevel;
        private String medalName;
        private String special;
        private Long targetId;


        @Override
        public Object clone() throws CloneNotSupportedException {
            return CloneUtils.deepClone(this);
        }

        public MedalInfo() {
        }

        public MedalInfo(Long anchorRoomid, String anchorUname, Long guardLevel, Long iconId, Long isLighted,
                         String medalColor, Long medalColorBorder, Long medalColorEnd, Long medalColorStart,
                         Long medalLevel, String medalName, String special, Long targetId) {
            this.anchorRoomid = anchorRoomid;
            this.anchorUname = anchorUname;
            this.guardLevel = guardLevel;
            this.iconId = iconId;
            this.isLighted = isLighted;
            this.medalColor = medalColor;
            this.medalColorBorder = medalColorBorder;
            this.medalColorEnd = medalColorEnd;
            this.medalColorStart = medalColorStart;
            this.medalLevel = medalLevel;
            this.medalName = medalName;
            this.special = special;
            this.targetId = targetId;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("MedalInfo{");
            sb.append("additionalProperties=").append(additionalProperties);
            sb.append(", anchorRoomid=").append(anchorRoomid);
            sb.append(", anchorUname='").append(anchorUname).append('\'');
            sb.append(", guardLevel=").append(guardLevel);
            sb.append(", iconId=").append(iconId);
            sb.append(", isLighted=").append(isLighted);
            sb.append(", medalColor='").append(medalColor).append('\'');
            sb.append(", medalColorBorder=").append(medalColorBorder);
            sb.append(", medalColorEnd=").append(medalColorEnd);
            sb.append(", medalColorStart=").append(medalColorStart);
            sb.append(", medalLevel=").append(medalLevel);
            sb.append(", medalName='").append(medalName).append('\'');
            sb.append(", special='").append(special).append('\'');
            sb.append(", targetId=").append(targetId);
            sb.append('}');
            return sb.toString();
        }

        public Map<String, Object> getAdditionalProperties() {
            return additionalProperties;
        }

        public Long getAnchorRoomid() {
            return anchorRoomid;
        }

        public MedalInfo setAnchorRoomid(Long anchorRoomid) {
            this.anchorRoomid = anchorRoomid;
            return this;
        }

        public String getAnchorUname() {
            return anchorUname;
        }

        public MedalInfo setAnchorUname(String anchorUname) {
            this.anchorUname = anchorUname;
            return this;
        }

        public Long getGuardLevel() {
            return guardLevel;
        }

        public MedalInfo setGuardLevel(Long guardLevel) {
            this.guardLevel = guardLevel;
            return this;
        }

        public Long getIconId() {
            return iconId;
        }

        public MedalInfo setIconId(Long iconId) {
            this.iconId = iconId;
            return this;
        }

        public Long getIsLighted() {
            return isLighted;
        }

        public MedalInfo setIsLighted(Long isLighted) {
            this.isLighted = isLighted;
            return this;
        }

        public String getMedalColor() {
            return medalColor;
        }

        public MedalInfo setMedalColor(String medalColor) {
            this.medalColor = medalColor;
            return this;
        }

        public Long getMedalColorBorder() {
            return medalColorBorder;
        }

        public MedalInfo setMedalColorBorder(Long medalColorBorder) {
            this.medalColorBorder = medalColorBorder;
            return this;
        }

        public Long getMedalColorEnd() {
            return medalColorEnd;
        }

        public MedalInfo setMedalColorEnd(Long medalColorEnd) {
            this.medalColorEnd = medalColorEnd;
            return this;
        }

        public Long getMedalColorStart() {
            return medalColorStart;
        }

        public MedalInfo setMedalColorStart(Long medalColorStart) {
            this.medalColorStart = medalColorStart;
            return this;
        }

        public Long getMedalLevel() {
            return medalLevel;
        }

        public MedalInfo setMedalLevel(Long medalLevel) {
            this.medalLevel = medalLevel;
            return this;
        }

        public String getMedalName() {
            return medalName;
        }

        public MedalInfo setMedalName(String medalName) {
            this.medalName = medalName;
            return this;
        }

        public String getSpecial() {
            return special;
        }

        public MedalInfo setSpecial(String special) {
            this.special = special;
            return this;
        }

        public Long getTargetId() {
            return targetId;
        }

        public MedalInfo setTargetId(Long targetId) {
            this.targetId = targetId;
            return this;
        }

        public void addAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }
    }

    public static class UserInfo implements Serializable, Cloneable {
        private final static long serialVersionUID = 5294051665651419042L;
        private final Map<String, Object> additionalProperties = new HashMap<>();
        private String face;
        private String faceFrame;
        private Long guardLevel;
        private Long isMainVip;
        private Long isSvip;
        private Long isVip;
        private String levelColor;
        private Long manager;
        private String nameColor;
        private String title;
        private String uname;
        private Long userLevel;


        public UserInfo() {
        }

        public UserInfo(String face, String faceFrame, Long guardLevel, Long isMainVip, Long isSvip, Long isVip,
                        String levelColor, Long manager, String nameColor, String title, String uname, Long userLevel) {
            this.face = face;
            this.faceFrame = faceFrame;
            this.guardLevel = guardLevel;
            this.isMainVip = isMainVip;
            this.isSvip = isSvip;
            this.isVip = isVip;
            this.levelColor = levelColor;
            this.manager = manager;
            this.nameColor = nameColor;
            this.title = title;
            this.uname = uname;
            this.userLevel = userLevel;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("UserInfo{");
            sb.append("additionalProperties=").append(additionalProperties);
            sb.append(", face='").append(face).append('\'');
            sb.append(", faceFrame='").append(faceFrame).append('\'');
            sb.append(", guardLevel=").append(guardLevel);
            sb.append(", isMainVip=").append(isMainVip);
            sb.append(", isSvip=").append(isSvip);
            sb.append(", isVip=").append(isVip);
            sb.append(", levelColor='").append(levelColor).append('\'');
            sb.append(", manager=").append(manager);
            sb.append(", nameColor='").append(nameColor).append('\'');
            sb.append(", title='").append(title).append('\'');
            sb.append(", uname='").append(uname).append('\'');
            sb.append(", userLevel=").append(userLevel);
            sb.append('}');
            return sb.toString();
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return CloneUtils.deepClone(this);
        }

        public Map<String, Object> getAdditionalProperties() {
            return additionalProperties;
        }

        public String getFace() {
            return face;
        }

        public UserInfo setFace(String face) {
            this.face = face;
            return this;
        }

        public String getFaceFrame() {
            return faceFrame;
        }

        public UserInfo setFaceFrame(String faceFrame) {
            this.faceFrame = faceFrame;
            return this;
        }

        public Long getGuardLevel() {
            return guardLevel;
        }

        public UserInfo setGuardLevel(Long guardLevel) {
            this.guardLevel = guardLevel;
            return this;
        }

        public Long getIsMainVip() {
            return isMainVip;
        }

        public UserInfo setIsMainVip(Long isMainVip) {
            this.isMainVip = isMainVip;
            return this;
        }

        public Long getIsSvip() {
            return isSvip;
        }

        public UserInfo setIsSvip(Long isSvip) {
            this.isSvip = isSvip;
            return this;
        }

        public Long getIsVip() {
            return isVip;
        }

        public UserInfo setIsVip(Long isVip) {
            this.isVip = isVip;
            return this;
        }

        public String getLevelColor() {
            return levelColor;
        }

        public UserInfo setLevelColor(String levelColor) {
            this.levelColor = levelColor;
            return this;
        }

        public Long getManager() {
            return manager;
        }

        public UserInfo setManager(Long manager) {
            this.manager = manager;
            return this;
        }

        public String getNameColor() {
            return nameColor;
        }

        public UserInfo setNameColor(String nameColor) {
            this.nameColor = nameColor;
            return this;
        }

        public String getTitle() {
            return title;
        }

        public UserInfo setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getUname() {
            return uname;
        }

        public UserInfo setUname(String uname) {
            this.uname = uname;
            return this;
        }

        public Long getUserLevel() {
            return userLevel;
        }

        public UserInfo setUserLevel(Long userLevel) {
            this.userLevel = userLevel;
            return this;
        }

        public void addAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }
    }
}

