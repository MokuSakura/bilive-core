package org.mokusakura.bilive.core.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.mokusakura.bilive.core.factory.BilibiliMessageFactory;
import org.mokusakura.bilive.core.util.CloneUtils;

import java.io.Serializable;

/**
 * <p>
 * Generic Bilibili Message.
 * Using {@link BilibiliMessageFactory#create(BilibiliWebSocketFrame)} )} to create a instance.
 * </p>
 *
 * @author MokuSakura
 */

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public abstract class GenericBilibiliMessage implements Serializable, Cloneable {
    public static final long serializationUID = -3584268486724L;
    private String messageType;
    private Long roomId;
    private Long timestamp;
    private String rawMessage;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return CloneUtils.deepClone(this);
    }
}
