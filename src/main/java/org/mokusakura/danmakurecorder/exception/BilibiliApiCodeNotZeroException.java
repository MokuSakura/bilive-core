package org.mokusakura.danmakurecorder.exception;

/**
 * @author MokuSakura
 */
public class BilibiliApiCodeNotZeroException extends BilibiliApiException {
    private Integer code;

    public BilibiliApiCodeNotZeroException(String url, String bilibiliMessage, Integer code) {
        super(url, bilibiliMessage);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public BilibiliApiCodeNotZeroException setCode(Integer code) {
        this.code = code;
        return this;
    }
}
