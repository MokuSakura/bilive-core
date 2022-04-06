package org.mokusakura.bilive.core.model;

/**
 * @author MokuSakura
 */

public abstract class StatusChangedMessage extends GenericBilibiliMessage {
    private String status;

    public StatusChangedMessage(String messageType, String status) {
        super(messageType);
        this.status = status;
    }

    public StatusChangedMessage(String messageType, Long roomId, Long timestamp, String rawMessage, String status) {
        super(messageType, roomId, timestamp, rawMessage);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public StatusChangedMessage setStatus(String status) {
        this.status = status;
        return this;
    }

    public static class Status {
        public static final String BEGIN = "Begin";
        public static final String PREPARING = "Preparing";
        public static final String DISCONNECT = "Disconnect";
    }

}
