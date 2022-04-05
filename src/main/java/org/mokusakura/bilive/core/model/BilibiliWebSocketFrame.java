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

    /**
     * <p>
     * Resolve and consume ByteBuffer.
     * </p>
     *
     * @param buffer ByteBuffer from bilibili-live-ws.
     * @return BilibiliWebSocketFrame if resolve successfully else null.
     * If null, it means that the buffer is not enough to resolve.
     * But you can use the origin buffer to receive more data.
     * @throws BufferUnderflowException if the buffer is not enough to resolve and
     *                                  the capacity of the buffer is not enough to receive the next frame.
     */
    public static BilibiliWebSocketFrame resolve(ByteBuffer buffer) throws BufferUnderflowException {
        // Not enough to be a complete header
        if (buffer.remaining() < BilibiliWebSocketHeader.HEADER_LENGTH) {
            return null;
        }


        int totalLength = buffer.getInt(BilibiliWebSocketHeader.TOTAL_LENGTH_OFFSET);
        // Not enough to be a complete package
        if (buffer.remaining() < totalLength) {
            // Buffer capacity is not enough to receive the next frame
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

    public static BilibiliWebSocketFrame newHeartBeat() {
        BilibiliWebSocketHeader header = new BilibiliWebSocketHeader(BilibiliWebSocketHeader.HEADER_LENGTH,
                                                                     BilibiliWebSocketHeader.HEADER_LENGTH,
                                                                     BilibiliWebSocketHeader.ProtocolVersion.ClientSend,
                                                                     BilibiliWebSocketHeader.ActionType.HeartBeat, 0);
        return new BilibiliWebSocketFrame(header, null);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return CloneUtils.deepClone(this);
    }
}
