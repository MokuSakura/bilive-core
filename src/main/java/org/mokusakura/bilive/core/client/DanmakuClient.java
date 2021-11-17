package org.mokusakura.bilive.core.client;

import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;
import org.mokusakura.bilive.core.exception.NoRoomFoundException;

import java.io.Closeable;
import java.io.IOException;

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

}
