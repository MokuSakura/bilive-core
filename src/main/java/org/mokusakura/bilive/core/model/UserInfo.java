package org.mokusakura.bilive.core.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author MokuSakura
 */
public class UserInfo {
    @JSONField(name = "info")
    private InfoClass info;

    public InfoClass getInfo() {
        return info;
    }

    public UserInfo setInfo(InfoClass info) {
        this.info = info;
        return this;
    }

    public static class InfoClass {
        @JSONField(name = "uname")
        private String name;

        public String getName() {
            return name;
        }

        public InfoClass setName(String name) {
            this.name = name;
            return this;
        }
    }
}
