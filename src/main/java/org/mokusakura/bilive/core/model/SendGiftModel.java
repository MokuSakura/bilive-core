package org.mokusakura.bilive.core.model;

import com.alibaba.fastjson.JSONObject;

/**
 * @author MokuSakura
 */
public class SendGiftModel extends AbstractDanmaku {
    protected Integer giftId;
    protected Integer giftType;
    protected String giftName;
    protected Double giftPrice;
    protected Integer giftNumber;
    protected Integer guardLevel;
    protected String guardName;
    protected Integer medalLevel;
    protected String medalName;


    protected SendGiftModel(String json) {
        var obj = JSONObject.parseObject(json);
        super.uid = obj.getJSONObject("data").getInteger("uid");
        super.timestamp = obj.getJSONObject("data").getLong("timestamp");
        super.messageType = MessageType.GiftSend;
        super.username = obj.getJSONObject("data").getString("uname");

        giftId = obj.getJSONObject("data").getInteger("giftId");
        giftType = obj.getJSONObject("data").getInteger("giftType");
        giftName = obj.getJSONObject("data").getString("giftName");
        giftPrice = obj.getJSONObject("data").getDouble("price");
        giftNumber = obj.getJSONObject("data").getInteger("num");
        guardLevel = obj.getJSONObject("data").getInteger("guard_level");
        guardName = mapGuardLevelToName(guardLevel);
        medalLevel = obj.getJSONObject("data").getJSONObject("medal_info").getInteger("medal_level");
        medalName = obj.getJSONObject("data").getJSONObject("medal_info").getString("medal_name");
    }

    public Integer getGiftId() {
        return giftId;
    }

    public SendGiftModel setGiftId(Integer giftId) {
        this.giftId = giftId;
        return this;
    }

    public Integer getGiftType() {
        return giftType;
    }

    public SendGiftModel setGiftType(Integer giftType) {
        this.giftType = giftType;
        return this;
    }

    public String getGiftName() {
        return giftName;
    }

    public SendGiftModel setGiftName(String giftName) {
        this.giftName = giftName;
        return this;
    }

    public Double getGiftPrice() {
        return giftPrice;
    }

    public SendGiftModel setGiftPrice(Double giftPrice) {
        this.giftPrice = giftPrice;
        return this;
    }

    public Integer getGiftNumber() {
        return giftNumber;
    }

    public SendGiftModel setGiftNumber(Integer giftNumber) {
        this.giftNumber = giftNumber;
        return this;
    }

    public Integer getGuardLevel() {
        return guardLevel;
    }

    public SendGiftModel setGuardLevel(Integer guardLevel) {
        this.guardLevel = guardLevel;
        return this;
    }

    public String getGuardName() {
        return guardName;
    }

    public SendGiftModel setGuardName(String guardName) {
        this.guardName = guardName;
        return this;
    }

    public Integer getMedalLevel() {
        return medalLevel;
    }

    public SendGiftModel setMedalLevel(Integer medalLevel) {
        this.medalLevel = medalLevel;
        return this;
    }

    public String getMedalName() {
        return medalName;
    }

    public SendGiftModel setMedalName(String medalName) {
        this.medalName = medalName;
        return this;
    }
}
