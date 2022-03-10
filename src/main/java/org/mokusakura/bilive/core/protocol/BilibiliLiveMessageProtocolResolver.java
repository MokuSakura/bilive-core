package org.mokusakura.bilive.core.protocol;

import org.mokusakura.bilive.core.model.BilibiliWebSocketFrame;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;

import java.util.List;

/**
 * @author MokuSakura
 */
public interface BilibiliLiveMessageProtocolResolver {
    List<GenericBilibiliMessage> create(BilibiliWebSocketFrame frame);
}
