package org.mokusakura.bilive.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author MokuSakura
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class GenericStatusChangedModel extends GenericBilibiliMessage {
    private String status;

    public static class Status {
        public static final String BEGIN = "Begin";
        public static final String PREPARING = "Preparing";
        public static final String DISCONNECT = "Disconnect";
    }

}
