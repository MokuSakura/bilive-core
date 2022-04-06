package org.mokusakura.bilive.core.client;

import org.mokusakura.bilive.core.event.GenericEvent;
import org.mokusakura.bilive.core.listener.Listener;
import org.mokusakura.bilive.core.listener.ParallelListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author MokuSakura
 */

public class SubscribableClientImpl
        extends TcpDanmakuClient
        implements SubscribableClient,
        ParallelListenerHandleable {

    @SuppressWarnings("rawtypes")
    private final Map<Class<? extends GenericEvent<?>>, List<Listener>> listenerMap = new HashMap<>();
    @SuppressWarnings("rawtypes")
    private final Map<Class<? extends GenericEvent<?>>, List<ParallelListener>> parallelListenerMap = new HashMap<>();
    private Stream<GenericEvent<?>> buffered = Stream.<GenericEvent<?>>empty().parallel();
    private int bufferedEventsCount = 0;
    private int batchSize = 64;
    private int lingerMs = 1000;
    private long lastCallParallelListenersTime = System.currentTimeMillis();

    public SubscribableClientImpl() {
        //TODO add listener for status changed event
    }

    @Override
    @SuppressWarnings("rawtypes")
    public <E extends GenericEvent<?>> void subscribe(Class<E> eventClass, Listener<E> listener) {
        if (listener instanceof ParallelListener) {
            getParallelListeners(eventClass).add((ParallelListener) listener);
        } else {
            getListeners(eventClass).add(listener);
        }
    }

    @Override
    public <E extends GenericEvent<?>> void unsubscribe(Class<E> eventClass, Listener<E> listener) {
        if (listener instanceof ParallelListener) {
            getParallelListeners(eventClass).remove(listener);
        } else {
            getListeners(eventClass).remove(listener);
        }
    }

    @Override
    public <E extends GenericEvent<?>> void unsubscribeAll(Class<E> eventClass) {
        getListeners(eventClass).clear();
        getParallelListeners(eventClass).clear();
    }

    @Override
    public void unsubscribeAll() {
        listenerMap.clear();
        parallelListenerMap.clear();
    }

    @Override
    public int getBatchSize() {
        return batchSize;
    }

    @Override
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    @Override
    public int getLingerMillSeconds() {
        return lingerMs;
    }

    @Override
    public void setLingerMillSeconds(int lingerMillSeconds) {
        this.lingerMs = lingerMillSeconds;
    }


    @Override
    protected void callListeners(long roomId, List<GenericEvent<?>> messages) {
        //parallel
        callParallelListeners(messages);
        //no parallel
        messages.forEach(this::onGenericEvent);


    }

    private void callParallelListeners(List<GenericEvent<?>> events) {

        long currentTimeMillis = System.currentTimeMillis();
        if (bufferedEventsCount + events.size() >= batchSize
                || currentTimeMillis - lastCallParallelListenersTime >= lingerMs) {
            lastCallParallelListenersTime = currentTimeMillis;
            //Only god and I know how it works.
            //Now, only god knows how it works.
            for (var entry : parallelListenerMap.entrySet()) {
                //noinspection unchecked
                buffered.filter(e -> e.getClass().equals(entry.getKey()))
                        .forEach(e -> entry.getValue().forEach(l -> l.onEvent(e)));
            }
            bufferedEventsCount = 0;
            buffered = Stream.empty();
        }
        buffered = Stream.concat(buffered, events.stream()).parallel();
        bufferedEventsCount += events.size();
    }

    @SuppressWarnings("rawtypes")
    private synchronized List<Listener> getListeners(Class<? extends GenericEvent<?>> eventClass) {
        return listenerMap.computeIfAbsent(eventClass, k -> new LinkedList<>());
    }

    @SuppressWarnings("rawtypes")
    private synchronized List<ParallelListener> getParallelListeners(Class<? extends GenericEvent<?>> eventClass) {
        return parallelListenerMap.computeIfAbsent(eventClass, k -> new LinkedList<>());
    }

    @SuppressWarnings("rawtypes")
    private void onGenericEvent(GenericEvent<?> e) {
        if (listenerMap.containsKey(e.getClass())) {
            //subscribe method ensured that the listener will take e.getClass() as the parameter.

            List<Listener> listeners = listenerMap.get(e.getClass());
            for (Listener listener : listeners) {
                try {
                    //noinspection unchecked
                    listener.onEvent(e);
                } catch (Exception ignored) {
                }
            }
        }
    }


}
