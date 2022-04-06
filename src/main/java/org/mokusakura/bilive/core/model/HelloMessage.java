package org.mokusakura.bilive.core.model;

import org.mokusakura.bilive.core.util.CloneUtils;

import java.io.Serializable;

/**
 * @author MokuSakura
 */
public class HelloMessage implements Serializable, Cloneable {
    public static final long serializationUID = -568728646842L;
    private Long uid;
    private Long roomid;
    private Integer protover;
    private String platform;
    private String clientver;
    private Integer type;
    private String key;

    public static HelloMessage newDefault(Long roomId, String token) {
        return new HelloMessage()
                .setUid(0L)
                .setRoomid(roomId)
                .setProtover(0)
                .setPlatform("web")
                .setClientver("2.6.25")
                .setType(2)
                .setKey(token);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return CloneUtils.deepClone(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HelloMessage{");
        sb.append("uid=").append(uid);
        sb.append(", roomid=").append(roomid);
        sb.append(", protover=").append(protover);
        sb.append(", platform='").append(platform).append('\'');
        sb.append(", clientver='").append(clientver).append('\'');
        sb.append(", type=").append(type);
        sb.append(", key='").append(key).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public Long getUid() {
        return uid;
    }

    public HelloMessage setUid(Long uid) {
        this.uid = uid;
        return this;
    }

    public Long getRoomid() {
        return roomid;
    }

    public HelloMessage setRoomid(Long roomid) {
        this.roomid = roomid;
        return this;
    }

    public Integer getProtover() {
        return protover;
    }

    public HelloMessage setProtover(Integer protover) {
        this.protover = protover;
        return this;
    }

    public String getPlatform() {
        return platform;
    }

    public HelloMessage setPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    public String getClientver() {
        return clientver;
    }

    public HelloMessage setClientver(String clientver) {
        this.clientver = clientver;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public HelloMessage setType(Integer type) {
        this.type = type;
        return this;
    }

    public String getKey() {
        return key;
    }

    public HelloMessage setKey(String key) {
        this.key = key;
        return this;
    }
}
