package org.mokusakura.bilive.core.client;

import org.mokusakura.bilive.core.event.GenericEvent;
import org.mokusakura.bilive.core.event.MessageReceivedEvent;
import org.mokusakura.bilive.core.event.StatusChangedEvent;
import org.mokusakura.bilive.core.factory.BilibiliMessageFactory;
import org.mokusakura.bilive.core.factory.BilibiliMessageFactoryDispatcher;
import org.mokusakura.bilive.core.factory.EventFactoryDispatcher;
import org.mokusakura.bilive.core.listener.Listener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractSubscribableDanmakuClient extends AbstractDanmakuClient implements SubscribableClient {
    @SuppressWarnings("rawtypes")
    private final Map<Class, List<Listener>> listenerMap = new ConcurrentHashMap<>();

    public AbstractSubscribableDanmakuClient(EventFactoryDispatcher eventFactory,
                                             BilibiliMessageFactory bilibiliMessageFactory) {
        super(Collections.emptyList(), Collections.emptyList(), eventFactory, bilibiliMessageFactory);
    }

    public AbstractSubscribableDanmakuClient() {
        this(EventFactoryDispatcher.getShared(), BilibiliMessageFactoryDispatcher.getShared());
    }

    @Override
    public <E extends GenericEvent<?>> void subscribe(Class<E> eventClass, Listener<E> listener) {
        getListeners(eventClass).add(listener);
    }

    @Override
    public <E extends GenericEvent<?>> void unsubscribe(Class<E> eventClass, Listener<E> listener) {
        getListeners(eventClass).remove(listener);
    }

    @Override
    public <E extends GenericEvent<?>> void unsubscribeAll(Class<E> eventClass) {
        getListeners(eventClass).clear();
    }

    @SuppressWarnings("rawtypes")
    private synchronized List<Listener> getListeners(Class eventClass) {
        return listenerMap.computeIfAbsent(eventClass, k -> new LinkedList<>());
    }

    @Override
    public void unsubscribeAll() {
        listenerMap.clear();
    }

    @Override
    public void addMessageReceivedListener(Listener<MessageReceivedEvent<?>> consumer) {}

    @Override
    public void addStatusChangedListener(Listener<StatusChangedEvent<?>> consumer) {}

    @Override
    public Listener<MessageReceivedEvent<?>> removeMessageReceivedListener(Listener<MessageReceivedEvent<?>> consumer) {
        return null;
    }

    @Override
    public Listener<StatusChangedEvent<?>> removeStatusChangedListener(Listener<StatusChangedEvent<?>> consumer) {
        return null;
    }

    @Override
    public Collection<Listener<MessageReceivedEvent<?>>> getMessageReceivedListener() {return Collections.emptyList();}

    @Override
    public Collection<Listener<StatusChangedEvent<?>>> getStatusChangedListener() {return Collections.emptyList();}

    @Override
    public Collection<Listener<MessageReceivedEvent<?>>> clearMessageReceivedListeners() {return Collections.emptyList();}

    @Override
    public Collection<Listener<StatusChangedEvent<?>>> clearStatusChangedListeners() {return Collections.emptyList();}

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void callListeners(long roomId, List<GenericEvent<?>> events) {
        for (GenericEvent event : events) {
            List<Listener> listeners = getListeners(event.getClass());
            for (Listener listener : listeners) {
                listener.onEvent(event);
            }
        }
    }
}
