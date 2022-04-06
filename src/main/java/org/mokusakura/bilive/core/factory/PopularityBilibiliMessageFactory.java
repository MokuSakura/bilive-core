package org.mokusakura.bilive.core.factory;

import org.mokusakura.bilive.core.model.BilibiliWebSocketFrame;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MokuSakura
 */
public class PopularityBilibiliMessageFactory implements BilibiliMessageFactory {
    @Override
    public List<GenericBilibiliMessage> create(BilibiliWebSocketFrame frame) {
//        log.debug("{}, {}", frame.getBilibiliWebSocketHeader().getActionType(),
//                  Arrays.toString(frame.getWebSocketBody()));
        return new ArrayList<>();
    }
}
