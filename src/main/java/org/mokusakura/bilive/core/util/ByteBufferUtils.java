package org.mokusakura.bilive.core.util;

import java.nio.ByteBuffer;

public class ByteBufferUtils {
    public static ByteBuffer duplicateCapacity(ByteBuffer byteBuffer) {
        return incrementCapacity(byteBuffer, 2);
    }

    public static ByteBuffer incrementCapacity(ByteBuffer byteBuffer, int factor) {
        int size = byteBuffer.capacity() * factor;
        ByteBuffer res = ByteBuffer.allocate(size);
        // Mark, this is essential here.
        byteBuffer.flip();
        res.put(byteBuffer);
        return res;
    }

    public static ByteBuffer ensureCapacity(ByteBuffer buffer) {
        if (buffer.capacity() == buffer.position()) {
            return duplicateCapacity(buffer);
        }
        return buffer;
    }
}
