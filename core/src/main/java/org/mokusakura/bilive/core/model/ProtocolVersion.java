package org.mokusakura.bilive.core.model;

/**
 * @author MokuSakura
 */
public enum ProtocolVersion {
    PureJson((short) 0), Popularity((short) 1), CompressedBuffer((short) 2), CompressedBuffer2((short) 5), ClientSend(
            (short) 1), Default((short) 1);
    private final short value;

    ProtocolVersion(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }
}
