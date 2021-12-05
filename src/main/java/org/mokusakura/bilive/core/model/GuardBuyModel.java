package org.mokusakura.bilive.core.model;

import com.alibaba.fastjson.JSONObject;

/**
 * @author MokuSakura
 */
public class GuardBuyModel extends AbstractDanmaku {
    protected Integer guardLevel;
    protected String giftName;
    protected Integer giftId;
    protected Double price;
    protected Integer num;
    protected Long startTime;
    protected Long endTime;


    public GuardBuyModel(String json) {
        var obj = JSONObject.parseObject(json);
        super.messageType = MessageType.GuardBuy;
        super.uid = obj.getJSONObject("data").getLong("uid");
        super.username = obj.getJSONObject("data").getString("username");
        super.timestamp = obj.getJSONObject("data").getLong("start_time");
        this.guardLevel = obj.getJSONObject("data").getInteger("guard_level");
        this.giftName = obj.getJSONObject("data").getString("gift_name");
        this.giftId = obj.getJSONObject("data").getInteger("gift_id");
        this.price = obj.getJSONObject("data").getDouble("price");
        this.num = obj.getJSONObject("data").getInteger("num");
        this.startTime = obj.getJSONObject("data").getLong("start_time");
        this.endTime = obj.getJSONObject("data").getLong("end_time");
        super.rawMessage = json;
    }

    public Integer getGuardLevel() {
        return guardLevel;
    }

    public GuardBuyModel setGuardLevel(Integer guardLevel) {
        this.guardLevel = guardLevel;
        return this;
    }

    public String getGiftName() {
        return giftName;
    }

    public GuardBuyModel setGiftName(String giftName) {
        this.giftName = giftName;
        return this;
    }

    public Integer getGiftId() {
        return giftId;
    }

    public GuardBuyModel setGiftId(Integer giftId) {
        this.giftId = giftId;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public GuardBuyModel setPrice(Double price) {
        this.price = price;
        return this;
    }

    public Integer getNum() {
        return num;
    }

    public GuardBuyModel setNum(Integer num) {
        this.num = num;
        return this;
    }

    public Long getStartTime() {
        return startTime;
    }

    public GuardBuyModel setStartTime(Long startTime) {
        this.startTime = startTime;
        return this;
    }

    public Long getEndTime() {
        return endTime;
    }

    public GuardBuyModel setEndTime(Long endTime) {
        this.endTime = endTime;
        return this;
    }
}
