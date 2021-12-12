package org.mokusakura.bilive.core.model;

import com.alibaba.fastjson.JSONObject;
import lombok.*;
import lombok.experimental.Accessors;
import org.mokusakura.bilive.core.util.CloneUtils;

import java.io.Serializable;
import java.util.HashMap;
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
public class SCModel extends GenericBilibiliMessage implements Serializable, Cloneable {


    private final static long serialVersionUID = 8275693861288435948L;
    private final Map<String, Object> additionalProperties = new HashMap<>();
    private String backgroundBottomColor;
    private String backgroundColor;
    private String backgroundColorEnd;
    private String backgroundColorStart;
    private String backgroundIcon;
    private String backgroundImage;
    private String backgroundPriceColor;
    private Double colorPoint;
    private Long dmscore;
    private Long endTime;
    private Gift gift;
    private Long id;
    private Long isRanked;
    private String isSendAudit;
    private MedalInfo medalInfo;
    private String message;
    private String messageFontColor;
    private String messageTrans;
    private Long price;
    private Long rate;
    private Long startTime;
    private Long time;
    private String token;
    private Long transMark;
    private Long ts;
    private Long uid;
    private UserInfo userInfo;

    public static SCModel createFromJson(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONObject dataObject = jsonObject.getJSONObject("data");
        if (dataObject != null) {
            jsonObject = dataObject;
        }
        SCModel res = jsonObject.toJavaObject(SCModel.class);
        res.setRawMessage(json);
        res.setMessageType(MessageType.SUPER_CHAT);
        return res;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return CloneUtils.deepClone(this);
    }

    public void addAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)

    public static class Gift implements Serializable, Cloneable {
        private final static long serialVersionUID = 1540792054227797718L;
        private final Map<String, Object> additionalProperties = new HashMap<>();
        private Long giftId;
        private String giftName;
        private Long num;

        public void addAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return CloneUtils.deepClone(this);
        }
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class MedalInfo implements Serializable, Cloneable {
        private final static long serialVersionUID = 3204661273937089018L;
        private final Map<String, Object> additionalProperties = new HashMap<>();
        private Long anchorRoomid;
        private String anchorUname;
        private Long guardLevel;
        private Long iconId;
        private Long isLighted;
        private String medalColor;
        private Long medalColorBorder;
        private Long medalColorEnd;
        private Long medalColorStart;
        private Long medalLevel;
        private String medalName;
        private String special;
        private Long targetId;

        public void addAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return CloneUtils.deepClone(this);
        }
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class UserInfo implements Serializable, Cloneable {
        private final static long serialVersionUID = 5294051665651419042L;
        private final Map<String, Object> additionalProperties = new HashMap<>();
        private String face;
        private String faceFrame;
        private Long guardLevel;
        private Long isMainVip;
        private Long isSvip;
        private Long isVip;
        private String levelColor;
        private Long manager;
        private String nameColor;
        private String title;
        private String uname;
        private Long userLevel;

        public void addAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return CloneUtils.deepClone(this);
        }
    }

}

