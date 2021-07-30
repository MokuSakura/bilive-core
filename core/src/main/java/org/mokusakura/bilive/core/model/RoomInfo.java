package org.mokusakura.bilive.core.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author MokuSakura
 */
public class RoomInfo {
    @JSONField(name = "room_id")
    private Integer roomId;
    @JSONField(name = "short_id")
    private Integer shortId;
    @JSONField(name = "live_status")
    private Integer liveStatus;
    @JSONField(name = "area_id")
    private Integer areaId;
    @JSONField(name = "parent_area_id")
    private Integer parentAreaId;
    @JSONField(name = "area_name")
    private String areaName;
    @JSONField(name = "parent_area_name")
    private String parentAreaName;
    @JSONField(name = "title")
    private String title;

    public Integer getRoomId() {
        return roomId;
    }

    public RoomInfo setRoomId(Integer roomId) {
        this.roomId = roomId;
        return this;
    }

    public Integer getShortId() {
        return shortId;
    }

    public RoomInfo setShortId(Integer shortId) {
        this.shortId = shortId;
        return this;
    }

    public Integer getLiveStatus() {
        return liveStatus;
    }

    public RoomInfo setLiveStatus(Integer liveStatus) {
        this.liveStatus = liveStatus;
        return this;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public RoomInfo setAreaId(Integer areaId) {
        this.areaId = areaId;
        return this;
    }

    public Integer getParentAreaId() {
        return parentAreaId;
    }

    public RoomInfo setParentAreaId(Integer parentAreaId) {
        this.parentAreaId = parentAreaId;
        return this;
    }

    public String getAreaName() {
        return areaName;
    }

    public RoomInfo setAreaName(String areaName) {
        this.areaName = areaName;
        return this;
    }

    public String getParentAreaName() {
        return parentAreaName;
    }

    public RoomInfo setParentAreaName(String parentAreaName) {
        this.parentAreaName = parentAreaName;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public RoomInfo setTitle(String title) {
        this.title = title;
        return this;
    }
}
