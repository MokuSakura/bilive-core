package org.mokusakura.danmakurecorder.exception;

/**
 * @author MokuSakura
 */
public class NoNetworkConnectionException extends Exception {
    public NoNetworkConnectionException() {
    }

    public NoNetworkConnectionException(String message) {
        super(message);
    }

    public NoNetworkConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoNetworkConnectionException(Throwable cause) {
        super(cause);
    }

    public NoNetworkConnectionException(String message, Throwable cause, boolean enableSuppression,
                                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
