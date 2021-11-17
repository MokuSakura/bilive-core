package org.mokusakura.bilive.core.model;

import com.alibaba.fastjson.JSONObject;


/**
 * @author MokuSakura
 */
public class CommentModel extends AbstractDanmaku {
    protected String commentText;
    protected Boolean admin;
    protected Boolean vip;


    protected Integer guardLevel;
    protected Integer medalLevel;
    protected String medalName;
    protected String guardName;

    public CommentModel(String json) {
        var obj = JSONObject.parseObject(json);
        super.messageType = MessageType.Comment;
        super.uid = obj.getJSONArray("info").getJSONArray(2).getLong(0);
        super.username = obj.getJSONArray("info").getJSONArray(2).getString(1);
        super.timestamp = obj.getJSONArray("info").getJSONObject(9).getLong("ts");

        this.commentText = obj.getJSONArray("info").getString(1);
        this.admin = "1".equals(obj.getJSONArray("info").getJSONArray(2).getString(2));
        this.vip = "1".equals(obj.getJSONArray("info").getJSONArray(2).getString(3));
        this.guardLevel = obj.getJSONArray("info").getInteger(7);
        this.guardName = mapGuardLevelToName(guardLevel);
        var medalArray = obj.getJSONArray("info").getJSONArray(3);
        if (medalArray != null && medalArray.size() > 0) {
            this.medalLevel = obj.getJSONArray("info").getJSONArray(3).getInteger(0);
            this.medalName = obj.getJSONArray("info").getJSONArray(3).getString(1);
        }
    }

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

    public Integer getUserGuardLevel() {
        return guardLevel;
    }

    public CommentModel setUserGuardLevel(Integer guardLevel) {
        this.guardLevel = guardLevel;
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
