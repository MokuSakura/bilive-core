package org.mokusakura.bilive.core.model;

/**
 * @author MokuSakura
 */
public class BilibiliWebSocketFrame {
    private final BilibiliWebSocketHeader bilibiliWebSocketHeader;
    private final byte[] webSocketBody;

    public BilibiliWebSocketFrame(BilibiliWebSocketHeader bilibiliWebSocketHeader, byte[] webSocketBody) {
        this.bilibiliWebSocketHeader = bilibiliWebSocketHeader;
        this.webSocketBody = webSocketBody;
    }

    public BilibiliWebSocketHeader getWebSocketHeader() {
        return bilibiliWebSocketHeader;
    }


    public byte[] getWebSocketBody() {
        return webSocketBody;
    }

}
