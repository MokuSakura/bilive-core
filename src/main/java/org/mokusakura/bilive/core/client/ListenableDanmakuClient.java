package org.mokusakura.bilive.core.client;

import org.mokusakura.bilive.core.event.MessageReceivedEvent;
import org.mokusakura.bilive.core.event.StatusChangedEvent;

import java.util.function.Consumer;

/**
 * @author MokuSakura
 */
public interface ListenableDanmakuClient extends DanmakuClient {
    void addMessageReceivedListener(Consumer<MessageReceivedEvent> consumer);

    void addStatusChangedListener(Consumer<StatusChangedEvent> consumer);

    boolean removeMessageReceivedListener(Consumer<MessageReceivedEvent> consumer);

    boolean removeStatusChangedListener(Consumer<StatusChangedEvent> consumer);
}
