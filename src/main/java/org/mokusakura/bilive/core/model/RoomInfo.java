package org.mokusakura.bilive.core.model;
import java.util.List;


public class RoomInfo {

    private Long uid;
    private Integer roomId;
    private Integer shortId;
    private Long attention;
    private Long online;
    private Boolean isPortrait;
    private String description;
    private Integer liveStatus;
    private Integer areaId;
    private Integer parentAreaId;
    private String parentAreaName;
    private Integer oldAreaId;
    private String background;
    private String title;
    private String userCover;
    private String keyframe;
    private Boolean isStrictRoom;
    private String liveTime;
    private String tags;
    private Integer isAnchor;
    private String roomSilentType;
    private Integer roomSilentLevel;
    private Integer roomSilentSecond;
    private String areaName;
    private String pendants;
    private String areaPendants;
    private List<String> hotWords;
    private Integer hotWordsStatus;
    private String verify;
    private NewPendants newPendants;
    private String upSession;
    private Integer pkStatus;
    private Integer pkId;
    private Integer battleId;
    private Integer allowChangeAreaTime;
    private Integer allowUploadCoverTime;
    private StudioInfo studioInfo;

    public Long getUid() {
        return uid;
    }

    public RoomInfo setUid(Long uid) {
        this.uid = uid;
        return this;
    }

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

    public Long getAttention() {
        return attention;
    }

    public RoomInfo setAttention(Long attention) {
        this.attention = attention;
        return this;
    }

    public Long getOnline() {
        return online;
    }

    public RoomInfo setOnline(Long online) {
        this.online = online;
        return this;
    }

    public Boolean getPortrait() {
        return isPortrait;
    }

    public RoomInfo setPortrait(Boolean portrait) {
        isPortrait = portrait;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RoomInfo setDescription(String description) {
        this.description = description;
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

    public String getParentAreaName() {
        return parentAreaName;
    }

    public RoomInfo setParentAreaName(String parentAreaName) {
        this.parentAreaName = parentAreaName;
        return this;
    }

    public Integer getOldAreaId() {
        return oldAreaId;
    }

    public RoomInfo setOldAreaId(Integer oldAreaId) {
        this.oldAreaId = oldAreaId;
        return this;
    }

    public String getBackground() {
        return background;
    }

    public RoomInfo setBackground(String background) {
        this.background = background;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public RoomInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getUserCover() {
        return userCover;
    }

    public RoomInfo setUserCover(String userCover) {
        this.userCover = userCover;
        return this;
    }

    public String getKeyframe() {
        return keyframe;
    }

    public RoomInfo setKeyframe(String keyframe) {
        this.keyframe = keyframe;
        return this;
    }

    public Boolean getStrictRoom() {
        return isStrictRoom;
    }

    public RoomInfo setStrictRoom(Boolean strictRoom) {
        isStrictRoom = strictRoom;
        return this;
    }

    public String getLiveTime() {
        return liveTime;
    }

    public RoomInfo setLiveTime(String liveTime) {
        this.liveTime = liveTime;
        return this;
    }

    public String getTags() {
        return tags;
    }

    public RoomInfo setTags(String tags) {
        this.tags = tags;
        return this;
    }

    public Integer getIsAnchor() {
        return isAnchor;
    }

    public RoomInfo setIsAnchor(Integer isAnchor) {
        this.isAnchor = isAnchor;
        return this;
    }

    public String getRoomSilentType() {
        return roomSilentType;
    }

    public RoomInfo setRoomSilentType(String roomSilentType) {
        this.roomSilentType = roomSilentType;
        return this;
    }

    public Integer getRoomSilentLevel() {
        return roomSilentLevel;
    }

    public RoomInfo setRoomSilentLevel(Integer roomSilentLevel) {
        this.roomSilentLevel = roomSilentLevel;
        return this;
    }

    public Integer getRoomSilentSecond() {
        return roomSilentSecond;
    }

    public RoomInfo setRoomSilentSecond(Integer roomSilentSecond) {
        this.roomSilentSecond = roomSilentSecond;
        return this;
    }

    public String getAreaName() {
        return areaName;
    }

    public RoomInfo setAreaName(String areaName) {
        this.areaName = areaName;
        return this;
    }

    public String getPendants() {
        return pendants;
    }

    public RoomInfo setPendants(String pendants) {
        this.pendants = pendants;
        return this;
    }

    public String getAreaPendants() {
        return areaPendants;
    }

    public RoomInfo setAreaPendants(String areaPendants) {
        this.areaPendants = areaPendants;
        return this;
    }

    public List<String> getHotWords() {
        return hotWords;
    }

    public RoomInfo setHotWords(List<String> hotWords) {
        this.hotWords = hotWords;
        return this;
    }

    public Integer getHotWordsStatus() {
        return hotWordsStatus;
    }

    public RoomInfo setHotWordsStatus(Integer hotWordsStatus) {
        this.hotWordsStatus = hotWordsStatus;
        return this;
    }

    public String getVerify() {
        return verify;
    }

    public RoomInfo setVerify(String verify) {
        this.verify = verify;
        return this;
    }

    public NewPendants getNewPendants() {
        return newPendants;
    }

    public RoomInfo setNewPendants(NewPendants newPendants) {
        this.newPendants = newPendants;
        return this;
    }

    public String getUpSession() {
        return upSession;
    }

    public RoomInfo setUpSession(String upSession) {
        this.upSession = upSession;
        return this;
    }

    public Integer getPkStatus() {
        return pkStatus;
    }

    public RoomInfo setPkStatus(Integer pkStatus) {
        this.pkStatus = pkStatus;
        return this;
    }

    public Integer getPkId() {
        return pkId;
    }

    public RoomInfo setPkId(Integer pkId) {
        this.pkId = pkId;
        return this;
    }

    public Integer getBattleId() {
        return battleId;
    }

    public RoomInfo setBattleId(Integer battleId) {
        this.battleId = battleId;
        return this;
    }

    public Integer getAllowChangeAreaTime() {
        return allowChangeAreaTime;
    }

    public RoomInfo setAllowChangeAreaTime(Integer allowChangeAreaTime) {
        this.allowChangeAreaTime = allowChangeAreaTime;
        return this;
    }

    public Integer getAllowUploadCoverTime() {
        return allowUploadCoverTime;
    }

    public RoomInfo setAllowUploadCoverTime(Integer allowUploadCoverTime) {
        this.allowUploadCoverTime = allowUploadCoverTime;
        return this;
    }

    public StudioInfo getStudioInfo() {
        return studioInfo;
    }

    public RoomInfo setStudioInfo(StudioInfo studioInfo) {
        this.studioInfo = studioInfo;
        return this;
    }

    public static class NewPendants {

        private Frame frame;
        private Badge badge;
        private MobileFrame mobileFrame;
        private String mobileBadge;

        public Frame getFrame() {
            return frame;
        }

        public NewPendants setFrame(Frame frame) {
            this.frame = frame;
            return this;
        }

        public Badge getBadge() {
            return badge;
        }

        public NewPendants setBadge(Badge badge) {
            this.badge = badge;
            return this;
        }

        public MobileFrame getMobileFrame() {
            return mobileFrame;
        }

        public NewPendants setMobileFrame(MobileFrame mobileFrame) {
            this.mobileFrame = mobileFrame;
            return this;
        }

        public String getMobileBadge() {
            return mobileBadge;
        }

        public NewPendants setMobileBadge(String mobileBadge) {
            this.mobileBadge = mobileBadge;
            return this;
        }

        public static class Frame {

            private String name;
            private String value;
            private Integer position;
            private String desc;
            private Integer area;
            private Integer areaOld;
            private String bgColor;
            private String bgPic;
            private Boolean useOldArea;

            public String getName() {
                return name;
            }

            public Frame setName(String name) {
                this.name = name;
                return this;
            }

            public String getValue() {
                return value;
            }

            public Frame setValue(String value) {
                this.value = value;
                return this;
            }

            public Integer getPosition() {
                return position;
            }

            public Frame setPosition(Integer position) {
                this.position = position;
                return this;
            }

            public String getDesc() {
                return desc;
            }

            public Frame setDesc(String desc) {
                this.desc = desc;
                return this;
            }

            public Integer getArea() {
                return area;
            }

            public Frame setArea(Integer area) {
                this.area = area;
                return this;
            }

            public Integer getAreaOld() {
                return areaOld;
            }

            public Frame setAreaOld(Integer areaOld) {
                this.areaOld = areaOld;
                return this;
            }

            public String getBgColor() {
                return bgColor;
            }

            public Frame setBgColor(String bgColor) {
                this.bgColor = bgColor;
                return this;
            }

            public String getBgPic() {
                return bgPic;
            }

            public Frame setBgPic(String bgPic) {
                this.bgPic = bgPic;
                return this;
            }

            public Boolean getUseOldArea() {
                return useOldArea;
            }

            public Frame setUseOldArea(Boolean useOldArea) {
                this.useOldArea = useOldArea;
                return this;
            }
        }

        public static class Badge {
            private String name;
            private Integer position;
            private String desc;

            public String getName() {
                return name;
            }

            public Badge setName(String name) {
                this.name = name;
                return this;
            }

            public Integer getPosition() {
                return position;
            }

            public Badge setPosition(Integer position) {
                this.position = position;
                return this;
            }

            public String getDesc() {
                return desc;
            }

            public Badge setDesc(String desc) {
                this.desc = desc;
                return this;
            }
        }

        public static class MobileFrame {

            private String name;
            private String value;
            private Integer position;
            private String desc;
            private Integer area;
            private Integer areaOld;
            private String bgColor;
            private String bgPic;
            private Boolean useOldArea;

            public String getName() {
                return name;
            }

            public MobileFrame setName(String name) {
                this.name = name;
                return this;
            }

            public String getValue() {
                return value;
            }

            public MobileFrame setValue(String value) {
                this.value = value;
                return this;
            }

            public Integer getPosition() {
                return position;
            }

            public MobileFrame setPosition(Integer position) {
                this.position = position;
                return this;
            }

            public String getDesc() {
                return desc;
            }

            public MobileFrame setDesc(String desc) {
                this.desc = desc;
                return this;
            }

            public Integer getArea() {
                return area;
            }

            public MobileFrame setArea(Integer area) {
                this.area = area;
                return this;
            }

            public Integer getAreaOld() {
                return areaOld;
            }

            public MobileFrame setAreaOld(Integer areaOld) {
                this.areaOld = areaOld;
                return this;
            }

            public String getBgColor() {
                return bgColor;
            }

            public MobileFrame setBgColor(String bgColor) {
                this.bgColor = bgColor;
                return this;
            }

            public String getBgPic() {
                return bgPic;
            }

            public MobileFrame setBgPic(String bgPic) {
                this.bgPic = bgPic;
                return this;
            }

            public Boolean getUseOldArea() {
                return useOldArea;
            }

            public MobileFrame setUseOldArea(Boolean useOldArea) {
                this.useOldArea = useOldArea;
                return this;
            }
        }
    }


    public static class StudioInfo {

        private Integer status;
        private List<Object> masterList;

        public Integer getStatus() {
            return status;
        }

        public StudioInfo setStatus(Integer status) {
            this.status = status;
            return this;
        }

        public List<Object> getMasterList() {
            return masterList;
        }

        public StudioInfo setMasterList(List<Object> masterList) {
            this.masterList = masterList;
            return this;
        }
    }
}