package org.mokusakura.bilive.core.client;

import org.mokusakura.bilive.core.event.GenericEvent;
import org.mokusakura.bilive.core.event.MessageReceivedEvent;
import org.mokusakura.bilive.core.event.StatusChangedEvent;
import org.mokusakura.bilive.core.listener.Listener;

import java.util.Collection;
import java.util.Collections;

/**
 * <p>
 * Using this class will disable {@link DanmakuClient#addMessageReceivedListener(Listener)}
 * ,{@link DanmakuClient#addStatusChangedListener(Listener)}
 * ,{@link DanmakuClient#removeMessageReceivedListener(Listener)}
 * ,{@link DanmakuClient#removeStatusChangedListener(Listener)}
 * ,{@link DanmakuClient#clearMessageReceivedListeners()}
 * and {@link DanmakuClient#clearStatusChangedListeners()}
 * </p>
 *
 * @author MokuSakura
 */
public interface SubscribableClient extends DanmakuClient {
    <E extends GenericEvent<?>> void subscribe(Class<E> eventClass, Listener<E> listener);

    <E extends GenericEvent<?>> void unsubscribe(Class<E> eventClass, Listener<E> listener);

    <E extends GenericEvent<?>> void unsubscribeAll(Class<E> eventClass);

    void unsubscribeAll();

    @Override
    default void addMessageReceivedListener(Listener<MessageReceivedEvent<?>> consumer) {}

    @Override
    default void addStatusChangedListener(Listener<StatusChangedEvent<?>> consumer) {}

    @Override
    default Collection<Listener<MessageReceivedEvent<?>>> getMessageReceivedListener() {return Collections.emptyList();}

    @Override
    default Collection<Listener<StatusChangedEvent<?>>> getStatusChangedListener() {return Collections.emptyList();}

    @Override
    default Collection<Listener<MessageReceivedEvent<?>>> clearMessageReceivedListeners() {return Collections.emptyList();}

    @Override
    default Collection<Listener<StatusChangedEvent<?>>> clearStatusChangedListeners() {return Collections.emptyList();}

    @Override
    default Listener<MessageReceivedEvent<?>> removeMessageReceivedListener(
            Listener<MessageReceivedEvent<?>> consumer) {return null;}

    @Override
    default Listener<StatusChangedEvent<?>> removeStatusChangedListener(
            Listener<StatusChangedEvent<?>> consumer) {return null;}
}
