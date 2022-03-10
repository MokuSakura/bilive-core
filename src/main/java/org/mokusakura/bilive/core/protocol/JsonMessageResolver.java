package org.mokusakura.bilive.core.protocol;

import lombok.extern.log4j.Log4j2;
import org.mokusakura.bilive.core.model.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author MokuSakura
 */
@Log4j2
public class JsonMessageResolver implements BilibiliLiveMessageProtocolResolver {
    private final Map<String, Function<String, GenericBilibiliMessage>> cmdConstructorMap = new HashMap<>();
    private static final Pattern CMD_PATTERN = Pattern.compile(
            "(?=[^\\\\])\"cmd(?=[^\\\\])\":.*?(?=[^\\\\])\"(.*?)(?=[^\\\\])\"");


    public static JsonMessageResolver createDefault() {
        JsonMessageResolver res = new JsonMessageResolver();
        res.register("GUARD_BUY", GuardBuyModel::createFromJson);
        res.register("DANMU_MSG", CommentModel::createFromJson);
        res.register("LIVE", LiveBeginModel::createFromJson);
        res.register("PREPARING", LiveEndModel::createFromJson);
        res.register("SUPER_CHAT_MESSAGE", SCModel::createFromJson);
        res.register("SEND_GIFT", SendGiftModel::createFromJson);
        res.register("INTERACT_WORD", InteractWord::createFromJson);
        return res;
    }

    public static JsonMessageResolver createDefault(Map<String, Function<String, GenericBilibiliMessage>> map) {
        JsonMessageResolver res = createDefault();
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
        if (frame.getBilibiliWebSocketHeader().getProtocolVersion() !=
                BilibiliWebSocketHeader.ProtocolVersion.PureJson) {
            throw new IllegalArgumentException("Wrong protocol version");
        }
        String json = new String(frame.getWebSocketBody().array(), StandardCharsets.UTF_8);
        List<GenericBilibiliMessage> res = new ArrayList<>();
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
            }
            if (message != null) {
                res.add(message);
            }
            return res;
        } catch (Exception e) {
            log.error("{} {} {} {}", e.getMessage(), e.getClass(), Arrays.toString(e.getStackTrace()), json);
            return null;
        }
    }
}
