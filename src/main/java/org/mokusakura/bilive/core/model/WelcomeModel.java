package org.mokusakura.bilive.core.model;

/**
 * @author MokuSakura
 */
public class WelcomeModel extends GenericBilibiliMessage {
    private Integer uid;

    public Integer getUid() {
        return uid;
    }

    public WelcomeModel setUid(Integer uid) {
        this.uid = uid;
        return this;
    }
}
