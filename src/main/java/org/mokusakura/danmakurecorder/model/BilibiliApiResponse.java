package org.mokusakura.danmakurecorder.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author MokuSakura
 */
public class BilibiliApiResponse<T> {
    @JSONField(name = "code")
    private Integer code;
    @JSONField(name = "data")
    private T data;
    @JSONField(name = "message")
    private String message;

    public String getMessage() {
        return message;
    }

    public BilibiliApiResponse<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public BilibiliApiResponse<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public T getData() {
        return data;
    }

    public BilibiliApiResponse<T> setData(T data) {
        this.data = data;
        return this;
    }
}
