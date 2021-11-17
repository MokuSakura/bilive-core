package org.mokusakura.bilive.core.client;

import org.mokusakura.bilive.core.event.StatusChangedEvent;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;

import java.util.function.Consumer;

/**
 * @author MokuSakura
 */
public interface ListenableDanmakuClient extends DanmakuClient {
    void addMessageReceivedListener(Consumer<GenericBilibiliMessage> consumer);

    void addStatusChangedListener(Consumer<StatusChangedEvent> consumer);

    boolean removeMessageReceivedListener(Consumer<GenericBilibiliMessage> consumer);

    boolean removeStatusChangedListener(Consumer<StatusChangedEvent> consumer);
}
