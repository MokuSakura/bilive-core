package org.mokusakura.danmakurecorder;

import org.mokusakura.danmakurecorder.event.DanmakuClientClosedEvent;
import org.mokusakura.danmakurecorder.event.DanmakuReceivedEvent;

import java.io.Closeable;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * @author MokuSakura
 */
public interface DanmakuClient extends Closeable {

    Collection<Consumer<DanmakuReceivedEvent>> danmakuReceivedHandlers();

    Collection<Consumer<DanmakuClientClosedEvent>> danmakuClientClosedHandlers();


    void addReceivedHandlers(Consumer<DanmakuReceivedEvent> consumer);

    void addClosedHandlers(Consumer<DanmakuClientClosedEvent> consumer);

}
