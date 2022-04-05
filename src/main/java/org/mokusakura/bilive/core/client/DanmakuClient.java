package org.mokusakura.bilive.core.client;

import org.mokusakura.bilive.core.event.MessageReceivedEvent;
import org.mokusakura.bilive.core.event.StatusChangedEvent;
import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;
import org.mokusakura.bilive.core.exception.NoRoomFoundException;
import org.mokusakura.bilive.core.listener.Listener;
import org.mokusakura.bilive.core.model.BilibiliWebSocketFrame;

import java.io.Closeable;
import java.util.Collection;
import java.util.concurrent.Future;

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

    /**
     * <p>
     * Send message to specified room, no matter the room is short id or real id.
     * </p>
     *
     * @param roomId Room id to send message. No matter short id or real id.
     * @param frame  Message to send
     * @return true if message sent successfully else false
     */
    Future<Boolean> sendMessageAsync(long roomId, BilibiliWebSocketFrame frame);

    /**
     * <p>
     * If the client is still functioning, it will return true.
     * Real effect is depends on the implementation.
     * </p>
     *
     * @return true if client is still functioning else false
     */
    boolean isOpen();

    /**
     * <p>
     * Add specified message received listener.
     * </p>
     *
     * @param consumer Message received listener to add.
     */
    void addMessageReceivedListener(Listener<MessageReceivedEvent<?>> consumer);

    /**
     * <p>
     * Add specified status changed listener.
     * </p>
     *
     * @param consumer Status changed listener to add.
     */
    void addStatusChangedListener(Listener<StatusChangedEvent<?>> consumer);

    /**
     * @return A copy of message received listeners
     */
    Collection<Listener<MessageReceivedEvent<?>>> getMessageReceivedListener();

    /**
     * @return A copy of status changed listeners
     */
    Collection<Listener<StatusChangedEvent<?>>> getStatusChangedListener();

    /**
     * <p>
     * Clear all message received listeners and return a copy of them.
     * </p>
     *
     * @return A copy of message received listeners
     */
    Collection<Listener<MessageReceivedEvent<?>>> clearMessageReceivedListeners();

    /**
     * <p>
     * Clear all status changed listeners and return a copy of them.
     * </p>
     *
     * @return A copy of status changed listeners
     */
    Collection<Listener<StatusChangedEvent<?>>> clearStatusChangedListeners();

    /**
     * Remove specified message received listener and return it if it exists else null.
     *
     * @param consumer Message received listener to remove.
     * @return Removed message received listener if it exists else null.
     */
    Listener<MessageReceivedEvent<?>> removeMessageReceivedListener(Listener<MessageReceivedEvent<?>> consumer);

    /**
     * Remove specified status changed listener and return it if it exists else null.
     *
     * @param consumer Status changed listener to remove.
     * @return Removed status changed listener if it exists else null.
     */
    Listener<StatusChangedEvent<?>> removeStatusChangedListener(Listener<StatusChangedEvent<?>> consumer);

}
