package org.mokusakura.bilive.core.protocol;

import lombok.extern.log4j.Log4j2;
import org.mokusakura.bilive.core.model.BilibiliWebSocketFrame;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MokuSakura
 */
@Log4j2
public class PopularityProtocolResolver implements BilibiliLiveMessageProtocolResolver {
    @Override
    public List<GenericBilibiliMessage> create(BilibiliWebSocketFrame frame) {
//        log.debug("{}, {}", frame.getBilibiliWebSocketHeader().getActionType(),
//                  Arrays.toString(frame.getWebSocketBody()));
        return new ArrayList<>();
    }
}
