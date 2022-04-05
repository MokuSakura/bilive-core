package org.mokusakura.bilive.core.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.*;
import lombok.experimental.Accessors;
import org.mokusakura.bilive.core.util.CloneUtils;

import java.io.Serializable;


/**
 * @author MokuSakura
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
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

    public static CommentMessage createFromJson(String json) {
        JSONArray jsonArray;
        try {
            jsonArray = JSON.parseArray(json);

        } catch (JSONException e) {
            JSONObject jsonObject = JSON.parseObject(json);
            jsonArray = jsonObject.getJSONArray("info");
        }
        CommentMessage commentMessage = new CommentMessage();
        commentMessage.setMessageType(MessageType.COMMENT);
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
    public Object clone() throws CloneNotSupportedException {
        return CloneUtils.deepClone(this);
    }
}
