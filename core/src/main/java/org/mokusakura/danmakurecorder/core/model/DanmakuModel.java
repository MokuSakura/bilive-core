package org.mokusakura.danmakurecorder.core.model;

import com.alibaba.fastjson.JSONObject;

/**
 * @author MokuSakura
 */
public class DanmakuModel {
    private DanmakuType danmakuType;
    private String title;
    private String parentAreaName;
    private String areaName;
    private String commentText;
    private String username;
    private Double price;
    private Integer scKeepTime;
    private Integer userId;
    private Integer userGuardLevel;
    private String giftName;
    private Integer giftCount;
    private Boolean admin;
    private Boolean vip;
    private String roomId;
    private String raw;
    private JSONObject rawObject;

    public DanmakuModel(String json) {
        this.raw = json;

        var obj = JSONObject.parseObject(json);
        this.rawObject = obj;

        var cmd = obj.getString("cmd") == null ? "" : obj.getString("cmd");
        if (cmd.startsWith("DANMU_MSG:")) {
            cmd = "DANMU_MSG";
        }

        switch (cmd) {
            case "LIVE":
                this.danmakuType = DanmakuType.LiveStart;
                this.roomId = obj.getString("roomid");
                break;
            case "PREPARING":
                this.danmakuType = DanmakuType.LiveEnd;
                this.roomId = obj.getString("roomid");
                break;
            // Array? Seriously?
            case "DANMU_MSG":
                this.danmakuType = DanmakuType.Comment;
                this.commentText = obj.getJSONArray("info").getString(1);
                this.userId = obj.getJSONArray("info").getJSONArray(2).getInteger(0);
                this.username = obj.getJSONArray("info").getJSONArray(2).getString(1);
                this.admin = "1".equals(obj.getJSONArray("info").getJSONArray(2).getString(2));
                this.vip = "1".equals(obj.getJSONArray("info").getJSONArray(2).getString(3));
                this.userGuardLevel = obj.getJSONArray("info").getInteger(7);
                break;
            case "SEND_GIFT":
                this.danmakuType = DanmakuType.GiftSend;
                this.giftName = obj.getJSONObject("data").getString("giftName");
                this.username = obj.getJSONObject("data").getString("uname");
                this.userId = obj.getJSONObject("data").getInteger("uid");
                this.giftCount = obj.getJSONObject("data").getInteger("num");
                break;
            case "GUARD_BUY": {
                this.danmakuType = DanmakuType.GuardBuy;
                this.userId = obj.getJSONObject("data").getInteger("uid");
                this.username = obj.getJSONObject("data").getString("username");
                this.userGuardLevel = obj.getJSONObject("data").getInteger("guard_level");
                this.giftName =
                        this.userGuardLevel.equals(3) ? "舰长" :
                                this.userGuardLevel.equals(2) ? "提督" :
                                        this.userGuardLevel.equals(1) ? "总督" : "";
                this.giftCount = obj.getJSONObject("data").getInteger("num");
                break;
            }
            case "SUPER_CHAT_MESSAGE": {
                this.danmakuType = DanmakuType.SuperChat;
                this.commentText = obj.getJSONObject("data").getString("message");
                this.userId = obj.getJSONObject("data").getInteger("uid");
                this.username = obj.getJSONObject("data").getJSONObject("user_info").getString("uname");
                this.price = obj.getJSONObject("data").getDouble("price");
                this.scKeepTime = obj.getJSONObject("data").getInteger("time");
                break;
            }
            case "ROOM_CHANGE": {
                this.danmakuType = DanmakuType.RoomChange;
                this.title = obj.getJSONObject("data").getString("title");
                this.areaName = obj.getJSONObject("data").getString("area_name");
                this.parentAreaName = obj.getJSONObject("data").getString("parent_area_name");
                break;
            }
            default: {
                this.danmakuType = DanmakuType.Unknown;
                break;
            }
        }


    }

    public DanmakuType getDanmakuType() {
        return danmakuType;
    }

    public DanmakuModel setDanmakuType(DanmakuType danmakuType) {
        this.danmakuType = danmakuType;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public DanmakuModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getParentAreaName() {
        return parentAreaName;
    }

    public DanmakuModel setParentAreaName(String parentAreaName) {
        this.parentAreaName = parentAreaName;
        return this;
    }

    public String getAreaName() {
        return areaName;
    }

    public DanmakuModel setAreaName(String areaName) {
        this.areaName = areaName;
        return this;
    }

    public String getCommentText() {
        return commentText;
    }

    public DanmakuModel setCommentText(String commentText) {
        this.commentText = commentText;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public DanmakuModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public DanmakuModel setPrice(Double price) {
        this.price = price;
        return this;
    }

    public Integer getScKeepTime() {
        return scKeepTime;
    }

    public DanmakuModel setScKeepTime(Integer scKeepTime) {
        this.scKeepTime = scKeepTime;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public DanmakuModel setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getUserGuardLevel() {
        return userGuardLevel;
    }

    public DanmakuModel setUserGuardLevel(Integer userGuardLevel) {
        this.userGuardLevel = userGuardLevel;
        return this;
    }

    public String getGiftName() {
        return giftName;
    }

    public DanmakuModel setGiftName(String giftName) {
        this.giftName = giftName;
        return this;
    }

    public Integer getGiftCount() {
        return giftCount;
    }

    public DanmakuModel setGiftCount(Integer giftCount) {
        this.giftCount = giftCount;
        return this;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public DanmakuModel setAdmin(Boolean admin) {
        this.admin = admin;
        return this;
    }

    public Boolean getVip() {
        return vip;
    }

    public DanmakuModel setVip(Boolean vip) {
        this.vip = vip;
        return this;
    }

    public String getRoomId() {
        return roomId;
    }

    public DanmakuModel setRoomId(String roomId) {
        this.roomId = roomId;
        return this;
    }

    public String getRaw() {
        return raw;
    }

    public DanmakuModel setRaw(String raw) {
        this.raw = raw;
        return this;
    }

    public JSONObject getRawObject() {
        return rawObject;
    }

    public DanmakuModel setRawObject(JSONObject rawObject) {
        this.rawObject = rawObject;
        return this;
    }
}
