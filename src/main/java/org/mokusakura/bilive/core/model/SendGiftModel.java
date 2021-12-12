package org.mokusakura.bilive.core.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;
import lombok.experimental.Accessors;
import org.mokusakura.bilive.core.util.CloneUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SendGiftModel extends GenericBilibiliMessage implements Serializable, Cloneable {
    private static final Long serializationUID = 918374598163287456L;
    private String action;
    private String batchComboId;
    private BatchComboSend batchComboSend;
    private String beatId;
    private String bizSource;
    private String blindGift;
    private final Map<String, Object> additionalProperties = new HashMap<>();
    private String coinType;
    private Long broadcastId;
    private ComboSend comboSend;
    private Long comboResourcesId;
    private Long comboStayTime;
    private Long comboTotalCoin;
    private Long critProb;
    private Long demarcation;
    private Long dmscore;
    private Long draw;
    private Long effect;
    private String face;
    private Long effectBlock;
    private String giftName;
    private Long giftId;
    private Long giftType;
    private Long gold;
    private Long guardLevel;
    private Boolean isFirst;
    private Long isSpecialBatch;
    private MedalInfo medalInfo;
    private String nameColor;
    private Long magnification;
    private String originalGiftName;
    private Long num;
    private Long price;
    private Long rcost;
    private String rnd;
    private String sendMaster;
    private Long remain;
    private Long silver;
    @JSONField(name = "super")
    private Long super_;
    private Long superBatchGiftNum;
    private Long superGiftNum;
    private String tagImage;
    private String tid;
    private String topList;
    private Long svgaBlock;
    private String uname;
    private Long totalCoin;
    private Long uid;

    public static SendGiftModel createFromJson(String jsonStr) {
        JSONObject obj = JSON.parseObject(jsonStr);
        JSONObject.parseObject(jsonStr, SendGiftModel.class);
        JSONObject dataObject = obj.getJSONObject("data");
        if (dataObject != null) {
            obj = dataObject;
        }
        SendGiftModel res = obj.toJavaObject(SendGiftModel.class);
        res.setMessageType(MessageType.GIFT_SEND)
                .setRawMessage(jsonStr);
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
    public static class BatchComboSend implements Serializable, Cloneable {
        private static final Long serializationUID = -65284685465124L;
        private String action;
        private String batchComboId;
        private final Map<String, Object> additionalProperties = new HashMap<>();
        private String blindGift;
        private Long batchComboNum;
        private String giftName;
        private Long giftId;
        private String sendMaster;
        private Long giftNum;
        private String uname;
        private Long uid;

        @Override
        public Object clone() throws CloneNotSupportedException {
            return CloneUtils.deepClone(this);
        }

        public void setAdditionalProperties(String key, Object object) {
            additionalProperties.put(key, object);
        }
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class ComboSend implements Serializable, Cloneable {
        private static final Long serializationUID = 65768272654657462L;
        private String action;
        private String comboId;
        private final Map<String, Object> additionalProperties = new HashMap<>();
        private Long comboNum;
        private String giftName;
        private Long giftId;
        private String sendMaster;
        private Long giftNum;
        private String uname;
        private Long uid;

        @Override
        public Object clone() throws CloneNotSupportedException {
            return CloneUtils.deepClone(this);
        }

        public void setAdditionalProperties(String key, Object object) {
            additionalProperties.put(key, object);
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
        private static final Long serializationUID = -2658726854658468524L;
        private final Map<String, Object> additionalProperties = new HashMap<>();
        private String anchorUname;
        private Long anchorRoomid;
        private Long guardLevel;
        private Long iconId;
        private Long isLighted;
        private Long medalColor;
        private Long medalColorBorder;
        private Long medalColorEnd;
        private Long medalColorStart;
        private String medalName;
        private String special;
        private Long medalLevel;
        private Long targetId;

        @Override
        public Object clone() throws CloneNotSupportedException {
            return CloneUtils.deepClone(this);
        }

        public void setAdditionalProperties(String key, Object object) {
            additionalProperties.put(key, object);
        }

    }

}
