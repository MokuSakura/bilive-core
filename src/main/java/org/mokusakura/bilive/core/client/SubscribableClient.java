package org.mokusakura.bilive.core.client;

import org.mokusakura.bilive.core.event.GenericEvent;
import org.mokusakura.bilive.core.listener.Listener;

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
}
