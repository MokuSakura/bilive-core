package org.mokusakura.bilive.core.client;

import org.mokusakura.bilive.core.event.GenericEvent;
import org.mokusakura.bilive.core.event.MessageReceivedEvent;
import org.mokusakura.bilive.core.event.StatusChangedEvent;
import org.mokusakura.bilive.core.listener.Listener;

import java.util.*;

/**
 * @author MokuSakura
 */
@SuppressWarnings("rawtypes")
public class SubscribableClientImpl extends TcpDanmakuClient implements SubscribableClient {


    Map<Class<? extends GenericEvent<?>>, List<Listener>> listenerMap = new HashMap<>();

    public SubscribableClientImpl() {
        super.addMessageReceivedListener(this::onGenericEvent);
        super.addStatusChangedListener(this::onGenericEvent);
    }

    @Override
    public Collection<Listener<MessageReceivedEvent<?>>> clearMessageReceivedListeners() {
        return new ArrayList<>(0);
    }

    @Override
    public Collection<Listener<StatusChangedEvent<?>>> clearStatusChangedListeners() {
        return new ArrayList<>(0);
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

    @Override
    public <E extends GenericEvent<?>> void subscribe(Class<E> eventClass, Listener<E> listener) {
        listenerMap.computeIfAbsent(eventClass, k -> new LinkedList<>()).add(listener);
    }


}
