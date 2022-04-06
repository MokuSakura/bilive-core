package org.mokusakura.bilive.core.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import org.mokusakura.bilive.core.util.CloneUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SendGiftMessage extends GenericBilibiliMessage implements Serializable, Cloneable {
    private static final Long serializationUID = 918374598163287456L;

    private final Map<String, Object> additionalProperties = new HashMap<>();
    private String action;
    private String batchComboId;
    private BatchComboSend batchComboSend;
    private String beatId;
    private String bizSource;
    private String blindGift;
    private String coinType;
    private Long broadcastId;
    private ComboSend comboSend;
    private Long comboResourcesId;
    private Long comboStayTime;
    private Long comboTotalCoin;
    private Long critProb;
    private Long demarcation;
    private Long dmscore;
    private Long draw;
    private Long effect;
    private String face;
    private Long effectBlock;
    private String giftName;
    private Long giftId;
    private Long giftType;
    private Long gold;
    private Long guardLevel;
    private Boolean isFirst;
    private Long isSpecialBatch;
    private MedalInfo medalInfo;
    private String nameColor;
    private Long magnification;
    private String originalGiftName;
    private Long num;
    private Long price;
    private Long rcost;
    private String rnd;
    private String sendMaster;
    private Long remain;
    private Long silver;
    @JSONField(name = "super")
    private Long super_;
    private Long superBatchGiftNum;
    private Long superGiftNum;
    private String tagImage;
    private String tid;
    private String topList;
    private Long svgaBlock;
    private String uname;
    private Long totalCoin;
    private Long uid;

    public SendGiftMessage() {
        super(MessageType.GIFT_SEND);
    }

    public SendGiftMessage(String messageType) {
        super(messageType);
    }

    public SendGiftMessage(String messageType, Long roomId, Long timestamp, String rawMessage, String action,
                           String batchComboId,
                           BatchComboSend batchComboSend, String beatId, String bizSource, String blindGift,
                           String coinType, Long broadcastId,
                           ComboSend comboSend, Long comboResourcesId, Long comboStayTime, Long comboTotalCoin,
                           Long critProb, Long demarcation, Long dmscore, Long draw, Long effect, String face,
                           Long effectBlock, String giftName, Long giftId, Long giftType, Long gold,
                           Long guardLevel, Boolean isFirst, Long isSpecialBatch,
                           MedalInfo medalInfo, String nameColor, Long magnification, String originalGiftName,
                           Long num, Long price, Long rcost, String rnd, String sendMaster, Long remain,
                           Long silver, Long super_, Long superBatchGiftNum, Long superGiftNum, String tagImage,
                           String tid, String topList, Long svgaBlock, String uname, Long totalCoin, Long uid) {
        super(messageType, roomId, timestamp, rawMessage);
        this.action = action;
        this.batchComboId = batchComboId;
        this.batchComboSend = batchComboSend;
        this.beatId = beatId;
        this.bizSource = bizSource;
        this.blindGift = blindGift;
        this.coinType = coinType;
        this.broadcastId = broadcastId;
        this.comboSend = comboSend;
        this.comboResourcesId = comboResourcesId;
        this.comboStayTime = comboStayTime;
        this.comboTotalCoin = comboTotalCoin;
        this.critProb = critProb;
        this.demarcation = demarcation;
        this.dmscore = dmscore;
        this.draw = draw;
        this.effect = effect;
        this.face = face;
        this.effectBlock = effectBlock;
        this.giftName = giftName;
        this.giftId = giftId;
        this.giftType = giftType;
        this.gold = gold;
        this.guardLevel = guardLevel;
        this.isFirst = isFirst;
        this.isSpecialBatch = isSpecialBatch;
        this.medalInfo = medalInfo;
        this.nameColor = nameColor;
        this.magnification = magnification;
        this.originalGiftName = originalGiftName;
        this.num = num;
        this.price = price;
        this.rcost = rcost;
        this.rnd = rnd;
        this.sendMaster = sendMaster;
        this.remain = remain;
        this.silver = silver;
        this.super_ = super_;
        this.superBatchGiftNum = superBatchGiftNum;
        this.superGiftNum = superGiftNum;
        this.tagImage = tagImage;
        this.tid = tid;
        this.topList = topList;
        this.svgaBlock = svgaBlock;
        this.uname = uname;
        this.totalCoin = totalCoin;
        this.uid = uid;
    }

    public static SendGiftMessage createFromJson(String jsonStr) {
        JSONObject obj = JSON.parseObject(jsonStr);
        JSONObject dataObject = obj.getJSONObject("data");
        if (dataObject != null) {
            obj = dataObject;
        }
        SendGiftMessage res = obj.toJavaObject(SendGiftMessage.class);
        res.setMessageType(MessageType.GIFT_SEND)
                .setRawMessage(jsonStr);
        return res;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return CloneUtils.deepClone(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SendGiftMessage{");
        sb.append("action='").append(action).append('\'');
        sb.append(", batchComboId='").append(batchComboId).append('\'');
        sb.append(", batchComboSend=").append(batchComboSend);
        sb.append(", beatId='").append(beatId).append('\'');
        sb.append(", bizSource='").append(bizSource).append('\'');
        sb.append(", blindGift='").append(blindGift).append('\'');
        sb.append(", additionalProperties=").append(additionalProperties);
        sb.append(", coinType='").append(coinType).append('\'');
        sb.append(", broadcastId=").append(broadcastId);
        sb.append(", comboSend=").append(comboSend);
        sb.append(", comboResourcesId=").append(comboResourcesId);
        sb.append(", comboStayTime=").append(comboStayTime);
        sb.append(", comboTotalCoin=").append(comboTotalCoin);
        sb.append(", critProb=").append(critProb);
        sb.append(", demarcation=").append(demarcation);
        sb.append(", dmscore=").append(dmscore);
        sb.append(", draw=").append(draw);
        sb.append(", effect=").append(effect);
        sb.append(", face='").append(face).append('\'');
        sb.append(", effectBlock=").append(effectBlock);
        sb.append(", giftName='").append(giftName).append('\'');
        sb.append(", giftId=").append(giftId);
        sb.append(", giftType=").append(giftType);
        sb.append(", gold=").append(gold);
        sb.append(", guardLevel=").append(guardLevel);
        sb.append(", isFirst=").append(isFirst);
        sb.append(", isSpecialBatch=").append(isSpecialBatch);
        sb.append(", medalInfo=").append(medalInfo);
        sb.append(", nameColor='").append(nameColor).append('\'');
        sb.append(", magnification=").append(magnification);
        sb.append(", originalGiftName='").append(originalGiftName).append('\'');
        sb.append(", num=").append(num);
        sb.append(", price=").append(price);
        sb.append(", rcost=").append(rcost);
        sb.append(", rnd='").append(rnd).append('\'');
        sb.append(", sendMaster='").append(sendMaster).append('\'');
        sb.append(", remain=").append(remain);
        sb.append(", silver=").append(silver);
        sb.append(", super_=").append(super_);
        sb.append(", superBatchGiftNum=").append(superBatchGiftNum);
        sb.append(", superGiftNum=").append(superGiftNum);
        sb.append(", tagImage='").append(tagImage).append('\'');
        sb.append(", tid='").append(tid).append('\'');
        sb.append(", topList='").append(topList).append('\'');
        sb.append(", svgaBlock=").append(svgaBlock);
        sb.append(", uname='").append(uname).append('\'');
        sb.append(", totalCoin=").append(totalCoin);
        sb.append(", uid=").append(uid);
        sb.append('}');
        return sb.toString();
    }

    public void setAdditionalProperties(String key, Object object) {
        additionalProperties.put(key, object);
    }

    public String getAction() {
        return action;
    }

    public SendGiftMessage setAction(String action) {
        this.action = action;
        return this;
    }

    public String getBatchComboId() {
        return batchComboId;
    }

    public SendGiftMessage setBatchComboId(String batchComboId) {
        this.batchComboId = batchComboId;
        return this;
    }

    public BatchComboSend getBatchComboSend() {
        return batchComboSend;
    }

    public SendGiftMessage setBatchComboSend(
            BatchComboSend batchComboSend) {
        this.batchComboSend = batchComboSend;
        return this;
    }

    public String getBeatId() {
        return beatId;
    }

    public SendGiftMessage setBeatId(String beatId) {
        this.beatId = beatId;
        return this;
    }

    public String getBizSource() {
        return bizSource;
    }

    public SendGiftMessage setBizSource(String bizSource) {
        this.bizSource = bizSource;
        return this;
    }

    public String getBlindGift() {
        return blindGift;
    }

    public SendGiftMessage setBlindGift(String blindGift) {
        this.blindGift = blindGift;
        return this;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public String getCoinType() {
        return coinType;
    }

    public SendGiftMessage setCoinType(String coinType) {
        this.coinType = coinType;
        return this;
    }

    public Long getBroadcastId() {
        return broadcastId;
    }

    public SendGiftMessage setBroadcastId(Long broadcastId) {
        this.broadcastId = broadcastId;
        return this;
    }

    public ComboSend getComboSend() {
        return comboSend;
    }

    public SendGiftMessage setComboSend(ComboSend comboSend) {
        this.comboSend = comboSend;
        return this;
    }

    public Long getComboResourcesId() {
        return comboResourcesId;
    }

    public SendGiftMessage setComboResourcesId(Long comboResourcesId) {
        this.comboResourcesId = comboResourcesId;
        return this;
    }

    public Long getComboStayTime() {
        return comboStayTime;
    }

    public SendGiftMessage setComboStayTime(Long comboStayTime) {
        this.comboStayTime = comboStayTime;
        return this;
    }

    public Long getComboTotalCoin() {
        return comboTotalCoin;
    }

    public SendGiftMessage setComboTotalCoin(Long comboTotalCoin) {
        this.comboTotalCoin = comboTotalCoin;
        return this;
    }

    public Long getCritProb() {
        return critProb;
    }

    public SendGiftMessage setCritProb(Long critProb) {
        this.critProb = critProb;
        return this;
    }

    public Long getDemarcation() {
        return demarcation;
    }

    public SendGiftMessage setDemarcation(Long demarcation) {
        this.demarcation = demarcation;
        return this;
    }

    public Long getDmscore() {
        return dmscore;
    }

    public SendGiftMessage setDmscore(Long dmscore) {
        this.dmscore = dmscore;
        return this;
    }

    public Long getDraw() {
        return draw;
    }

    public SendGiftMessage setDraw(Long draw) {
        this.draw = draw;
        return this;
    }

    public Long getEffect() {
        return effect;
    }

    public SendGiftMessage setEffect(Long effect) {
        this.effect = effect;
        return this;
    }

    public String getFace() {
        return face;
    }

    public SendGiftMessage setFace(String face) {
        this.face = face;
        return this;
    }

    public Long getEffectBlock() {
        return effectBlock;
    }

    public SendGiftMessage setEffectBlock(Long effectBlock) {
        this.effectBlock = effectBlock;
        return this;
    }

    public String getGiftName() {
        return giftName;
    }

    public SendGiftMessage setGiftName(String giftName) {
        this.giftName = giftName;
        return this;
    }

    public Long getGiftId() {
        return giftId;
    }

    public SendGiftMessage setGiftId(Long giftId) {
        this.giftId = giftId;
        return this;
    }

    public Long getGiftType() {
        return giftType;
    }

    public SendGiftMessage setGiftType(Long giftType) {
        this.giftType = giftType;
        return this;
    }

    public Long getGold() {
        return gold;
    }

    public SendGiftMessage setGold(Long gold) {
        this.gold = gold;
        return this;
    }

    public Long getGuardLevel() {
        return guardLevel;
    }

    public SendGiftMessage setGuardLevel(Long guardLevel) {
        this.guardLevel = guardLevel;
        return this;
    }

    public Boolean getFirst() {
        return isFirst;
    }

    public SendGiftMessage setFirst(Boolean first) {
        isFirst = first;
        return this;
    }

    public Long getIsSpecialBatch() {
        return isSpecialBatch;
    }

    public SendGiftMessage setIsSpecialBatch(Long isSpecialBatch) {
        this.isSpecialBatch = isSpecialBatch;
        return this;
    }

    public MedalInfo getMedalInfo() {
        return medalInfo;
    }

    public SendGiftMessage setMedalInfo(MedalInfo medalInfo) {
        this.medalInfo = medalInfo;
        return this;
    }

    public String getNameColor() {
        return nameColor;
    }

    public SendGiftMessage setNameColor(String nameColor) {
        this.nameColor = nameColor;
        return this;
    }

    public Long getMagnification() {
        return magnification;
    }

    public SendGiftMessage setMagnification(Long magnification) {
        this.magnification = magnification;
        return this;
    }

    public String getOriginalGiftName() {
        return originalGiftName;
    }

    public SendGiftMessage setOriginalGiftName(String originalGiftName) {
        this.originalGiftName = originalGiftName;
        return this;
    }

    public Long getNum() {
        return num;
    }

    public SendGiftMessage setNum(Long num) {
        this.num = num;
        return this;
    }

    public Long getPrice() {
        return price;
    }

    public SendGiftMessage setPrice(Long price) {
        this.price = price;
        return this;
    }

    public Long getRcost() {
        return rcost;
    }

    public SendGiftMessage setRcost(Long rcost) {
        this.rcost = rcost;
        return this;
    }

    public String getRnd() {
        return rnd;
    }

    public SendGiftMessage setRnd(String rnd) {
        this.rnd = rnd;
        return this;
    }

    public String getSendMaster() {
        return sendMaster;
    }

    public SendGiftMessage setSendMaster(String sendMaster) {
        this.sendMaster = sendMaster;
        return this;
    }

    public Long getRemain() {
        return remain;
    }

    public SendGiftMessage setRemain(Long remain) {
        this.remain = remain;
        return this;
    }

    public Long getSilver() {
        return silver;
    }

    public SendGiftMessage setSilver(Long silver) {
        this.silver = silver;
        return this;
    }

    public Long getSuper_() {
        return super_;
    }

    public SendGiftMessage setSuper_(Long super_) {
        this.super_ = super_;
        return this;
    }

    public Long getSuperBatchGiftNum() {
        return superBatchGiftNum;
    }

    public SendGiftMessage setSuperBatchGiftNum(Long superBatchGiftNum) {
        this.superBatchGiftNum = superBatchGiftNum;
        return this;
    }

    public Long getSuperGiftNum() {
        return superGiftNum;
    }

    public SendGiftMessage setSuperGiftNum(Long superGiftNum) {
        this.superGiftNum = superGiftNum;
        return this;
    }

    public String getTagImage() {
        return tagImage;
    }

    public SendGiftMessage setTagImage(String tagImage) {
        this.tagImage = tagImage;
        return this;
    }

    public String getTid() {
        return tid;
    }

    public SendGiftMessage setTid(String tid) {
        this.tid = tid;
        return this;
    }

    public String getTopList() {
        return topList;
    }

    public SendGiftMessage setTopList(String topList) {
        this.topList = topList;
        return this;
    }

    public Long getSvgaBlock() {
        return svgaBlock;
    }

    public SendGiftMessage setSvgaBlock(Long svgaBlock) {
        this.svgaBlock = svgaBlock;
        return this;
    }

    public String getUname() {
        return uname;
    }

    public SendGiftMessage setUname(String uname) {
        this.uname = uname;
        return this;
    }

    public Long getTotalCoin() {
        return totalCoin;
    }

    public SendGiftMessage setTotalCoin(Long totalCoin) {
        this.totalCoin = totalCoin;
        return this;
    }

    public Long getUid() {
        return uid;
    }

    public SendGiftMessage setUid(Long uid) {
        this.uid = uid;
        return this;
    }

    public static class BatchComboSend implements Serializable, Cloneable {
        private static final Long serializationUID = -65284685465124L;
        private String action;
        private String batchComboId;
        private final Map<String, Object> additionalProperties = new HashMap<>();
        private String blindGift;
        private Long batchComboNum;
        private String giftName;
        private Long giftId;
        private String sendMaster;
        private Long giftNum;
        private String uname;
        private Long uid;

        @Override
        public Object clone() throws CloneNotSupportedException {
            return CloneUtils.deepClone(this);
        }

        public BatchComboSend() {
        }

        public BatchComboSend(String action, String batchComboId, String blindGift, Long batchComboNum,
                              String giftName, Long giftId, String sendMaster, Long giftNum, String uname, Long uid) {
            this.action = action;
            this.batchComboId = batchComboId;
            this.blindGift = blindGift;
            this.batchComboNum = batchComboNum;
            this.giftName = giftName;
            this.giftId = giftId;
            this.sendMaster = sendMaster;
            this.giftNum = giftNum;
            this.uname = uname;
            this.uid = uid;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("BatchComboSend{");
            sb.append("action='").append(action).append('\'');
            sb.append(", batchComboId='").append(batchComboId).append('\'');
            sb.append(", additionalProperties=").append(additionalProperties);
            sb.append(", blindGift='").append(blindGift).append('\'');
            sb.append(", batchComboNum=").append(batchComboNum);
            sb.append(", giftName='").append(giftName).append('\'');
            sb.append(", giftId=").append(giftId);
            sb.append(", sendMaster='").append(sendMaster).append('\'');
            sb.append(", giftNum=").append(giftNum);
            sb.append(", uname='").append(uname).append('\'');
            sb.append(", uid=").append(uid);
            sb.append('}');
            return sb.toString();
        }

        public String getAction() {
            return action;
        }

        public BatchComboSend setAction(String action) {
            this.action = action;
            return this;
        }

        public String getBatchComboId() {
            return batchComboId;
        }

        public BatchComboSend setBatchComboId(String batchComboId) {
            this.batchComboId = batchComboId;
            return this;
        }

        public Map<String, Object> getAdditionalProperties() {
            return additionalProperties;
        }

        public String getBlindGift() {
            return blindGift;
        }

        public BatchComboSend setBlindGift(String blindGift) {
            this.blindGift = blindGift;
            return this;
        }

        public Long getBatchComboNum() {
            return batchComboNum;
        }

        public BatchComboSend setBatchComboNum(Long batchComboNum) {
            this.batchComboNum = batchComboNum;
            return this;
        }

        public String getGiftName() {
            return giftName;
        }

        public BatchComboSend setGiftName(String giftName) {
            this.giftName = giftName;
            return this;
        }

        public Long getGiftId() {
            return giftId;
        }

        public BatchComboSend setGiftId(Long giftId) {
            this.giftId = giftId;
            return this;
        }

        public String getSendMaster() {
            return sendMaster;
        }

        public BatchComboSend setSendMaster(String sendMaster) {
            this.sendMaster = sendMaster;
            return this;
        }

        public Long getGiftNum() {
            return giftNum;
        }

        public BatchComboSend setGiftNum(Long giftNum) {
            this.giftNum = giftNum;
            return this;
        }

        public String getUname() {
            return uname;
        }

        public BatchComboSend setUname(String uname) {
            this.uname = uname;
            return this;
        }

        public Long getUid() {
            return uid;
        }

        public BatchComboSend setUid(Long uid) {
            this.uid = uid;
            return this;
        }

        public void setAdditionalProperties(String key, Object object) {
            additionalProperties.put(key, object);
        }
    }

    public static class ComboSend implements Serializable, Cloneable {
        private static final Long serializationUID = 65768272654657462L;
        private String action;
        private String comboId;
        private final Map<String, Object> additionalProperties = new HashMap<>();
        private Long comboNum;
        private String giftName;
        private Long giftId;
        private String sendMaster;
        private Long giftNum;
        private String uname;
        private Long uid;

        @Override
        public Object clone() throws CloneNotSupportedException {
            return CloneUtils.deepClone(this);
        }

        public ComboSend() {
        }

        public ComboSend(String action, String comboId, Long comboNum, String giftName, Long giftId,
                         String sendMaster, Long giftNum, String uname, Long uid) {
            this.action = action;
            this.comboId = comboId;
            this.comboNum = comboNum;
            this.giftName = giftName;
            this.giftId = giftId;
            this.sendMaster = sendMaster;
            this.giftNum = giftNum;
            this.uname = uname;
            this.uid = uid;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("ComboSend{");
            sb.append("action='").append(action).append('\'');
            sb.append(", comboId='").append(comboId).append('\'');
            sb.append(", additionalProperties=").append(additionalProperties);
            sb.append(", comboNum=").append(comboNum);
            sb.append(", giftName='").append(giftName).append('\'');
            sb.append(", giftId=").append(giftId);
            sb.append(", sendMaster='").append(sendMaster).append('\'');
            sb.append(", giftNum=").append(giftNum);
            sb.append(", uname='").append(uname).append('\'');
            sb.append(", uid=").append(uid);
            sb.append('}');
            return sb.toString();
        }

        public String getAction() {
            return action;
        }

        public ComboSend setAction(String action) {
            this.action = action;
            return this;
        }

        public String getComboId() {
            return comboId;
        }

        public ComboSend setComboId(String comboId) {
            this.comboId = comboId;
            return this;
        }

        public Map<String, Object> getAdditionalProperties() {
            return additionalProperties;
        }

        public Long getComboNum() {
            return comboNum;
        }

        public ComboSend setComboNum(Long comboNum) {
            this.comboNum = comboNum;
            return this;
        }

        public String getGiftName() {
            return giftName;
        }

        public ComboSend setGiftName(String giftName) {
            this.giftName = giftName;
            return this;
        }

        public Long getGiftId() {
            return giftId;
        }

        public ComboSend setGiftId(Long giftId) {
            this.giftId = giftId;
            return this;
        }

        public String getSendMaster() {
            return sendMaster;
        }

        public ComboSend setSendMaster(String sendMaster) {
            this.sendMaster = sendMaster;
            return this;
        }

        public Long getGiftNum() {
            return giftNum;
        }

        public ComboSend setGiftNum(Long giftNum) {
            this.giftNum = giftNum;
            return this;
        }

        public String getUname() {
            return uname;
        }

        public ComboSend setUname(String uname) {
            this.uname = uname;
            return this;
        }

        public Long getUid() {
            return uid;
        }

        public ComboSend setUid(Long uid) {
            this.uid = uid;
            return this;
        }

        public void setAdditionalProperties(String key, Object object) {
            additionalProperties.put(key, object);
        }

    }

    public static class MedalInfo implements Serializable, Cloneable {
        private static final Long serializationUID = -2658726854658468524L;
        private final Map<String, Object> additionalProperties = new HashMap<>();
        private String anchorUname;
        private Long anchorRoomid;
        private Long guardLevel;
        private Long iconId;
        private Long isLighted;
        private Long medalColor;
        private Long medalColorBorder;
        private Long medalColorEnd;
        private Long medalColorStart;
        private String medalName;
        private String special;
        private Long medalLevel;
        private Long targetId;

        @Override
        public Object clone() throws CloneNotSupportedException {
            return CloneUtils.deepClone(this);
        }

        public MedalInfo() {
        }

        public MedalInfo(String anchorUname, Long anchorRoomid, Long guardLevel, Long iconId, Long isLighted,
                         Long medalColor, Long medalColorBorder, Long medalColorEnd, Long medalColorStart,
                         String medalName, String special, Long medalLevel, Long targetId) {
            this.anchorUname = anchorUname;
            this.anchorRoomid = anchorRoomid;
            this.guardLevel = guardLevel;
            this.iconId = iconId;
            this.isLighted = isLighted;
            this.medalColor = medalColor;
            this.medalColorBorder = medalColorBorder;
            this.medalColorEnd = medalColorEnd;
            this.medalColorStart = medalColorStart;
            this.medalName = medalName;
            this.special = special;
            this.medalLevel = medalLevel;
            this.targetId = targetId;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("MedalInfo{");
            sb.append("additionalProperties=").append(additionalProperties);
            sb.append(", anchorUname='").append(anchorUname).append('\'');
            sb.append(", anchorRoomid=").append(anchorRoomid);
            sb.append(", guardLevel=").append(guardLevel);
            sb.append(", iconId=").append(iconId);
            sb.append(", isLighted=").append(isLighted);
            sb.append(", medalColor=").append(medalColor);
            sb.append(", medalColorBorder=").append(medalColorBorder);
            sb.append(", medalColorEnd=").append(medalColorEnd);
            sb.append(", medalColorStart=").append(medalColorStart);
            sb.append(", medalName='").append(medalName).append('\'');
            sb.append(", special='").append(special).append('\'');
            sb.append(", medalLevel=").append(medalLevel);
            sb.append(", targetId=").append(targetId);
            sb.append('}');
            return sb.toString();
        }

        public Map<String, Object> getAdditionalProperties() {
            return additionalProperties;
        }

        public String getAnchorUname() {
            return anchorUname;
        }

        public MedalInfo setAnchorUname(String anchorUname) {
            this.anchorUname = anchorUname;
            return this;
        }

        public Long getAnchorRoomid() {
            return anchorRoomid;
        }

        public MedalInfo setAnchorRoomid(Long anchorRoomid) {
            this.anchorRoomid = anchorRoomid;
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

        public Long getMedalColor() {
            return medalColor;
        }

        public MedalInfo setMedalColor(Long medalColor) {
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

        public Long getMedalLevel() {
            return medalLevel;
        }

        public MedalInfo setMedalLevel(Long medalLevel) {
            this.medalLevel = medalLevel;
            return this;
        }

        public Long getTargetId() {
            return targetId;
        }

        public MedalInfo setTargetId(Long targetId) {
            this.targetId = targetId;
            return this;
        }

        public void setAdditionalProperties(String key, Object object) {
            additionalProperties.put(key, object);
        }

    }
}
