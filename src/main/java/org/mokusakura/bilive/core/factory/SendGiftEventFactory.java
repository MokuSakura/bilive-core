package org.mokusakura.bilive.core.factory;

import org.mokusakura.bilive.core.event.SendGiftEvent;
import org.mokusakura.bilive.core.model.SendGiftMessage;

/**
 * @author MokuSakura
 */
public class SendGiftEventFactory implements EventFactory<SendGiftMessage, SendGiftEvent> {
    @Override
    public SendGiftEvent createEvent(Long roomId, SendGiftMessage message) {
        SendGiftEvent event = new SendGiftEvent(SendGiftMessage.class);
        event.setRoomId(roomId)
                .setMessage(message);
        return event;
    }
}
