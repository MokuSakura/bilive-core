package org.mokusakura.bilive.core.exception;

/**
 * @author MokuSakura
 */
public class BilibiliApiException extends RuntimeException {
    private String url;
    private String bilibiliMessage;

    public BilibiliApiException(String url, String message) {
        super("Exception when access " + url);
        this.bilibiliMessage = message;
        this.url = url;
    }

    public String getBilibiliMessage() {
        return bilibiliMessage;
    }

    public BilibiliApiException setBilibiliMessage(String bilibiliMessage) {
        this.bilibiliMessage = bilibiliMessage;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public BilibiliApiException setUrl(String url) {
        this.url = url;
        return this;
    }
}
