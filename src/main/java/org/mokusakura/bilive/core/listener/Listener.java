package org.mokusakura.bilive.core.listener;

import org.mokusakura.bilive.core.event.GenericEvent;

/**
 * @author MokuSakura
 */

@FunctionalInterface
public interface Listener<E extends GenericEvent<?>> {
    void onEvent(E event);

}


