package org.mokusakura.bilive.core.factory;

import org.mokusakura.bilive.core.event.GenericEvent;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;
import org.mokusakura.bilive.core.model.MessageType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MokuSakura
 */

@SuppressWarnings("ALL")
public class EventFactoryDispatcher extends Register<String, EventFactory> implements EventFactory {
    public static EventFactoryDispatcher getInstance() {
        return Holder.INSTANCE;
    }

    public static EventFactoryDispatcher newDefault(Map<String, EventFactory> registerMap) {
        EventFactoryDispatcher res = newDefault();
        res.register(registerMap);
        return res;
    }

    public static EventFactoryDispatcher newDefault() {
        Map<String, EventFactory> registerMap = new HashMap<>();
        registerMap.put(MessageType.COMMENT, new CommentEventFactory());
        registerMap.put(MessageType.GIFT_SEND, new SendGiftEventFactory());
        registerMap.put(MessageType.GUARD_BUY, new BuyGuardEventFactory());
        registerMap.put(MessageType.SUPER_CHAT, new SuperChatEventFactory());
        registerMap.put(MessageType.LIVE_START, new LiveBeginEventFactory());
        registerMap.put(MessageType.LIVE_END, new LiveEndEventFactory());

        EventFactoryDispatcher res = new EventFactoryDispatcher();
        res.register(registerMap);
        return res;
    }


    @Override
    public GenericEvent<?> createEvent(Long roomId, GenericBilibiliMessage message) {
        EventFactory factory = map.get(message.getMessageType());
        if (factory == null) {
            return null;
        }
        try {
            return factory.createEvent(roomId, message);
        } catch (ClassCastException e) {
            throw new RuntimeException(String.format(
                    "Wrong registraion for key \"%s\". %s found, but facotry should accept %s as the second parameter expected.",
                    message.getMessageType(), factory.getClass().getName(), message.getClass().getName()));
        }
    }

    static class Holder {
        static final EventFactoryDispatcher INSTANCE = newDefault();
    }
}
