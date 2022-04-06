package org.mokusakura.bilive.core.listener;

import org.mokusakura.bilive.core.event.GenericEvent;

/**
 * A listener that can receive events in parallel.
 * {@link #onEvent(GenericEvent)} may not be called
 * as soon as events arrive. Events may be stored in a queue.
 * and called later for a better performance.
 * {@link #onEvent(GenericEvent)} will be called in parallel.
 * However, some inheritors of DanmakuClient may not be able to
 * handle events in parallel. Consider using {@link
 * AsyncListener} for asynchronous event handling.
 *
 * @author MokuSakura
 */
@FunctionalInterface
public interface ParallelListener<E extends GenericEvent<?>> extends Listener<E> {
}
