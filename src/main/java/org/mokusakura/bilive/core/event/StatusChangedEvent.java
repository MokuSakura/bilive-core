package org.mokusakura.bilive.core.event;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.nio.channels.SocketChannel;

/**
 * @author MokuSakura
 */
@Getter
@Setter
@Accessors(chain = true)
public class StatusChangedEvent extends GenericEvent {
    private final String status;

    public StatusChangedEvent(long roomId,
                              SocketChannel socketChannel,
                              String status) {
        super(roomId, socketChannel);
        this.status = status;
    }


}
