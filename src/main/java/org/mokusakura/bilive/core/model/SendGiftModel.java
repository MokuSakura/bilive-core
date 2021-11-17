package org.mokusakura.bilive.core.model;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class SendGiftModel extends AbstractDanmaku {
    private String action;
    private String batchComboId;
    private BatchComboSend batchComboSend;
    private String beatId;
    private String bizSource;
    private String blindGift;
    private int broadcastId;
    private String coinType;
    private int comboResourcesId;
    private ComboSend comboSend;
    private int comboStayTime;
    private long comboTotalCoin;
    private int critProb;
    private int demarcation;
    private int dmscore;
    private int draw;
    private int effect;
    private int effectBlock;
    private String face;
    private int giftId;
    private String giftName;
    private int giftType;
    private int gold;
    private int guardLevel;
    private boolean isFirst;
    private int isSpecialBatch;
    private int magnification;
    private MedalInfo medalInfo;
    private String nameColor;
    private int num;
    private String originalGiftName;
    private long price;
    private long rcost;
    private int remain;
    private String rnd;
    private String sendMaster;
    private int silver;
    private int super_;
    private int superBatchGiftNum;
    private int superGiftNum;
    private int svgaBlock;
    private String tagImage;
    private String tid;
    private String topList;
    private long totalCoin;
    private String uname;

    public static SendGiftModel createSendGiftModel(String json) {
        SendGiftModel res = JSONObject.parseObject(json, SendGiftModel.class);
        res.setUsername(res.getUname()).setMessageType("SendGiftModel").setRawMessage(json);
        return res;
    }

    public String getAction() {
        return action;
    }

    public SendGiftModel setAction(String action) {
        this.action = action;
        return this;
    }

    public String getBatchComboId() {
        return batchComboId;
    }

    public SendGiftModel setBatchComboId(String batchComboId) {
        this.batchComboId = batchComboId;
        return this;
    }

    public BatchComboSend getBatchComboSend() {
        return batchComboSend;
    }

    public SendGiftModel setBatchComboSend(BatchComboSend batchComboSend) {
        this.batchComboSend = batchComboSend;
        return this;
    }

    public String getBeatId() {
        return beatId;
    }

    public SendGiftModel setBeatId(String beatId) {
        this.beatId = beatId;
        return this;
    }

    public String getBizSource() {
        return bizSource;
    }

    public SendGiftModel setBizSource(String bizSource) {
        this.bizSource = bizSource;
        return this;
    }

    public String getBlindGift() {
        return blindGift;
    }

    public SendGiftModel setBlindGift(String blindGift) {
        this.blindGift = blindGift;
        return this;
    }

    public int getBroadcastId() {
        return broadcastId;
    }

    public SendGiftModel setBroadcastId(int broadcastId) {
        this.broadcastId = broadcastId;
        return this;
    }

    public String getCoinType() {
        return coinType;
    }

    public SendGiftModel setCoinType(String coinType) {
        this.coinType = coinType;
        return this;
    }

    public int getComboResourcesId() {
        return comboResourcesId;
    }

    public SendGiftModel setComboResourcesId(int comboResourcesId) {
        this.comboResourcesId = comboResourcesId;
        return this;
    }

    public ComboSend getComboSend() {
        return comboSend;
    }

    public SendGiftModel setComboSend(ComboSend comboSend) {
        this.comboSend = comboSend;
        return this;
    }

    public int getComboStayTime() {
        return comboStayTime;
    }

    public SendGiftModel setComboStayTime(int comboStayTime) {
        this.comboStayTime = comboStayTime;
        return this;
    }

    public long getComboTotalCoin() {
        return comboTotalCoin;
    }

    public SendGiftModel setComboTotalCoin(long comboTotalCoin) {
        this.comboTotalCoin = comboTotalCoin;
        return this;
    }

    public int getCritProb() {
        return critProb;
    }

    public SendGiftModel setCritProb(int critProb) {
        this.critProb = critProb;
        return this;
    }

    public int getDemarcation() {
        return demarcation;
    }

    public SendGiftModel setDemarcation(int demarcation) {
        this.demarcation = demarcation;
        return this;
    }

    public int getDmscore() {
        return dmscore;
    }

    public SendGiftModel setDmscore(int dmscore) {
        this.dmscore = dmscore;
        return this;
    }

    public int getDraw() {
        return draw;
    }

    public SendGiftModel setDraw(int draw) {
        this.draw = draw;
        return this;
    }

    public int getEffect() {
        return effect;
    }

    public SendGiftModel setEffect(int effect) {
        this.effect = effect;
        return this;
    }

    public int getEffectBlock() {
        return effectBlock;
    }

    public SendGiftModel setEffectBlock(int effectBlock) {
        this.effectBlock = effectBlock;
        return this;
    }

    public String getFace() {
        return face;
    }

    public SendGiftModel setFace(String face) {
        this.face = face;
        return this;
    }

    public int getGiftId() {
        return giftId;
    }

    public SendGiftModel setGiftId(int giftId) {
        this.giftId = giftId;
        return this;
    }

    public String getGiftName() {
        return giftName;
    }

    public SendGiftModel setGiftName(String giftName) {
        this.giftName = giftName;
        return this;
    }

    public int getGiftType() {
        return giftType;
    }

    public SendGiftModel setGiftType(int giftType) {
        this.giftType = giftType;
        return this;
    }

    public int getGold() {
        return gold;
    }

    public SendGiftModel setGold(int gold) {
        this.gold = gold;
        return this;
    }

    public int getGuardLevel() {
        return guardLevel;
    }

    public SendGiftModel setGuardLevel(int guardLevel) {
        this.guardLevel = guardLevel;
        return this;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public SendGiftModel setFirst(boolean first) {
        isFirst = first;
        return this;
    }

    public int getIsSpecialBatch() {
        return isSpecialBatch;
    }

    public SendGiftModel setIsSpecialBatch(int isSpecialBatch) {
        this.isSpecialBatch = isSpecialBatch;
        return this;
    }

    public int getMagnification() {
        return magnification;
    }

    public SendGiftModel setMagnification(int magnification) {
        this.magnification = magnification;
        return this;
    }

    public MedalInfo getMedalInfo() {
        return medalInfo;
    }

    public SendGiftModel setMedalInfo(MedalInfo medalInfo) {
        this.medalInfo = medalInfo;
        return this;
    }

    public String getNameColor() {
        return nameColor;
    }

    public SendGiftModel setNameColor(String nameColor) {
        this.nameColor = nameColor;
        return this;
    }

    public int getNum() {
        return num;
    }

    public SendGiftModel setNum(int num) {
        this.num = num;
        return this;
    }

    public String getOriginalGiftName() {
        return originalGiftName;
    }

    public SendGiftModel setOriginalGiftName(String originalGiftName) {
        this.originalGiftName = originalGiftName;
        return this;
    }

    public long getPrice() {
        return price;
    }

    public SendGiftModel setPrice(long price) {
        this.price = price;
        return this;
    }

    public long getRcost() {
        return rcost;
    }

    public SendGiftModel setRcost(long rcost) {
        this.rcost = rcost;
        return this;
    }

    public int getRemain() {
        return remain;
    }

    public SendGiftModel setRemain(int remain) {
        this.remain = remain;
        return this;
    }

    public String getRnd() {
        return rnd;
    }

    public SendGiftModel setRnd(String rnd) {
        this.rnd = rnd;
        return this;
    }

    public String getSendMaster() {
        return sendMaster;
    }

    public SendGiftModel setSendMaster(String sendMaster) {
        this.sendMaster = sendMaster;
        return this;
    }

    public int getSilver() {
        return silver;
    }

    public SendGiftModel setSilver(int silver) {
        this.silver = silver;
        return this;
    }

    @JSONField(name = "super")
    public int getSuper_() {
        return super_;
    }

    @JSONField(name = "super")
    public SendGiftModel setSuper_(int super_) {
        this.super_ = super_;
        return this;
    }

    public int getSuperBatchGiftNum() {
        return superBatchGiftNum;
    }

    public SendGiftModel setSuperBatchGiftNum(int superBatchGiftNum) {
        this.superBatchGiftNum = superBatchGiftNum;
        return this;
    }

    public int getSuperGiftNum() {
        return superGiftNum;
    }

    public SendGiftModel setSuperGiftNum(int superGiftNum) {
        this.superGiftNum = superGiftNum;
        return this;
    }

    public int getSvgaBlock() {
        return svgaBlock;
    }

    public SendGiftModel setSvgaBlock(int svgaBlock) {
        this.svgaBlock = svgaBlock;
        return this;
    }

    public String getTagImage() {
        return tagImage;
    }

    public SendGiftModel setTagImage(String tagImage) {
        this.tagImage = tagImage;
        return this;
    }

    public String getTid() {
        return tid;
    }

    public SendGiftModel setTid(String tid) {
        this.tid = tid;
        return this;
    }

    public String getTopList() {
        return topList;
    }

    public SendGiftModel setTopList(String topList) {
        this.topList = topList;
        return this;
    }

    public long getTotalCoin() {
        return totalCoin;
    }

    public SendGiftModel setTotalCoin(long totalCoin) {
        this.totalCoin = totalCoin;
        return this;
    }

    public String getUname() {
        return uname;
    }

    public SendGiftModel setUname(String uname) {
        this.uname = uname;
        return this;
    }

    public static class BatchComboSend {
        private String action;
        private String batchComboId;
        private int batchComboNum;
        private String blindGift;
        private int giftId;
        private String giftName;
        private int giftNum;
        private String sendMaster;
        private long uid;
        private String uname;

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

        public int getBatchComboNum() {
            return batchComboNum;
        }

        public BatchComboSend setBatchComboNum(int batchComboNum) {
            this.batchComboNum = batchComboNum;
            return this;
        }

        public String getBlindGift() {
            return blindGift;
        }

        public BatchComboSend setBlindGift(String blindGift) {
            this.blindGift = blindGift;
            return this;
        }

        public int getGiftId() {
            return giftId;
        }

        public BatchComboSend setGiftId(int giftId) {
            this.giftId = giftId;
            return this;
        }

        public String getGiftName() {
            return giftName;
        }

        public BatchComboSend setGiftName(String giftName) {
            this.giftName = giftName;
            return this;
        }

        public int getGiftNum() {
            return giftNum;
        }

        public BatchComboSend setGiftNum(int giftNum) {
            this.giftNum = giftNum;
            return this;
        }

        public String getSendMaster() {
            return sendMaster;
        }

        public BatchComboSend setSendMaster(String sendMaster) {
            this.sendMaster = sendMaster;
            return this;
        }

        public long getUid() {
            return uid;
        }

        public BatchComboSend setUid(long uid) {
            this.uid = uid;
            return this;
        }

        public String getUname() {
            return uname;
        }

        public BatchComboSend setUname(String uname) {
            this.uname = uname;
            return this;
        }
    }

    public static class ComboSend {
        private String action;
        private String comboId;
        private int comboNum;
        private int giftId;
        private String giftName;
        private int giftNum;
        private String sendMaster;
        private long uid;
        private String uname;

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

        public int getComboNum() {
            return comboNum;
        }

        public ComboSend setComboNum(int comboNum) {
            this.comboNum = comboNum;
            return this;
        }

        public int getGiftId() {
            return giftId;
        }

        public ComboSend setGiftId(int giftId) {
            this.giftId = giftId;
            return this;
        }

        public String getGiftName() {
            return giftName;
        }

        public ComboSend setGiftName(String giftName) {
            this.giftName = giftName;
            return this;
        }

        public int getGiftNum() {
            return giftNum;
        }

        public ComboSend setGiftNum(int giftNum) {
            this.giftNum = giftNum;
            return this;
        }

        public String getSendMaster() {
            return sendMaster;
        }

        public ComboSend setSendMaster(String sendMaster) {
            this.sendMaster = sendMaster;
            return this;
        }

        public long getUid() {
            return uid;
        }

        public ComboSend setUid(long uid) {
            this.uid = uid;
            return this;
        }

        public String getUname() {
            return uname;
        }

        public ComboSend setUname(String uname) {
            this.uname = uname;
            return this;
        }
    }

    public static class MedalInfo {
        private int anchorRoomid;
        private String anchorUname;
        private int guardLevel;
        private int iconId;
        private int isLighted;
        private long medalColor;
        private long medalColorBorder;
        private long medalColorEnd;
        private long medalColorStart;
        private int medalLevel;
        private String medalName;
        private String special;
        private long targetId;

        public int getAnchorRoomid() {
            return anchorRoomid;
        }

        public MedalInfo setAnchorRoomid(int anchorRoomid) {
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

        public int getGuardLevel() {
            return guardLevel;
        }

        public MedalInfo setGuardLevel(int guardLevel) {
            this.guardLevel = guardLevel;
            return this;
        }

        public int getIconId() {
            return iconId;
        }

        public MedalInfo setIconId(int iconId) {
            this.iconId = iconId;
            return this;
        }

        public int getIsLighted() {
            return isLighted;
        }

        public MedalInfo setIsLighted(int isLighted) {
            this.isLighted = isLighted;
            return this;
        }

        public long getMedalColor() {
            return medalColor;
        }

        public MedalInfo setMedalColor(long medalColor) {
            this.medalColor = medalColor;
            return this;
        }

        public long getMedalColorBorder() {
            return medalColorBorder;
        }

        public MedalInfo setMedalColorBorder(long medalColorBorder) {
            this.medalColorBorder = medalColorBorder;
            return this;
        }

        public long getMedalColorEnd() {
            return medalColorEnd;
        }

        public MedalInfo setMedalColorEnd(long medalColorEnd) {
            this.medalColorEnd = medalColorEnd;
            return this;
        }

        public long getMedalColorStart() {
            return medalColorStart;
        }

        public MedalInfo setMedalColorStart(long medalColorStart) {
            this.medalColorStart = medalColorStart;
            return this;
        }

        public int getMedalLevel() {
            return medalLevel;
        }

        public MedalInfo setMedalLevel(int medalLevel) {
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

        public long getTargetId() {
            return targetId;
        }

        public MedalInfo setTargetId(long targetId) {
            this.targetId = targetId;
            return this;
        }
    }

}


/**
 * @author MokuSakura
 */
//public class SendGiftModel extends AbstractDanmaku {
//    private Integer giftId;
//    private Integer giftType;
//    private String giftName;
//    private Double giftPrice;
//    private Integer giftNumber;
//    private Integer guardLevel;
//    private String guardName;
//    private Integer medalLevel;
//    private String medalName;
//
//
//    public SendGiftModel(String json) {
//        var obj = JSONObject.parseObject(json);
//        super.uid = obj.getJSONObject("data").getInteger("uid");
//        super.timestamp = obj.getJSONObject("data").getLong("timestamp");
//        super.messageType = MessageType.GiftSend;
//        super.username = obj.getJSONObject("data").getString("uname");
//
//        giftId = obj.getJSONObject("data").getInteger("giftId");
//        giftType = obj.getJSONObject("data").getInteger("giftType");
//        giftName = obj.getJSONObject("data").getString("giftName");
//        giftPrice = obj.getJSONObject("data").getDouble("price");
//        giftNumber = obj.getJSONObject("data").getInteger("num");
//        guardLevel = obj.getJSONObject("data").getInteger("guard_level");
//        guardName = mapGuardLevelToName(guardLevel);
//        var medalInfo = obj.getJSONObject("data").getJSONObject("medal_info");
//        if (medalInfo != null) {
//            medalLevel = medalInfo.getInteger("medal_level");
//            medalName = medalInfo.getString("medal_name");
//        }
//    }
//
//    public Integer getGiftId() {
//        return giftId;
//    }
//
//    public SendGiftModel setGiftId(Integer giftId) {
//        this.giftId = giftId;
//        return this;
//    }
//
//    public Integer getGiftType() {
//        return giftType;
//    }
//
//    public SendGiftModel setGiftType(Integer giftType) {
//        this.giftType = giftType;
//        return this;
//    }
//
//    public String getGiftName() {
//        return giftName;
//    }
//
//    public SendGiftModel setGiftName(String giftName) {
//        this.giftName = giftName;
//        return this;
//    }
//
//    public Double getGiftPrice() {
//        return giftPrice;
//    }
//
//    public SendGiftModel setGiftPrice(Double giftPrice) {
//        this.giftPrice = giftPrice;
//        return this;
//    }
//
//    public Integer getGiftNumber() {
//        return giftNumber;
//    }
//
//    public SendGiftModel setGiftNumber(Integer giftNumber) {
//        this.giftNumber = giftNumber;
//        return this;
//    }
//
//    public Integer getGuardLevel() {
//        return guardLevel;
//    }
//
//    public SendGiftModel setGuardLevel(Integer guardLevel) {
//        this.guardLevel = guardLevel;
//        return this;
//    }
//
//    public String getGuardName() {
//        return guardName;
//    }
//
//    public SendGiftModel setGuardName(String guardName) {
//        this.guardName = guardName;
//        return this;
//    }
//
//    public Integer getMedalLevel() {
//        return medalLevel;
//    }
//
//    public SendGiftModel setMedalLevel(Integer medalLevel) {
//        this.medalLevel = medalLevel;
//        return this;
//    }
//
//    public String getMedalName() {
//        return medalName;
//    }
//
//    public SendGiftModel setMedalName(String medalName) {
//        this.medalName = medalName;
//        return this;
//    }
//}
