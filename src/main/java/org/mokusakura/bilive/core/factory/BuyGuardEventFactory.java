package org.mokusakura.bilive.core.factory;

import org.mokusakura.bilive.core.event.BuyGuardEvent;
import org.mokusakura.bilive.core.model.BuyGuardMessage;

/**
 * @author MokuSakura
 */
public class BuyGuardEventFactory implements EventFactory<BuyGuardMessage, BuyGuardEvent> {
    @Override
    public BuyGuardEvent createEvent(Long roomId, BuyGuardMessage message) {
        BuyGuardEvent event = new BuyGuardEvent(BuyGuardMessage.class);
        event.setMessage(message)
                .setRoomId(roomId);
        return event;
    }
}
