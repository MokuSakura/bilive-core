package org.mokusakura.danmakurecorder.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author MokuSakura
 */
public class RoomInit {
    @JSONField(name = "room_id")
    private Integer roomId;
    @JSONField(name = "short_id")
    private Integer shortId;
    @JSONField(name = "uid")
    private Integer uid;
    @JSONField(name = "need_p2p")
    private Integer needP2P;
    @JSONField(name = "is_hidden")
    private Boolean isHidden;
    @JSONField(name = "is_locked")
    private Boolean isLocked;
    @JSONField(name = "is_portrait")
    private Boolean isPortrait;
    @JSONField(name = "live_status")
    private Integer liveStatus;
    @JSONField(name = "hidden_till")
    private Integer hiddenTill;
    @JSONField(name = "lock_till")
    private Integer lockTill;
    @JSONField(name = "encrypted")
    private Boolean encrypted;
    @JSONField(name = "pwd_verified")
    private Boolean pwdVerified;
    @JSONField(name = "live_time")
    private Long liveTime;
    @JSONField(name = "room_shield")
    private Integer roomShield;
    @JSONField(name = "is_sp")
    private Integer isSp;
    @JSONField(name = "special_type")
    private Integer specialType;


    public Integer getRoomId() {
        return roomId;
    }

    public RoomInit setRoomId(Integer roomId) {
        this.roomId = roomId;
        return this;
    }

    public Integer getShortId() {
        return shortId;
    }

    public RoomInit setShortId(Integer shortId) {
        this.shortId = shortId;
        return this;
    }

    public Integer getUid() {
        return uid;
    }

    public RoomInit setUid(Integer uid) {
        this.uid = uid;
        return this;
    }

    public Integer getNeedP2P() {
        return needP2P;
    }

    public RoomInit setNeedP2P(Integer needP2P) {
        this.needP2P = needP2P;
        return this;
    }

    public Boolean getHidden() {
        return isHidden;
    }

    public RoomInit setHidden(Boolean hidden) {
        isHidden = hidden;
        return this;
    }

    public Boolean getLocked() {
        return isLocked;
    }

    public RoomInit setLocked(Boolean locked) {
        isLocked = locked;
        return this;
    }

    public Boolean getPortrait() {
        return isPortrait;
    }

    public RoomInit setPortrait(Boolean portrait) {
        isPortrait = portrait;
        return this;
    }

    public Integer getLiveStatus() {
        return liveStatus;
    }

    public RoomInit setLiveStatus(Integer liveStatus) {
        this.liveStatus = liveStatus;
        return this;
    }

    public Integer getHiddenTill() {
        return hiddenTill;
    }

    public RoomInit setHiddenTill(Integer hiddenTill) {
        this.hiddenTill = hiddenTill;
        return this;
    }

    public Integer getLockTill() {
        return lockTill;
    }

    public RoomInit setLockTill(Integer lockTill) {
        this.lockTill = lockTill;
        return this;
    }

    public Boolean getEncrypted() {
        return encrypted;
    }

    public RoomInit setEncrypted(Boolean encrypted) {
        this.encrypted = encrypted;
        return this;
    }

    public Boolean getPwdVerified() {
        return pwdVerified;
    }

    public RoomInit setPwdVerified(Boolean pwdVerified) {
        this.pwdVerified = pwdVerified;
        return this;
    }

    public Long getLiveTime() {
        return liveTime;
    }

    public RoomInit setLiveTime(Long liveTime) {
        this.liveTime = liveTime;
        return this;
    }

    public Integer getRoomShield() {
        return roomShield;
    }

    public RoomInit setRoomShield(Integer roomShield) {
        this.roomShield = roomShield;
        return this;
    }

    public Integer getIsSp() {
        return isSp;
    }

    public RoomInit setIsSp(Integer isSp) {
        this.isSp = isSp;
        return this;
    }

    public Integer getSpecialType() {
        return specialType;
    }

    public RoomInit setSpecialType(Integer specialType) {
        this.specialType = specialType;
        return this;
    }
}
