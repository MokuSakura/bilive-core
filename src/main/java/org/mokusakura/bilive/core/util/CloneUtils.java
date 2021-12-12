package org.mokusakura.bilive.core.util;

import java.io.*;

/**
 * @author MokuSakura
 */
public class CloneUtils {
    public static <T> T deepClone(T t) throws CloneNotSupportedException {
        if (!(t instanceof Cloneable)) {
            throw new CloneNotSupportedException();
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(baos);
            objectOutputStream.writeObject(t);
            ObjectInputStream bais = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            //noinspection unchecked
            return (T) bais.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException ignored) {
            throw new RuntimeException();
        }
    }
}
