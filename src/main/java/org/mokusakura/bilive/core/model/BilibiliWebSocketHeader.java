package org.mokusakura.bilive.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.mokusakura.bilive.core.util.CloneUtils;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * @author MokuSakura
 */
@Log4j2
@Getter
@ToString
@Builder
@AllArgsConstructor
public class BilibiliWebSocketHeader implements Serializable, Cloneable {
    private static final long serializationUID = -628468546534265L;
    public static final short HEADER_LENGTH = 16;
    public static final int TOTAL_LENGTH_OFFSET = 0;
    public static final int HEADER_LENGTH_OFFSET = 4;
    public static final int PROTOCOL_VERSION_OFFSET = 6;
    public static final int ACTION_OFFSET = 8;
    public static final int SEQUENCE_OFFSET = 12;
    public static final int BODY_OFFSET = 16;
    public static final BilibiliWebSocketHeader HEART_BEAT_HEADER = new BilibiliWebSocketHeader(0,
                                                                                                HEADER_LENGTH,
                                                                                                ProtocolVersion.ClientSend,
                                                                                                ActionType.HeartBeat,
                                                                                                1);
    private final int totalLength;
    private final short headerLength;
    private final short protocolVersion;
    private final int actionType;
    private final int sequence;

    public static BilibiliWebSocketHeader getHeartBeatHeader() {
        return HEART_BEAT_HEADER;
    }

    /**
     * <p>
     * Same as <code>Websocket.newInstance(bytes,true);</code>
     * </p>
     *
     * @param bytes Data
     * @return Decoded Header
     * @see BilibiliWebSocketHeader#newInstance(byte[], boolean)
     */
    public static BilibiliWebSocketHeader newInstance(byte[] bytes) {
        return newInstance(bytes, true);
    }

    /**
     * <p>
     * Since We don't know the meaning of {@link ProtocolVersion} and {@link ActionType} when sending data to the server.
     * We recommend to use the method only to decode the data came from server, which means {@code fromServer} is always true.
     * </p>
     *
     * @param bytes      Data
     * @param fromServer Weather the data came from server.
     * @return Decoded Header
     */
    public static BilibiliWebSocketHeader newInstance(byte[] bytes, boolean fromServer) {
        if (bytes.length < HEADER_LENGTH) {
            throw new IllegalArgumentException("Bytes length less than 16");
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes, 0, HEADER_LENGTH);
        return newInstance(byteBuffer);
    }

    /**
     * <p>
     * This method will not check the length of {@code byteBuffer} and
     * will not change the position of {@code byteBuffer}.
     * </p>
     *
     * @param byteBuffer Data
     * @return Decoded Header
     */
    public static BilibiliWebSocketHeader newInstance(ByteBuffer byteBuffer) {
        return newInstance(byteBuffer, false);
    }

    public static BilibiliWebSocketHeader newInstance(ByteBuffer byteBuffer, boolean consume) {
        int totalLength = consume ? byteBuffer.getInt() : byteBuffer.getInt(TOTAL_LENGTH_OFFSET);
        short headerLength = consume ? byteBuffer.getShort() : byteBuffer.getShort(HEADER_LENGTH_OFFSET);
        short protocolVersionShort = consume ? byteBuffer.getShort() : byteBuffer.getShort(PROTOCOL_VERSION_OFFSET);
        int actionInt = consume ? byteBuffer.getInt() : byteBuffer.getInt(ACTION_OFFSET);
        int sequence = consume ? byteBuffer.getInt() : byteBuffer.getInt(SEQUENCE_OFFSET);
        return new BilibiliWebSocketHeader(totalLength, headerLength, protocolVersionShort, actionInt, sequence);
    }


    public static BilibiliWebSocketHeader newInstance(long bodyLength, short protocolVersion,
                                                      int actionType) {
        if (actionType == ActionType.HeartBeat) {
            return HEART_BEAT_HEADER;
        }
        assert bodyLength < 0xFFFFFFFFL;

        return new BilibiliWebSocketHeader((int) bodyLength, HEADER_LENGTH, protocolVersion, actionType, 1);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return CloneUtils.deepClone(this);
    }

    /**
     * <p>
     * The length of the return is {@link BilibiliWebSocketHeader#HEADER_LENGTH}.
     * </p>
     *
     * @return Encoded header.
     */
    public byte[] array() {
        var headBuffer = ByteBuffer.allocate(HEADER_LENGTH);
        headBuffer.putInt(TOTAL_LENGTH_OFFSET, totalLength);
        headBuffer.putShort(HEADER_LENGTH_OFFSET, headerLength);
        headBuffer.putShort(PROTOCOL_VERSION_OFFSET, protocolVersion);
        headBuffer.putInt(ACTION_OFFSET, actionType);
        headBuffer.putInt(SEQUENCE_OFFSET, sequence);
        return headBuffer.array();
    }

    public static class ActionType {
        public static final int Default = 0;
        public static final int HeartBeat = 2;
        public static final int Popularity = 3;
        public static final int GlobalInfo = 5;
        public static final int Hello = 7;
        public static final int EnterRoom = 8;
    }

    public static class ProtocolVersion {
        public static final short PureJson = 0;
        public static final short Popularity = 1;
        public static final short CompressedBuffer = 2;
        public static final short ClientSend = 1;
        public static final short Default = 1;

    }
}
