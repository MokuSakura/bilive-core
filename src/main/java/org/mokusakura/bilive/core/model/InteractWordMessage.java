package org.mokusakura.bilive.core.model;

import com.alibaba.fastjson.JSONObject;
import lombok.*;
import lombok.experimental.Accessors;
import org.mokusakura.bilive.core.util.CloneUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class InteractWordMessage extends GenericBilibiliMessage implements Serializable, Cloneable {
    private static final long serialVersionUID = 32342356535445L;
    private final Map<String, Object> additionalProperties = new HashMap<>();
    private Contribution contribution;
    private Integer dmscore;
    private FansMedal fansMedal;
    private List<Integer> identities = new ArrayList<>();
    private Integer isSpread;
    private Integer msgType;
    private Integer roomid;
    private Long score;
    private String spreadDesc;
    private String spreadInfo;
    private Integer tailIcon;
    private Long triggerTime;
    private String unameColor;

    public static InteractWordMessage createFromJson(String json) {
        JSONObject obj = JSONObject.parseObject(json);
        JSONObject dataObject = obj.getJSONObject("data");
        if (dataObject != null) {
            obj = dataObject;
        }
        InteractWordMessage res = obj.toJavaObject(InteractWordMessage.class);
        res.setMessageType(MessageType.INTERACT_WORD);
        res.setRawMessage(json);
        return res;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return CloneUtils.deepClone(this);
    }

    public void setAdditionalProperties(String key, Object object) {
        additionalProperties.put(key, object);
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class FansMedal implements Serializable, Cloneable {
        private static final long serialVersionUID = -35422342356535445L;
        private final Map<String, Object> additionalProperties = new HashMap<>();
        private Integer anchorRoomid;
        private Integer guardLevel;
        private Integer iconId;
        private Integer isLighted;
        private Integer medalColor;
        private Integer medalColorBorder;
        private Integer medalColorEnd;
        private Integer medalColorStart;
        private Integer medalLevel;
        private String medalName;
        private Integer score;
        private String special;
        private Integer targetId;

        @Override
        public Object clone() throws CloneNotSupportedException {
            return CloneUtils.deepClone(this);
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class Contribution implements Serializable, Cloneable {
        private static final long serialVersionUID = -6524654642687L;
        private final Map<String, Object> additionalProperties = new HashMap<>();
        private Integer grade;

        @Override
        public Object clone() throws CloneNotSupportedException {
            return CloneUtils.deepClone(this);
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }


    }
}
