package org.mokusakura.bilive.core.factory;

import lombok.extern.log4j.Log4j2;
import org.mokusakura.bilive.core.model.BilibiliWebSocketFrame;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author MokuSakura
 */
@Log4j2
public class PopularityBilibiliMessageFactory implements BilibiliMessageFactory {
    @Override
    public List<GenericBilibiliMessage> create(BilibiliWebSocketFrame frame) {
        log.debug("{}, {}", frame.getWebSocketHeader().getActionType(), Arrays.toString(frame.getWebSocketBody()));
        return new ArrayList<>();
    }
}
