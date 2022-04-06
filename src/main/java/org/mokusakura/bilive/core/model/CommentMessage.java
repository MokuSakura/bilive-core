package org.mokusakura.bilive.core.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.mokusakura.bilive.core.util.CloneUtils;

import java.io.Serializable;


/**
 * @author MokuSakura
 */

public class CommentMessage extends GenericBilibiliMessage implements Serializable, Cloneable {
    public static final long serializationUID = 23462346234354676L;
    private String commentText;
    private Boolean admin;
    private Boolean vip;
    private Integer guardLevel;
    private Integer medalLevel;
    private String medalName;
    private Long uid;
    private String username;
    private Long timestamp;

    public CommentMessage(String messageType) {
        super(messageType);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return CloneUtils.deepClone(this);
    }

    public CommentMessage(String messageType, Long roomId, Long timestamp, String rawMessage, String commentText,
                          Boolean admin, Boolean vip, Integer guardLevel, Integer medalLevel, String medalName,
                          Long uid, String username, Long timestamp1) {
        super(messageType, roomId, timestamp, rawMessage);
        this.commentText = commentText;
        this.admin = admin;
        this.vip = vip;
        this.guardLevel = guardLevel;
        this.medalLevel = medalLevel;
        this.medalName = medalName;
        this.uid = uid;
        this.username = username;
        this.timestamp = timestamp1;
    }

    public static CommentMessage createFromJson(String json) {
        JSONArray jsonArray;
        try {
            jsonArray = JSON.parseArray(json);

        } catch (JSONException e) {
            JSONObject jsonObject = JSON.parseObject(json);
            jsonArray = jsonObject.getJSONArray("info");
        }
        CommentMessage commentMessage = new CommentMessage(MessageType.COMMENT);
        commentMessage.uid = jsonArray.getJSONArray(2).getLong(0);
        commentMessage.username = jsonArray.getJSONArray(2).getString(1);
        commentMessage.timestamp = jsonArray.getJSONObject(9).getLong("ts");
        commentMessage.commentText = jsonArray.getString(1);
        commentMessage.admin = "1".equals(jsonArray.getJSONArray(2).getString(2));
        commentMessage.vip = "1".equals(jsonArray.getJSONArray(2).getString(3));
        commentMessage.guardLevel = jsonArray.getInteger(7);
        var medalArray = jsonArray.getJSONArray(3);
        commentMessage.setRawMessage(json);
        if (medalArray != null && medalArray.size() > 0) {
            commentMessage.medalLevel = jsonArray.getJSONArray(3).getInteger(0);
            commentMessage.medalName = jsonArray.getJSONArray(3).getString(1);
        }
        return commentMessage;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CommentMessage{");
        sb.append("commentText='").append(commentText).append('\'');
        sb.append(", admin=").append(admin);
        sb.append(", vip=").append(vip);
        sb.append(", guardLevel=").append(guardLevel);
        sb.append(", medalLevel=").append(medalLevel);
        sb.append(", medalName='").append(medalName).append('\'');
        sb.append(", uid=").append(uid);
        sb.append(", username='").append(username).append('\'');
        sb.append(", timestamp=").append(timestamp);
        sb.append('}');
        return sb.toString();
    }

    public String getCommentText() {
        return commentText;
    }

    public CommentMessage setCommentText(String commentText) {
        this.commentText = commentText;
        return this;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public CommentMessage setAdmin(Boolean admin) {
        this.admin = admin;
        return this;
    }

    public Boolean getVip() {
        return vip;
    }

    public CommentMessage setVip(Boolean vip) {
        this.vip = vip;
        return this;
    }

    public Integer getGuardLevel() {
        return guardLevel;
    }

    public CommentMessage setGuardLevel(Integer guardLevel) {
        this.guardLevel = guardLevel;
        return this;
    }

    public Integer getMedalLevel() {
        return medalLevel;
    }

    public CommentMessage setMedalLevel(Integer medalLevel) {
        this.medalLevel = medalLevel;
        return this;
    }

    public String getMedalName() {
        return medalName;
    }

    public CommentMessage setMedalName(String medalName) {
        this.medalName = medalName;
        return this;
    }

    public Long getUid() {
        return uid;
    }

    public CommentMessage setUid(Long uid) {
        this.uid = uid;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public CommentMessage setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public CommentMessage setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
