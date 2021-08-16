package org.mokusakura.bilive.core.model;

/**
 * @author MokuSakura
 */
public class HelloModel {
    private Long uid;
    private Long roomid;
    private Integer protover;
    private String platform;
    private String clientver;
    private Integer type;
    private String key;

    public static HelloModel newDefault(Long roomId, String token) {
        HelloModel res = new HelloModel();
        res.setUid(0L)
                .setRoomid(roomId)
                .setProtover(0)
                .setPlatform("web")
                .setClientver("2.6.25")
                .setType(2)
                .setKey(token);
        return res;
    }

    public Long getUid() {
        return uid;
    }

    public HelloModel setUid(Long uid) {
        this.uid = uid;
        return this;
    }

    public Long getRoomid() {
        return roomid;
    }

    public HelloModel setRoomid(Long roomid) {
        this.roomid = roomid;
        return this;
    }

    public Integer getProtover() {
        return protover;
    }

    public HelloModel setProtover(Integer protover) {
        this.protover = protover;
        return this;
    }

    public String getPlatform() {
        return platform;
    }

    public HelloModel setPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    public String getClientver() {
        return clientver;
    }

    public HelloModel setClientver(String clientver) {
        this.clientver = clientver;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public HelloModel setType(Integer type) {
        this.type = type;
        return this;
    }

    public String getKey() {
        return key;
    }

    public HelloModel setKey(String token) {
        this.key = token;
        return this;
    }
}
