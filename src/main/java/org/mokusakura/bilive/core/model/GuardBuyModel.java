package org.mokusakura.bilive.core.model;

import com.alibaba.fastjson.JSONObject;

/**
 * @author MokuSakura
 */
public class GuardBuyModel extends AbstractDanmaku {
    protected Integer guardLevel;
    protected String guardName;
    protected Integer giftId;
    protected Double giftPrice;


    public GuardBuyModel(String json) {
        var obj = JSONObject.parseObject(json);
        super.messageType = MessageType.GuardBuy;
        super.uid = obj.getJSONObject("data").getLong("uid");
        super.username = obj.getJSONObject("data").getString("username");
        super.timestamp = obj.getJSONObject("data").getLong("start_time");
        this.guardLevel = obj.getJSONObject("data").getInteger("guard_level");
        this.guardName = obj.getJSONObject("data").getString("gift_name");
        this.giftId = obj.getJSONObject("data").getInteger("gift_id");
        this.giftPrice = obj.getJSONObject("data").getDouble("price");

    }

    public Integer getGuardLevel() {
        return guardLevel;
    }

    public GuardBuyModel setGuardLevel(Integer guardLevel) {
        this.guardLevel = guardLevel;
        return this;
    }

    public String getGuardName() {
        return guardName;
    }

    public GuardBuyModel setGuardName(String guardName) {
        this.guardName = guardName;
        return this;
    }

    public Integer getGiftId() {
        return giftId;
    }

    public GuardBuyModel setGiftId(Integer giftId) {
        this.giftId = giftId;
        return this;
    }

    public Double getGiftPrice() {
        return giftPrice;
    }

    public GuardBuyModel setGiftPrice(Double giftPrice) {
        this.giftPrice = giftPrice;
        return this;
    }
}
