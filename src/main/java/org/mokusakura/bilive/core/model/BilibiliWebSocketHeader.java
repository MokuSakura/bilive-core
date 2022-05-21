package org.mokusakura.bilive.core.model;

import org.mokusakura.bilive.core.util.CloneUtils;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * @author MokuSakura
 */
public class BilibiliWebSocketHeader implements Serializable, Cloneable {
    private static final long serializationUID = -628468546534265L;
    public static final short HEADER_LENGTH = 16;
    public static final int TOTAL_LENGTH_OFFSET = 0;
    public static final int HEADER_LENGTH_OFFSET = 4;
    public static final int DATA_FORMAT_VERSION_OFFSET = 6;
    public static final int ACTION_OFFSET = 8;
    public static final int SEQUENCE_OFFSET = 12;
    public static final int BODY_OFFSET = 16;
    public static final BilibiliWebSocketHeader HEART_BEAT_HEADER = new BilibiliWebSocketHeader(0,
                                                                                                HEADER_LENGTH,
                                                                                                DataFormat.ClientSend,
                                                                                                ActionType.HeartBeat,
                                                                                                1);
    private final int totalLength;
    private final short headerLength;
    private final short dataFormat;
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

    public BilibiliWebSocketHeader(int totalLength, short headerLength, short dataFormat, int actionType,
                                   int sequence) {
        this.totalLength = totalLength;
        this.headerLength = headerLength;
        this.dataFormat = dataFormat;
        this.actionType = actionType;
        this.sequence = sequence;
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

    /**
     * <p>
     * Since We don't know the meaning of {@link DataFormat} and {@link ActionType} when sending data to the server.
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

    public static BilibiliWebSocketHeader newInstance(ByteBuffer byteBuffer, boolean consume) {
        int totalLength = consume ? byteBuffer.getInt() : byteBuffer.getInt(
                byteBuffer.position() + TOTAL_LENGTH_OFFSET);
        short headerLength = consume ? byteBuffer.getShort() : byteBuffer.getShort(
                byteBuffer.position() + HEADER_LENGTH_OFFSET);
        short dataFormatShort = consume ? byteBuffer.getShort() : byteBuffer.getShort(
                byteBuffer.position() + DATA_FORMAT_VERSION_OFFSET);
        int actionInt = consume ? byteBuffer.getInt() : byteBuffer.getInt(byteBuffer.position() + ACTION_OFFSET);
        int sequence = consume ? byteBuffer.getInt() : byteBuffer.getInt(byteBuffer.position() + SEQUENCE_OFFSET);
        return new BilibiliWebSocketHeader(totalLength, headerLength, dataFormatShort, actionInt, sequence);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return CloneUtils.deepClone(this);
    }

    public static BilibiliWebSocketHeader newInstance(long bodyLength, short dataFormat,
                                                      int actionType) {
        if (actionType == ActionType.HeartBeat) {
            return HEART_BEAT_HEADER;
        }
        assert bodyLength < 0xFFFFFFFFL;

        return new BilibiliWebSocketHeader((int) bodyLength, HEADER_LENGTH, dataFormat, actionType, 1);
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
        headBuffer.putShort(DATA_FORMAT_VERSION_OFFSET, dataFormat);
        headBuffer.putInt(ACTION_OFFSET, actionType);
        headBuffer.putInt(SEQUENCE_OFFSET, sequence);
        return headBuffer.array();
    }

    public int getTotalLength() {
        return totalLength;
    }

    public short getHeaderLength() {
        return headerLength;
    }

    public short getDataFormat() {
        return dataFormat;
    }

    public int getActionType() {
        return actionType;
    }

    public int getSequence() {
        return sequence;
    }

    public static class ActionType {
        public static final int Default = 0;
        public static final int HeartBeat = 2;
        public static final int Popularity = 3;
        public static final int GlobalInfo = 5;
        public static final int Hello = 7;
        public static final int EnterRoom = 8;
    }

    public static class DataFormat {
        public static final short PureJson = 0;
        public static final short Popularity = 1;
        public static final short CompressedBuffer = 2;
        public static final short ClientSend = 1;
        public static final short Default = 1;

    }
}
