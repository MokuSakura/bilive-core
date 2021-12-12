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
public class WelcomeModel extends GenericBilibiliMessage implements Serializable, Cloneable {
    private static final long serializationUID = -26846535216354653L;
    private Integer uid;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return CloneUtils.deepClone(this);
    }
}
