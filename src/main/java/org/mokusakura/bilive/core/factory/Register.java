package org.mokusakura.bilive.core.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MokuSakura
 */
abstract class Register<E, T> {
    Map<E, T> map = new HashMap<>();

    /**
     * @param e key
     * @param t value
     * @return replaced count
     */
    public int register(E e, T t) {
        return map.put(e, t) == null ? 1 : 0;
    }

    /**
     * @param registerMap register map
     * @return replaced count
     */
    public int register(Map<E, T> registerMap) {
        int count = 0;
        for (Map.Entry<E, T> entry : registerMap.entrySet()) {
            count += register(entry.getKey(), entry.getValue());
        }
        return count;
    }

    public int unregister(E e) {
        return map.remove(e) == null ? 0 : 1;
    }

    public int unregister(List<E> list) {
        int count = 0;
        for (E e : list) {
            count += unregister(e);
        }
        return count;
    }

}
