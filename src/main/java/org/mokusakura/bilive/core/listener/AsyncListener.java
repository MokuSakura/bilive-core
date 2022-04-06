package org.mokusakura.bilive.core.listener;

import org.mokusakura.bilive.core.event.GenericEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author MokuSakura
 */
@FunctionalInterface
public interface AsyncListener<E extends GenericEvent<?>> extends Listener<E> {

    @Override
    default void onEvent(E event) {ExecutorHolder.EXECUTOR.execute(() -> doOnEvent(event));}

    default Future<?> onEventAsync(E event) {return ExecutorHolder.EXECUTOR.submit(() -> doOnEvent(event));}

    void doOnEvent(E event);

    class ExecutorHolder {
        private static ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

        public static synchronized void changeExecutor(ExecutorService executor) {
            EXECUTOR.shutdown();
            EXECUTOR = executor;
        }
    }
}


