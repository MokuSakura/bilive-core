package org.mokusakura.danmakurecorder.core.model;

import com.alibaba.fastjson.JSONObject;

/**
 * @author MokuSakura
 */
public class SCModel extends AbstractDanmaku {
    protected Double price;
    protected String message;

    static {
        register("", SCModel::new);
    }

    protected Integer guardLevel;
    protected String guardName;
    protected Integer medalLevel;
    protected String medalName;

    protected Integer keepTime;

    protected SCModel(String json) {
        var obj = JSONObject.parseObject(json);
        super.messageType = MessageType.SuperChat;

        super.uid = obj.getJSONObject("data").getInteger("uid");
        super.username = obj.getJSONObject("data").getJSONObject("user_info").getString("uname");
        super.timestamp = obj.getJSONObject("data").getLong("start_time");
        this.price = obj.getJSONObject("data").getDouble("price");
        this.message = obj.getJSONObject("data").getString("message");
        this.keepTime = obj.getJSONObject("data").getInteger("time");
        guardLevel = obj.getJSONObject("data").getInteger("guard_level");
        guardName = mapGuardLevelToName(guardLevel);
        medalLevel = obj.getJSONObject("data").getJSONObject("medal_info").getInteger("medal_level");
        medalName = obj.getJSONObject("data").getJSONObject("medal_info").getString("medal_name");
    }

    public Double getPrice() {
        return price;
    }

    public SCModel setPrice(Double price) {
        this.price = price;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public SCModel setMessage(String message) {
        this.message = message;
        return this;
    }

    public Integer getKeepTime() {
        return keepTime;
    }

    public SCModel setKeepTime(Integer keepTime) {
        this.keepTime = keepTime;
        return this;
    }

    public Integer getGuardLevel() {
        return guardLevel;
    }

    public SCModel setGuardLevel(Integer guardLevel) {
        this.guardLevel = guardLevel;
        return this;
    }

    public String getGuardName() {
        return guardName;
    }

    public SCModel setGuardName(String guardName) {
        this.guardName = guardName;
        return this;
    }

    public Integer getMedalLevel() {
        return medalLevel;
    }

    public SCModel setMedalLevel(Integer medalLevel) {
        this.medalLevel = medalLevel;
        return this;
    }

    public String getMedalName() {
        return medalName;
    }

    public SCModel setMedalName(String medalName) {
        this.medalName = medalName;
        return this;
    }
}
