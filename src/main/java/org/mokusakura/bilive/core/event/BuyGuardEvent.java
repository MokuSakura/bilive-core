package org.mokusakura.bilive.core.event;

import org.mokusakura.bilive.core.model.BuyGuardMessage;

/**
 * @author MokuSakura
 */
public class BuyGuardEvent extends MessageReceivedEvent<BuyGuardMessage> {

    public BuyGuardEvent(Class<BuyGuardMessage> messageClass) {
        super(messageClass);
    }
}
