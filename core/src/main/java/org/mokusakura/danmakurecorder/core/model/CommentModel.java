package org.mokusakura.danmakurecorder.core.model;

/**
 * @author MokuSakura
 */
public class CommentModel extends DanmakuModelBase {
    protected String commentText;
    protected Boolean admin;
    protected Boolean vip;
    protected String userGuardLevel;
    protected Integer medalLevel;
    protected String medalName;

    protected CommentModel() {}

    public String getCommentText() {
        return commentText;
    }

    public CommentModel setCommentText(String commentText) {
        this.commentText = commentText;
        return this;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public CommentModel setAdmin(Boolean admin) {
        this.admin = admin;
        return this;
    }

    public Boolean getVip() {
        return vip;
    }

    public CommentModel setVip(Boolean vip) {
        this.vip = vip;
        return this;
    }

    public String getUserGuardLevel() {
        return userGuardLevel;
    }

    public CommentModel setUserGuardLevel(String userGuardLevel) {
        this.userGuardLevel = userGuardLevel;
        return this;
    }

    public Integer getMedalLevel() {
        return medalLevel;
    }

    public CommentModel setMedalLevel(Integer medalLevel) {
        this.medalLevel = medalLevel;
        return this;
    }

    public String getMedalName() {
        return medalName;
    }

    public CommentModel setMedalName(String medalName) {
        this.medalName = medalName;
        return this;
    }
}
