package org.mokusakura.bilive.core.factory;

import org.mokusakura.bilive.core.event.SuperChatEvent;
import org.mokusakura.bilive.core.model.SuperChatMessage;

/**
 * @author MokuSakura
 */
public class SuperChatEventFactory implements EventFactory<SuperChatMessage, SuperChatEvent> {
    @Override
    public SuperChatEvent createEvent(Long roomId, SuperChatMessage message) {
        SuperChatEvent event = new SuperChatEvent(SuperChatMessage.class);
        event.setRoomId(roomId)
                .setMessage(message);
        return event;
    }
}
