package org.mokusakura.bilive.core.factory;

import org.mokusakura.bilive.core.model.BilibiliWebSocketFrame;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;

import java.util.List;

/**
 * @author MokuSakura
 */
public interface BilibiliMessageFactory {
    List<GenericBilibiliMessage> create(BilibiliWebSocketFrame frame);
}
