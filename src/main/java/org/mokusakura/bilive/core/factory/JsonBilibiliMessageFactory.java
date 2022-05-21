package org.mokusakura.bilive.core.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mokusakura.bilive.core.model.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author MokuSakura
 */

public class JsonBilibiliMessageFactory implements BilibiliMessageFactory {
    private final Map<String, Function<String, GenericBilibiliMessage>> cmdConstructorMap = new HashMap<>();
    private static final Logger log = LogManager.getLogger(JsonBilibiliMessageFactory.class);
    private static final Pattern CMD_PATTERN = Pattern.compile(
            "(?=[^\\\\])\"cmd(?=[^\\\\])\":.*?(?=[^\\\\])\"(.*?)(?=[^\\\\])\"");
    private static final Set<String> unregisteredCmd = new TreeSet<>();


    public static JsonBilibiliMessageFactory createDefault() {
        JsonBilibiliMessageFactory res = new JsonBilibiliMessageFactory();
        res.register("GUARD_BUY", BuyGuardMessage::createFromJson);
        res.register("DANMU_MSG", CommentMessage::createFromJson);
        res.register("LIVE", LiveBeginMessage::createFromJson);
        res.register("PREPARING", LiveEndMessage::createFromJson);
        res.register("SUPER_CHAT_MESSAGE", SuperChatMessage::createFromJson);
        res.register("SEND_GIFT", SendGiftMessage::createFromJson);
        res.register("INTERACT_WORD", InteractWordMessage::createFromJson);
        return res;
    }

    public static JsonBilibiliMessageFactory createDefault(Map<String, Function<String, GenericBilibiliMessage>> map) {
        JsonBilibiliMessageFactory res = createDefault();
        for (Map.Entry<String, Function<String, GenericBilibiliMessage>> entry : map.entrySet()) {
            res.register(entry.getKey(), entry.getValue());
        }
        return res;
    }

    public final int register(String cmd, Function<String, GenericBilibiliMessage> constructor) {
        return cmdConstructorMap.put(cmd, constructor) == null ? 1 : 0;
    }

    public final int register(Map<String, Function<String, GenericBilibiliMessage>> map) {
        int count = 0;
        for (Map.Entry<String, Function<String, GenericBilibiliMessage>> entry : map.entrySet()) {
            count += this.register(entry.getKey(), entry.getValue());
        }
        return count;
    }

    @Override
    public List<GenericBilibiliMessage> create(BilibiliWebSocketFrame frame) {
        if (frame.getBilibiliWebSocketHeader().getDataFormat() !=
                BilibiliWebSocketHeader.DataFormat.PureJson) {
            throw new IllegalArgumentException("Wrong protocol version");
        }
        String json = StandardCharsets.UTF_8.decode(frame.getWebSocketBody()).toString();
        List<GenericBilibiliMessage> res = new ArrayList<>(1);
        GenericBilibiliMessage message = null;
        try {

            Matcher matcher = CMD_PATTERN.matcher(json);

            if (!matcher.find()) {
                return res;
            }
            String cmd = matcher.group(1);
            cmd = Objects.requireNonNullElse(cmd, "");
            Function<String, GenericBilibiliMessage> constructor = cmdConstructorMap.get(cmd);
            if (constructor != null) {
                message = constructor.apply(json);
            } else {
                // TODO performance is affected here
                if (log.isDebugEnabled() && !unregisteredCmd.contains(cmd)) {
                    unregisteredCmd.add(cmd);
                    log.debug("Unregistered cmd found: {}. Json: {}", cmd, json);
                }
            }
            if (message != null) {
                res.add(message);
            }
        } catch (Exception e) {
            log.error("{}\n{}\n{}\n{}", e.getMessage(), e.getClass().getName(), Arrays.toString(e.getStackTrace()),
                      json);

        }
        return res;
    }
}
