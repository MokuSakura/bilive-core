package org.mokusakura.bilive.core.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mokusakura.bilive.core.client.TcpDanmakuClient;
import org.mokusakura.bilive.core.model.BilibiliWebSocketFrame;
import org.mokusakura.bilive.core.model.BilibiliWebSocketHeader;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;
import org.mokusakura.bilive.core.util.ByteBufferUtils;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.Inflater;

/**
 * @author MokuSakura
 */
public class CompressedBilibiliMessageFactory implements BilibiliMessageFactory {
    private final BilibiliMessageFactory defaultBilibiliMessageFactory;
    private static final Logger log = LogManager.getLogger(TcpDanmakuClient.class);
    ThreadLocal<Map<String, Object>> threadLocal = new InheritableThreadLocal<>();

    public CompressedBilibiliMessageFactory(BilibiliMessageFactory defaultBilibiliMessageFactory) {
        this.defaultBilibiliMessageFactory = defaultBilibiliMessageFactory;
    }

    @Override
    public List<GenericBilibiliMessage> create(BilibiliWebSocketFrame frame) {
        ByteBuffer decompressedData = decompressed(frame.getWebSocketBody());
        if (decompressedData == null) {
            return new ArrayList<>();
        }
        List<BilibiliWebSocketFrame> frames = new LinkedList<>();
        while (decompressedData.hasRemaining()) {
            BilibiliWebSocketHeader header = BilibiliWebSocketHeader.newInstance(decompressedData, true);
            ByteBuffer body = decompressedData.duplicate()
                    .limit(decompressedData.position() + header.getTotalLength() - header.getHeaderLength())
                    .asReadOnlyBuffer();
            decompressedData.position(decompressedData.position() + header.getTotalLength() - header.getHeaderLength());
            frames.add(new BilibiliWebSocketFrame(header, body));
        }
        return frames.stream()
                .flatMap(webSocketFrame -> defaultBilibiliMessageFactory.create(webSocketFrame).stream())
                .collect(Collectors.toList());
    }

    protected ByteBuffer decompressed(ByteBuffer originalData) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = createThreadLocal();
        }
        Inflater inflater = (Inflater) map.get("inflater");
        ByteBuffer byteBuffer = (ByteBuffer) map.get("byteBuffer");
        inflater.reset();
        byteBuffer.clear();

        try {
            inflater.setInput(originalData);
            while (inflater.inflate(byteBuffer) != 0) {
                byteBuffer = ByteBufferUtils.ensureCapacity(byteBuffer);
            }
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()));
            log.error(e.getMessage());
            return null;
        }
        return byteBuffer.flip();
    }

    private Map<String, Object> createThreadLocal() {
        Map<String, Object> map = new TreeMap<>();
        map.put("inflater", new Inflater());
        map.put("byteBuffer", ByteBuffer.allocate(1024 * 8));
        threadLocal.set(map);
        return map;

    }
}
