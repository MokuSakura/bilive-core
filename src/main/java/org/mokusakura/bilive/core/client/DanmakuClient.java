package org.mokusakura.bilive.core.client;

import org.mokusakura.bilive.core.event.MessageReceivedEvent;
import org.mokusakura.bilive.core.event.StatusChangedEvent;
import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;
import org.mokusakura.bilive.core.exception.NoRoomFoundException;
import org.mokusakura.bilive.core.model.BilibiliWebSocketFrame;

import java.io.Closeable;
import java.util.Collection;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * @author MokuSakura
 */
public interface DanmakuClient extends Closeable {
    /**
     * <p>
     * Connect to specified room id. No matter the id is short id or real id.
     * </p>
     *
     * @param roomId Room id to connect. No matter short id or real id.
     * @return true if connected successfully else false
     * @throws NoNetworkConnectionException If there is no net work connection
     * @throws NoRoomFoundException         If room id cannot be found
     */
    boolean connect(long roomId) throws NoNetworkConnectionException, NoRoomFoundException;


    Future<Boolean> sendMessageAsync(long roomId, BilibiliWebSocketFrame frame);

    boolean isOpen();

    void addMessageReceivedListener(Consumer<MessageReceivedEvent> consumer);

    void addStatusChangedListener(Consumer<StatusChangedEvent> consumer);

    Collection<Consumer<MessageReceivedEvent>> clearMessageReceivedListeners();

    Collection<Consumer<StatusChangedEvent>> clearStatusChangedListeners();

    Consumer<MessageReceivedEvent> removeMessageReceivedListener(Consumer<MessageReceivedEvent> consumer);

    Consumer<StatusChangedEvent> removeStatusChangedListener(Consumer<StatusChangedEvent> consumer);

}
