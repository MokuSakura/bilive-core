package org.mokusakura.bilive.core.event;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;

import java.nio.channels.SocketChannel;

/**
 * @author MokuSakura
 */
@Getter
@Setter
@Accessors(chain = true)
public class MessageReceivedEvent extends GenericEvent {
    private final GenericBilibiliMessage message;

    public MessageReceivedEvent(GenericBilibiliMessage message, long roomId, SocketChannel socketChannel) {
        super(roomId, socketChannel);
        this.message = message;
    }
}
