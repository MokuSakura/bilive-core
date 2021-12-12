package org.mokusakura.bilive.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.mokusakura.bilive.core.util.CloneUtils;

import java.io.Serializable;

/**
 * @author MokuSakura
 */
@Getter
@ToString
@Builder
@AllArgsConstructor

public class BilibiliWebSocketFrame implements Serializable, Cloneable {
    private static final long serializationUID = -52465426546542L;
    private final BilibiliWebSocketHeader bilibiliWebSocketHeader;
    private final byte[] webSocketBody;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return CloneUtils.deepClone(this);
    }
}
