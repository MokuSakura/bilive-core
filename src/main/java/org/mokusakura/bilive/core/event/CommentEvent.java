package org.mokusakura.bilive.core.event;

import org.mokusakura.bilive.core.model.CommentMessage;

/**
 * @author MokuSakura
 */
public class CommentEvent extends MessageReceivedEvent<CommentMessage> {
    public CommentEvent(Class<CommentMessage> messageClass) {
        super(messageClass);
    }
}
