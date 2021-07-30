package org.mokusakura.bilive.core.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author MokuSakura
 */
public abstract class AbstractDanmaku extends GenericBilibiliMessage {
    protected Integer uid;
    protected String username;
    protected Long timestamp;
    private static final Map<Integer, String> GUARD_LEVEL_NAME_MAP = new LinkedHashMap<>();
    private static final Map<String, Integer> GUARD_NAME_LEVEL_MAP = new LinkedHashMap<>();

    static {
        GUARD_LEVEL_NAME_MAP.put(1, "总督");
        GUARD_LEVEL_NAME_MAP.put(2, "提督");
        GUARD_LEVEL_NAME_MAP.put(3, "舰长");
        GUARD_NAME_LEVEL_MAP.put("总督", 1);
        GUARD_NAME_LEVEL_MAP.put("提督", 2);
        GUARD_NAME_LEVEL_MAP.put("舰长", 3);
    }

    public static String mapGuardLevelToName(Integer guardLevel) {
        return GUARD_LEVEL_NAME_MAP.get(guardLevel);
    }

    public static Integer mapGuardNameToLevel(String guardName) {
        return GUARD_NAME_LEVEL_MAP.get(guardName);
    }

    public Integer getUid() {
        return uid;
    }

    public AbstractDanmaku setUid(Integer uid) {
        this.uid = uid;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public AbstractDanmaku setUsername(String username) {
        this.username = username;
        return this;
    }

    public MessageType getDanmakuType() {
        return messageType;
    }

    public AbstractDanmaku setDanmakuType(MessageType messageType) {
        this.messageType = messageType;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public AbstractDanmaku setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
