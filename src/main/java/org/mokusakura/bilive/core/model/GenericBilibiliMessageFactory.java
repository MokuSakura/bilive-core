package org.mokusakura.bilive.core.model;

import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author MokuSakura
 */
@Log4j2
public class GenericBilibiliMessageFactory {
    private static final GenericBilibiliMessageFactory INSTANCE = new GenericBilibiliMessageFactory();
    private final Map<String, Function<String, GenericBilibiliMessage>> CMD_MAP = new LinkedHashMap<>();
    private final Pattern cmdPattern = Pattern.compile(
            "(?=[^\\\\])\"cmd(?=[^\\\\])\":.*?(?=[^\\\\])\"(.*?)(?=[^\\\\])\"");

    private GenericBilibiliMessageFactory() {
        register("GUARD_BUY", GuardBuyModel::new);
        register("DANMU_MSG", CommentModel::new);
        register("LIVE", LiveBeginModel::new);
        register("PREPARING", LiveEndModel::new);
        register("SUPER_CHAT_MESSAGE", SCModel::new);
        register("SEND_GIFT", SendGiftModel::new);
        register("INTERACT_WORD", InteractWord::createInteractWord);

    }

    public static GenericBilibiliMessageFactory getInstance() {
        return INSTANCE;
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
    public void register(String cmd, Function<String, GenericBilibiliMessage> function) {
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
    public boolean isRegistered(String cmd) {
        return CMD_MAP.containsKey(cmd);
    }

    /**
     * <p>
     * Create an instance of {@link GenericBilibiliMessage} from json string.
     * If there is no key "cmd" in the provided json or the value of "cmd" is not registered by calling
     * {@link GenericBilibiliMessageFactory#register(String, Function)}, this method will return null.
     * Otherwise the return value is depended on the registered function.
     * </p>
     *
     * @param json Json string
     * @return Null or an instance.
     */
    public GenericBilibiliMessage create(String json) {
        try {
            Matcher matcher = cmdPattern.matcher(json);
            if (!matcher.find()) {
                return null;
            }
            String cmd = matcher.group(1);
            cmd = Objects.requireNonNullElse(cmd, "");
            if (cmd.startsWith("DANMU_MSG:")) {
                cmd = "DANMU_MSG";
            }
            var res = Objects.requireNonNullElse(CMD_MAP.get(cmd), unused -> null).apply(json);
            if (res != null) {
                res.setRawMessage(json);
            }
            return res;
        } catch (Exception e) {
            log.error("{} {} {} {}", e.getMessage(), e.getClass(), Arrays.toString(e.getStackTrace()), json);
            return null;
        }
    }
}
