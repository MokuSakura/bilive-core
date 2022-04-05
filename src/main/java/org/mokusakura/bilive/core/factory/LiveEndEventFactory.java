package org.mokusakura.bilive.core.factory;

import org.mokusakura.bilive.core.event.LiveEndEvent;
import org.mokusakura.bilive.core.model.LiveEndMessage;

/**
 * @author MokuSakura
 */
public class LiveEndEventFactory implements EventFactory<LiveEndMessage, LiveEndEvent> {
    @Override
    public LiveEndEvent createEvent(Long roomId, LiveEndMessage message) {
        LiveEndEvent event = new LiveEndEvent(LiveEndMessage.class);
        event.setRoomId(roomId);
        event.setMessage(message);
        return event;
    }
}
