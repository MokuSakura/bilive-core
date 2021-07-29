package org.mokusakura.danmakurecorder.core.model;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * <p>
 * Generic Bilibili Message.<br/>
 * Using {@link GenericBilibiliMessage#createFromJson(String)} to create a instance.<br/>
 * Notice that if more inherited classes are defined,
 * call {@link GenericBilibiliMessage#register(String, Function)} to make sure an instance can be created
 * by calling {@link GenericBilibiliMessage#createFromJson(String)}
 * from a JSONObject in which the value of key "cmd" is the first parameter of
 * {@link GenericBilibiliMessage#register(String, Function)}
 * </p>
 *
 * @author MokuSakura
 */
@Slf4j
public abstract class GenericBilibiliMessage {
    private static final Map<String, Function<String, GenericBilibiliMessage>> CMD_MAP = new LinkedHashMap<>();
    private static boolean inited = false;
    protected MessageType messageType;
    protected String rawMessage;

    public static void init() {
        try {
            Class.forName("org.mokusakura.danmakurecorder.core.model.LiveEndModel");
            Class.forName("org.mokusakura.danmakurecorder.core.model.CommentModel");
            Class.forName("org.mokusakura.danmakurecorder.core.model.SendGiftModel");
            Class.forName("org.mokusakura.danmakurecorder.core.model.GuardBuyModel");
            Class.forName("org.mokusakura.danmakurecorder.core.model.SCModel");
            Class.forName("org.mokusakura.danmakurecorder.core.model.LiveBeginModel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        inited = true;
    }

    /**
     * <p>
     * The method is to register a new method to create an instance of {@link GenericBilibiliMessage}.
     * If one cmd value is registered more than once, only the last registered method will remain, and a warning will be logged.
     * </p>
     *
     * @param cmd      The value of key "cmd" in the JSON.
     * @param function The function to create an instance of {@link GenericBilibiliMessage} from json string.
     * @throws IllegalArgumentException If one of the parameters is null.
     */
    public static void register(String cmd, Function<String, GenericBilibiliMessage> function) {
        if (cmd == null && function == null) {
            throw new IllegalArgumentException("null");
        }
        if (CMD_MAP.containsKey(cmd)) {
            log.warn("{} is registered more than once to create an instance of GenericBilibiliMessage", cmd);
        }
        CMD_MAP.put(cmd, function);
    }

    /**
     * <p>
     * To find out whether the cmd is registered to create an instance of {@link GenericBilibiliMessage}.
     * </p>
     *
     * @param cmd cmd
     * @return true if already registered, or false if not registered.
     */
    public static boolean isRegistered(String cmd) {
        return CMD_MAP.containsKey(cmd);
    }

    /**
     * <p>
     * Create an instance of {@link GenericBilibiliMessage} from json string.
     * If there is no key "cmd" in the provided json or the value of "cmd" is not registered by calling
     * {@link GenericBilibiliMessage#register(String, Function)}, this method will return null.
     * Otherwise the return value is depended on the registered function.
     * </p>
     *
     * @param json Json string
     * @return Null or an instance.
     */
    public static GenericBilibiliMessage createFromJson(String json) {
        if (!inited) {
            init();
        }
        var jsonObject = JSONObject.parseObject(json);
        var cmd = jsonObject.getString("cmd");
        cmd = Objects.requireNonNullElse(cmd, "");
        if (cmd.startsWith("DANMU_MSG:")) {
            cmd = "DANMU_MSG";
        }
        var res = Objects.requireNonNullElse(CMD_MAP.get(cmd), unused -> null).apply(json);
        if (res != null) {
            res.setRawMessage(json);
        }
        return res;
    }

    public MessageType getDanmakuType() {
        return messageType;
    }

    public GenericBilibiliMessage setDanmakuType(MessageType messageType) {
        this.messageType = messageType;
        return this;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public GenericBilibiliMessage setMessageType(MessageType messageType) {
        this.messageType = messageType;
        return this;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public GenericBilibiliMessage setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
        return this;
    }
}
