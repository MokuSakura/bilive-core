package org.mokusakura.bilive.core.event;

import org.mokusakura.bilive.core.model.SuperChatMessage;

/**
 * @author MokuSakura
 */
public class SuperChatEvent extends MessageReceivedEvent<SuperChatMessage> {
    public SuperChatEvent(Class<SuperChatMessage> messageClass) {
        super(messageClass);
    }
}
