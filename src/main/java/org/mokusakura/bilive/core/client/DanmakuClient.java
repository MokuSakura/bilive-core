package org.mokusakura.bilive.core.client;

import org.mokusakura.bilive.core.event.MessageReceivedEvent;
import org.mokusakura.bilive.core.event.StatusChangedEvent;
import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;
import org.mokusakura.bilive.core.exception.NoRoomFoundException;

import java.io.Closeable;
import java.io.IOException;
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
     * @throws NoNetworkConnectionException If there is no net work connection
     * @throws NoRoomFoundException         If room id cannot be found
     */
    void connect(long roomId) throws NoNetworkConnectionException, NoRoomFoundException;

    boolean isConnected();

    void disconnect() throws IOException;

    void addMessageReceivedListener(Consumer<MessageReceivedEvent> consumer);

    void addStatusChangedListener(Consumer<StatusChangedEvent> consumer);

    boolean removeMessageReceivedListener(Consumer<MessageReceivedEvent> consumer);

    boolean removeStatusChangedListener(Consumer<StatusChangedEvent> consumer);

}
