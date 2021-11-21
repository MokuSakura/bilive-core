package org.mokusakura.bilive.core.factory;

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
public class JsonBilibiliMessageFactory implements BilibiliMessageFactory {
    private final Map<String, Function<String, GenericBilibiliMessage>> cmdConstructorMap = new HashMap<>();
    private static final Pattern CMD_PATTERN = Pattern.compile(
            "(?=[^\\\\])\"cmd(?=[^\\\\])\":.*?(?=[^\\\\])\"(.*?)(?=[^\\\\])\"");

    private JsonBilibiliMessageFactory() {}

    public static JsonBilibiliMessageFactory createDefault() {
        JsonBilibiliMessageFactory res = new JsonBilibiliMessageFactory();
        res.register("GUARD_BUY", GuardBuyModel::new);
        res.register("DANMU_MSG", CommentModel::new);
        res.register("LIVE", LiveBeginModel::new);
        res.register("PREPARING", LiveEndModel::new);
        res.register("SUPER_CHAT_MESSAGE", SCModel::new);
        res.register("SEND_GIFT", SendGiftModel::createSendGiftModel);
        res.register("INTERACT_WORD", InteractWord::createInteractWord);
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
        if (frame.getWebSocketHeader().getProtocolVersion() != BilibiliWebSocketHeader.ProtocolVersion.PureJson) {
            throw new IllegalArgumentException("Wrong protocol version");
        }
        String json = new String(frame.getWebSocketBody(), StandardCharsets.UTF_8);
        List<GenericBilibiliMessage> res = new ArrayList<>();
        GenericBilibiliMessage message = null;
        try {

            Matcher matcher = CMD_PATTERN.matcher(json);

            if (!matcher.find()) {
                return null;
            }
            String cmd = matcher.group(1);
            cmd = Objects.requireNonNullElse(cmd, "");
            if (cmd.startsWith("DANMU_MSG:")) {
                cmd = "DANMU_MSG";
            }
            Function<String, GenericBilibiliMessage> constructor = cmdConstructorMap.get(cmd);
            if (constructor != null) {
                message = constructor.apply(json);
            }
            if (message != null) {
                message.setRawMessage(json);
                res.add(message);
            }
            return res;
        } catch (Exception e) {
            log.error("{} {} {} {}", e.getMessage(), e.getClass(), Arrays.toString(e.getStackTrace()), json);
            return null;
        }
    }
}
