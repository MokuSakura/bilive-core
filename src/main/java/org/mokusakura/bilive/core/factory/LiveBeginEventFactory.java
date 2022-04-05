package org.mokusakura.bilive.core.factory;

import org.mokusakura.bilive.core.event.LiveBeginEvent;
import org.mokusakura.bilive.core.model.LiveBeginMessage;

/**
 * @author MokuSakura
 */
public class LiveBeginEventFactory implements EventFactory<LiveBeginMessage, LiveBeginEvent> {
    @Override
    public LiveBeginEvent createEvent(Long roomId, LiveBeginMessage message) {
        LiveBeginEvent event = new LiveBeginEvent(LiveBeginMessage.class);
        event.setRoomId(roomId);
        event.setMessage(message);
        return event;
    }
}
