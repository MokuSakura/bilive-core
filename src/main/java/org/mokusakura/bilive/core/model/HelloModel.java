package org.mokusakura.bilive.core.model;

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
public class HelloModel implements Serializable, Cloneable {
    public static final long serializationUID = -568728646842L;
    private Long uid;
    private Long roomid;
    private Integer protover;
    private String platform;
    private String clientver;
    private Integer type;
    private String key;

    public static HelloModel newDefault(Long roomId, String token) {
        return HelloModel.builder()
                .uid(0L)
                .roomid(roomId)
                .protover(0)
                .platform("web")
                .clientver("2.6.25")
                .type(2)
                .key(token)
                .build();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return CloneUtils.deepClone(this);
    }
}
