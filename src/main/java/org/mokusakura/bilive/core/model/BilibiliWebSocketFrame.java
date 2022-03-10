package org.mokusakura.bilive.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.mokusakura.bilive.core.util.CloneUtils;

import java.io.Serializable;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

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
    private final ByteBuffer webSocketBody;

    public static BilibiliWebSocketFrame resolve(ByteBuffer buffer) throws BufferUnderflowException {
        // Not enough to be a complete header
        if (buffer.remaining() < BilibiliWebSocketHeader.HEADER_LENGTH) {
            return null;
        }

        int totalLength = buffer.getInt(BilibiliWebSocketHeader.TOTAL_LENGTH_OFFSET);
        // Not enough to be a complete package
        if (buffer.remaining() < totalLength) {
            // Buffer capacity is not enough, so we allocate a new one with doubled capacity
            if (buffer.limit() == buffer.capacity()) {
                throw new BufferUnderflowException();
            }
            return null;

        }
        BilibiliWebSocketHeader header = BilibiliWebSocketHeader.newInstance(buffer, true);
        ByteBuffer body = buffer.duplicate();
        body.limit(buffer.position() + header.getTotalLength() - header.getHeaderLength());
        buffer.position(buffer.position() + header.getTotalLength() - header.getHeaderLength());
        return new BilibiliWebSocketFrame(header, body);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return CloneUtils.deepClone(this);
    }
}
