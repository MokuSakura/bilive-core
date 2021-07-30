package org.mokusakura.bilive.core;

import org.mokusakura.bilive.core.event.DanmakuReceivedEvent;
import org.mokusakura.bilive.core.event.LiveBeginEvent;
import org.mokusakura.bilive.core.event.LiveEndEvent;
import org.mokusakura.bilive.core.event.OtherEvent;
import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;
import org.mokusakura.bilive.core.exception.NoRoomFoundException;

import java.io.Closeable;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * @author MokuSakura
 */
public interface DanmakuClient extends Closeable {

    Collection<Consumer<DanmakuReceivedEvent>> danmakuReceivedHandlers();

    Collection<Consumer<LiveEndEvent>> liveEndHandlers();

    Collection<Consumer<LiveBeginEvent>> liveBeginHandlers();

    Collection<Consumer<OtherEvent>> otherHandlers();

    /**
     * <p>
     * Connect to specified room id. No matter the id is short id or real id.
     * </p>
     *
     * @param roomId Room id to connect. No matter short id or real id.
     * @throws NoNetworkConnectionException If there is no net work connection
     * @throws NoRoomFoundException         If room id cannot be found
     */
    void connect(int roomId) throws NoNetworkConnectionException, NoRoomFoundException;

    boolean isConnected();

    void disconnect();

    void addLiveBeginHandler(Consumer<LiveBeginEvent> consumer);

    void addReceivedHandlers(Consumer<DanmakuReceivedEvent> consumer);

    void addLiveEndHandlers(Consumer<LiveEndEvent> consumer);

    void addOtherHandlers(Consumer<OtherEvent> consumer);

}
