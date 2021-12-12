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
public class GuardBuyModel extends GenericBilibiliMessage implements Serializable, Cloneable {
    private static final long serializationUID = -812373894723948L;
    private final Map<String, Object> additionalProperties = new HashMap<>();
    private Long guardLevel;
    private String username;
    private Long uid;
    private String giftName;
    private Long giftId;
    private Double price;
    private Long num;
    private Long startTime;
    private Long endTime;

    public static GuardBuyModel createFromJson(String json) {
        JSONObject obj = JSONObject.parseObject(json);
        JSONObject dataObject = obj.getJSONObject("data");
        if (dataObject != null) {
            obj = dataObject;
        }
        GuardBuyModel res = obj.toJavaObject(GuardBuyModel.class);
        res.setMessageType(MessageType.GUARD_BUY);
        res.setRawMessage(json);
        return res;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return CloneUtils.deepClone(this);
    }


}
