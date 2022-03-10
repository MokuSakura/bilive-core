package org.mokusakura.bilive.core.event;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public abstract class GenericEvent {

    private final long roomId;
    private final SocketChannel socketChannel;
}
