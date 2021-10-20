package org.mokusakura.bilive.core.model;

import lombok.extern.log4j.Log4j2;

import java.nio.ByteBuffer;

/**
 * @author MokuSakura
 */
@Log4j2
public class BilibiliWebSocketHeader {
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

    protected BilibiliWebSocketHeader(int bodyLength, short headerLength,
                                      short protocolVersion, int actionType, int sequence) {
        this.totalLength = bodyLength;
        this.headerLength = headerLength;
        this.protocolVersion = protocolVersion;
        this.actionType = actionType;
        this.sequence = sequence;
    }

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
     * We recommend to use the method only to decode the data came from server, which means {@param fromServer} is always true.
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
        int totalLength = byteBuffer.getInt(TOTAL_LENGTH_OFFSET);
        short headerLength = byteBuffer.getShort(HEADER_LENGTH_OFFSET);
        short protocolVersionShort = byteBuffer.getShort(PROTOCOL_VERSION_OFFSET);
        int actionInt = byteBuffer.getInt(ACTION_OFFSET);
        int sequence = byteBuffer.getInt(SEQUENCE_OFFSET);
//        int protocolVersion;
//        if (fromServer) {
//            switch (protocolVersionShort) {
//                case 0:
//                    protocolVersion = ProtocolVersion.PureJson;
//                    break;
//                case 1:
//                    protocolVersion = ProtocolVersion.Popularity;
//                    break;
//                case 2:
//                    protocolVersion = ProtocolVersion.CompressedBuffer;
//                    break;
//                case 5:
//                    protocolVersion = ProtocolVersion.CompressedBuffer2;
//                    log.debug(Arrays.toString(byteBuffer.array()));
//                    break;
//                //TODO It seems another protocol version is used by Bilibili now.
//                default:
//                    throw new RuntimeException("Unknown Protocol Version" + protocolVersionShort);
//            }
//        } else {
//            protocolVersion = ProtocolVersion.Default;
//        }
//        int actionType;
//        switch (actionInt) {
//            case 2:
//                actionType = ActionType.HeartBeat;
//                break;
//            case 3:
//                actionType = ActionType.Popularity;
//                break;
//            case 5:
//                actionType = ActionType.GlobalInfo;
//                break;
//            case 7:
//                actionType = ActionType.Hello;
//                break;
//            case 8:
//                actionType = ActionType.EnterRoom;
//                break;
//            default:
//                throw new RuntimeException("Unknown ActionType");
//        }
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

    public short getProtocolVersion() {
        return protocolVersion;
    }

    public int getActionType() {
        return actionType;
    }

    public long getTotalLength() {
        return totalLength;
    }

    public int getHeaderLength() {
        return headerLength;
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

    public long getSequence() {
        return sequence;
    }

    public static class ProtocolVersion {
        public static final short PureJson = 0;
        public static final short Popularity = 1;
        public static final short CompressedBuffer = 2;
        public static final short ClientSend = 1;
        public static final short Default = 1;

    }
}
