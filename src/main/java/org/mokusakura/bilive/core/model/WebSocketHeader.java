package org.mokusakura.bilive.core.model;

import lombok.extern.log4j.Log4j2;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @author MokuSakura
 */
@Log4j2
public class WebSocketHeader {
    public static final short HEADER_LENGTH = 16;
    public static final int TOTAL_LENGTH_OFFSET = 0;
    public static final int HEADER_LENGTH_OFFSET = 4;
    public static final int PROTOCOL_VERSION_OFFSET = 6;
    public static final int ACTION_OFFSET = 8;
    public static final int SEQUENCE_OFFSET = 12;
    public static final int BODY_OFFSET = 16;
    public static final WebSocketHeader HEART_BEAT_HEADER = new WebSocketHeader(0,
                                                                                HEADER_LENGTH,
                                                                                ProtocolVersion.ClientSend,
                                                                                ActionType.HeartBeat,
                                                                                1);
    private final int totalLength;
    private final short headerLength;
    private final ProtocolVersion protocolVersion;
    private final ActionType actionType;
    private final int sequence;

    public enum ActionType {
        Default(0), HeartBeat(2), Popularity(3), GlobalInfo(5), Hello(7), EnterRoom(8);
        private final int value;

        ActionType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum ProtocolVersion {
        PureJson((short) 0), Popularity((short) 1), CompressedBuffer((short) 2), CompressedBuffer2(
                (short) 5), ClientSend(
                (short) 1), Default((short) 1);
        private final short value;

        ProtocolVersion(short value) {
            this.value = value;
        }

        public short getValue() {
            return value;
        }
    }

    protected WebSocketHeader(int bodyLength, short headerLength,
                              ProtocolVersion protocolVersion, ActionType actionType, int sequence) {
        this.totalLength = bodyLength;
        this.headerLength = headerLength;
        this.protocolVersion = protocolVersion;
        this.actionType = actionType;
        this.sequence = sequence;
    }

    public static WebSocketHeader getHeartBeatHeader() {
        return HEART_BEAT_HEADER;
    }

    /**
     * <p>
     * Same as <code>Websocket.newInstance(bytes,true);</code>
     * </p>
     *
     * @param bytes Data
     * @return Decoded Header
     * @see WebSocketHeader#newInstance(byte[], boolean)
     */
    public static WebSocketHeader newInstance(byte[] bytes) {
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
    public static WebSocketHeader newInstance(byte[] bytes, boolean fromServer) {
        if (bytes.length < HEADER_LENGTH) {
            throw new IllegalArgumentException("Bytes length less than 16");
        }
        var byteBuffer = ByteBuffer.wrap(bytes, 0, HEADER_LENGTH);
        var totalLength = byteBuffer.getInt(TOTAL_LENGTH_OFFSET);
        var headerLength = byteBuffer.getShort(HEADER_LENGTH_OFFSET);
        var protocolVersionShort = byteBuffer.getShort(PROTOCOL_VERSION_OFFSET);
        var actionInt = byteBuffer.getInt(ACTION_OFFSET);
        var sequence = byteBuffer.getInt(SEQUENCE_OFFSET);
        ProtocolVersion protocolVersion;
        if (fromServer) {
            switch (protocolVersionShort) {
                case 0:
                    protocolVersion = ProtocolVersion.PureJson;
                    break;
                case 1:
                    protocolVersion = ProtocolVersion.Popularity;
                    break;
                case 2:
                    protocolVersion = ProtocolVersion.CompressedBuffer;
                    break;
                case 5:
                    protocolVersion = ProtocolVersion.CompressedBuffer2;
                    log.debug(Arrays.toString(byteBuffer.array()));
                    break;
                //TODO It seems another protocol version is used by Bilibili now.
                default:
                    throw new RuntimeException("Unknown Protocol Version" + protocolVersionShort);
            }
        } else {
            protocolVersion = ProtocolVersion.Default;
        }
        ActionType actionType;
        switch (actionInt) {
            case 2:
                actionType = ActionType.HeartBeat;
                break;
            case 3:
                actionType = ActionType.Popularity;
                break;
            case 5:
                actionType = ActionType.GlobalInfo;
                break;
            case 7:
                actionType = ActionType.Hello;
                break;
            case 8:
                actionType = ActionType.EnterRoom;
                break;
            default:
                throw new RuntimeException("Unknown ActionType");
        }
        return new WebSocketHeader(totalLength, headerLength, protocolVersion, actionType, sequence);
    }

    public static WebSocketHeader newInstance(long bodyLength, ProtocolVersion protocolVersion, ActionType actionType) {
        if (actionType == ActionType.HeartBeat) {
            return HEART_BEAT_HEADER;
        }
        assert bodyLength < 0xFFFFFFFFL;

        return new WebSocketHeader((int) bodyLength, HEADER_LENGTH, protocolVersion, actionType, 1);
    }

    public long getTotalLength() {
        return totalLength;
    }

    public int getHeaderLength() {
        return headerLength;
    }

    public ProtocolVersion getProtocolVersion() {
        return protocolVersion;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public long getSequence() {
        return sequence;
    }

    /**
     * <p>
     * The length of the return is {@link WebSocketHeader#HEADER_LENGTH}.
     * </p>
     *
     * @return Encoded header.
     */
    public byte[] array() {
        var headBuffer = ByteBuffer.allocate(HEADER_LENGTH);
        headBuffer.putInt(TOTAL_LENGTH_OFFSET, totalLength);
        headBuffer.putShort(HEADER_LENGTH_OFFSET, headerLength);
        headBuffer.putShort(PROTOCOL_VERSION_OFFSET, protocolVersion.getValue());
        headBuffer.putInt(ACTION_OFFSET, actionType.getValue());
        headBuffer.putInt(SEQUENCE_OFFSET, sequence);
        return headBuffer.array();
    }
}
