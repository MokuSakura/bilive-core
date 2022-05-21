package org.mokusakura.bilive.core.event;

import org.mokusakura.bilive.core.model.StatusChangedMessage;

/**
 * @author MokuSakura
 */

public class DisconnectedEvent extends StatusChangedEvent<StatusChangedMessage> {
    private Long firstTryTime;
    private Long lastTryTime;
    private Integer tryCount;
    private Exception exception;

    public DisconnectedEvent() {
        super(StatusChangedMessage.class);
    }

    @Override
    public Exception getException() {
        return exception;
    }

    public DisconnectedEvent setException(Exception exception) {
        this.exception = exception;
        return this;
    }

    public Long getFirstTryTime() {
        return firstTryTime;
    }

    public DisconnectedEvent setFirstTryTime(Long firstTryTime) {
        this.firstTryTime = firstTryTime;
        return this;
    }

    public Long getLastTryTime() {
        return lastTryTime;
    }

    public DisconnectedEvent setLastTryTime(Long lastTryTime) {
        this.lastTryTime = lastTryTime;
        return this;
    }

    public Integer getTryCount() {
        return tryCount;
    }

    public DisconnectedEvent setTryCount(Integer tryCount) {
        this.tryCount = tryCount;
        return this;
    }
}
