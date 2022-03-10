package org.mokusakura.bilive.core.protocol;

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
public class ProtocolVersionDispatcher implements BilibiliLiveMessageProtocolResolver {
    private final Map<Short, BilibiliLiveMessageProtocolResolver> subFactoryMap = new HashMap<>();

    public static ProtocolVersionDispatcher createDefault() {
        ProtocolVersionDispatcher res = new ProtocolVersionDispatcher();

        res.register(BilibiliWebSocketHeader.ProtocolVersion.PureJson,
                     JsonMessageResolver.createDefault());

        res.register(BilibiliWebSocketHeader.ProtocolVersion.CompressedBuffer,
                     new CompressedProtocolResolver(res));
        return res;
    }

    public static ProtocolVersionDispatcher createDefault(Map<Short, BilibiliLiveMessageProtocolResolver> map) {
        ProtocolVersionDispatcher res = createDefault();
        for (Map.Entry<Short, BilibiliLiveMessageProtocolResolver> entry : map.entrySet()) {
            res.register(entry.getKey(), entry.getValue());
        }
        return res;
    }

    public int register(short protocolVersion, BilibiliLiveMessageProtocolResolver subFactory) {
        return subFactoryMap.put(protocolVersion, subFactory) == null ? 1 : 0;
    }

    @Override
    public List<GenericBilibiliMessage> create(BilibiliWebSocketFrame frame) {
        BilibiliLiveMessageProtocolResolver realFactory = subFactoryMap.get(
                frame.getBilibiliWebSocketHeader().getProtocolVersion());
        return realFactory == null ? new LinkedList<>() : realFactory.create(frame);
    }
}
