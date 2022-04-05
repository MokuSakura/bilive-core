package org.mokusakura.bilive.core.event;

import org.mokusakura.bilive.core.model.SendGiftMessage;

/**
 * @author MokuSakura
 */
public class SendGiftEvent extends MessageReceivedEvent<SendGiftMessage> {
    public SendGiftEvent(Class<SendGiftMessage> messageClass) {
        super(messageClass);
    }
}
