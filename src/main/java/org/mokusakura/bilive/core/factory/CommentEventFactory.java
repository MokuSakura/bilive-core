package org.mokusakura.bilive.core.factory;

import org.mokusakura.bilive.core.event.CommentEvent;
import org.mokusakura.bilive.core.model.CommentMessage;

/**
 * @author MokuSakura
 */
public class CommentEventFactory implements EventFactory<CommentMessage, CommentEvent> {
    @Override
    public CommentEvent createEvent(Long roomId, CommentMessage message) {
        CommentEvent res = new CommentEvent(CommentMessage.class);
        res.setMessage(message)
                .setRoomId(roomId);

        return res;
    }
}
