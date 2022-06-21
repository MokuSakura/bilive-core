package org.mokusakura.bilive.core.model;

import com.alibaba.fastjson.JSONObject;
import org.mokusakura.bilive.core.util.CloneUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MokuSakura
 */
public class BuyGuardMessage extends GenericBilibiliMessage implements Serializable, Cloneable {
    private static final long serializationUID = -812373894723948L;
    private final Map<String, Object> additionalProperties = new HashMap<>();
    private Long guardLevel;
    private String username;
    private Long uid;
    private String giftName;
    private Long giftId;
    private Long price;
    private Long num;
    private Long startTime;
    private Long endTime;

    public static BuyGuardMessage createFromJson(String json) {
        JSONObject obj = JSONObject.parseObject(json);
        JSONObject dataObject = obj.getJSONObject("data");
        if (dataObject != null) {
            obj = dataObject;
        }
        BuyGuardMessage res = obj.toJavaObject(BuyGuardMessage.class);
        res.setMessageType(MessageType.GUARD_BUY);
        res.setRawMessage(json);
        return res;
    }

    public BuyGuardMessage() {
        this(MessageType.GUARD_BUY);
    }

    public BuyGuardMessage(String messageType) {
        super(messageType);
    }

    public BuyGuardMessage(String messageType, Long roomId, Long timestamp, String rawMessage, Long guardLevel,
                           String username, Long uid, String giftName, Long giftId, Long price, Long num,
                           Long startTime, Long endTime) {
        super(messageType, roomId, timestamp, rawMessage);
        this.guardLevel = guardLevel;
        this.username = username;
        this.uid = uid;
        this.giftName = giftName;
        this.giftId = giftId;
        this.price = price;
        this.num = num;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return CloneUtils.deepClone(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BuyGuardMessage{");
        sb.append("additionalProperties=").append(additionalProperties);
        sb.append(", guardLevel=").append(guardLevel);
        sb.append(", username='").append(username).append('\'');
        sb.append(", uid=").append(uid);
        sb.append(", giftName='").append(giftName).append('\'');
        sb.append(", giftId=").append(giftId);
        sb.append(", price=").append(price);
        sb.append(", num=").append(num);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append('}');
        return sb.toString();
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public Long getGuardLevel() {
        return guardLevel;
    }

    public BuyGuardMessage setGuardLevel(Long guardLevel) {
        this.guardLevel = guardLevel;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public BuyGuardMessage setUsername(String username) {
        this.username = username;
        return this;
    }

    public Long getUid() {
        return uid;
    }

    public BuyGuardMessage setUid(Long uid) {
        this.uid = uid;
        return this;
    }

    public String getGiftName() {
        return giftName;
    }

    public BuyGuardMessage setGiftName(String giftName) {
        this.giftName = giftName;
        return this;
    }

    public Long getGiftId() {
        return giftId;
    }

    public BuyGuardMessage setGiftId(Long giftId) {
        this.giftId = giftId;
        return this;
    }

    public Long getPrice() {
        return price;
    }

    public BuyGuardMessage setPrice(Long price) {
        this.price = price;
        return this;
    }

    public Long getNum() {
        return num;
    }

    public BuyGuardMessage setNum(Long num) {
        this.num = num;
        return this;
    }

    public Long getStartTime() {
        return startTime;
    }

    public BuyGuardMessage setStartTime(Long startTime) {
        this.startTime = startTime;
        return this;
    }

    public Long getEndTime() {
        return endTime;
    }

    public BuyGuardMessage setEndTime(Long endTime) {
        this.endTime = endTime;
        return this;
    }


}
