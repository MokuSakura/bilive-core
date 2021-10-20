package org.mokusakura.bilive.core;

import org.mokusakura.bilive.core.model.BilibiliWebSocketFrame;
import org.mokusakura.bilive.core.model.BilibiliWebSocketHeader;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author MokuSakura
 */
public class DefaultBilibiliMessageFactory implements BilibiliMessageFactory {
    private final Map<Short, BilibiliMessageFactory> subFactoryMap = new HashMap<>();

    private DefaultBilibiliMessageFactory() {}

    public static DefaultBilibiliMessageFactory createDefault() {
        DefaultBilibiliMessageFactory res = new DefaultBilibiliMessageFactory();
        res.register(BilibiliWebSocketHeader.ProtocolVersion.PureJson, JsonBilibiliMessageFactory.createDefault());
        res.register(BilibiliWebSocketHeader.ProtocolVersion.CompressedBuffer,
                     new CompressedBilibiliMessageFactory(res));
        return res;
    }

    public static DefaultBilibiliMessageFactory createDefault(Map<Short, BilibiliMessageFactory> map) {
        DefaultBilibiliMessageFactory res = createDefault();
        for (Map.Entry<Short, BilibiliMessageFactory> entry : map.entrySet()) {
            res.register(entry.getKey(), entry.getValue());
        }
        return res;
    }

    public int register(short protocolVersion, BilibiliMessageFactory subFactory) {
        return subFactoryMap.put(protocolVersion, subFactory) == null ? 1 : 0;
    }

    @Override
    public List<GenericBilibiliMessage> create(BilibiliWebSocketFrame frame) {
        BilibiliMessageFactory realFactory = subFactoryMap.get(frame.getWebSocketHeader().getProtocolVersion());
        return realFactory == null ? new LinkedList<>() : realFactory.create(frame);
    }
}
