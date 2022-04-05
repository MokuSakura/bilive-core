package org.mokusakura.bilive.core.factory;

import org.mokusakura.bilive.core.event.GenericEvent;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;

/**
 * @author MokuSakura
 */
@FunctionalInterface
public interface EventFactory<E extends GenericBilibiliMessage, T extends GenericEvent<?>> {

    T createEvent(Long roomId, E message);

}
