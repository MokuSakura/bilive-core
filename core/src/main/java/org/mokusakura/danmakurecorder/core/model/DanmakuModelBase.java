package org.mokusakura.danmakurecorder.core.model;

import com.alibaba.fastjson.JSONObject;

import java.util.Objects;

/**
 * @author MokuSakura
 */
public class DanmakuModelBase {
    protected Integer uid;
    protected String username;
    protected DanmakuType danmakuType;
    protected Long timestamp;

    protected DanmakuModelBase() {

    }

    public static DanmakuModelBase createFromJson(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        String cmd = jsonObject.getString("cmd");
        cmd = Objects.requireNonNullElse(cmd, "");
        if (cmd.startsWith("DANMU_MSG:")) {
            cmd = "DANMU_MSG";
        }
        switch ()
    }

    public Integer getUid() {
        return uid;
    }

    public DanmakuModelBase setUid(Integer uid) {
        this.uid = uid;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public DanmakuModelBase setUsername(String username) {
        this.username = username;
        return this;
    }

    public DanmakuType getDanmakuType() {
        return danmakuType;
    }

    public DanmakuModelBase setDanmakuType(DanmakuType danmakuType) {
        this.danmakuType = danmakuType;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public DanmakuModelBase setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
