package org.mokusakura.bilive.core.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mokusakura.bilive.core.model.BilibiliWebSocketFrame;
import org.mokusakura.bilive.core.model.BilibiliWebSocketHeader;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;

import java.util.*;

/**
 * @author MokuSakura
 */
public class BilibiliMessageFactoryDispatcher extends Register<Short, BilibiliMessageFactory> implements
        BilibiliMessageFactory {
    private static final Set<Short> unregisteredProtocolVersion = new TreeSet<>();
    private static final Logger log = LogManager.getLogger();

    public static BilibiliMessageFactoryDispatcher createDefault() {
        BilibiliMessageFactoryDispatcher res = new BilibiliMessageFactoryDispatcher();

        res.register(BilibiliWebSocketHeader.ProtocolVersion.PureJson,
                     JsonBilibiliMessageFactory.createDefault());

        res.register(BilibiliWebSocketHeader.ProtocolVersion.CompressedBuffer,
                     new CompressedBilibiliMessageFactory(res));
        return res;
    }

    public static BilibiliMessageFactoryDispatcher createDefault(Map<Short, BilibiliMessageFactory> map) {
        BilibiliMessageFactoryDispatcher res = createDefault();
        for (Map.Entry<Short, BilibiliMessageFactory> entry : map.entrySet()) {
            res.register(entry.getKey(), entry.getValue());
        }
        return res;
    }


    @Override
    public List<GenericBilibiliMessage> create(BilibiliWebSocketFrame frame) {
        short protocolVersion = frame.getBilibiliWebSocketHeader().getProtocolVersion();
        BilibiliMessageFactory bilibiliMessageFactory = map.get(
                protocolVersion);
        if (bilibiliMessageFactory == null) {
            // TODO performance is affected here
            if (log.isDebugEnabled() && !unregisteredProtocolVersion.contains(protocolVersion)) {
                unregisteredProtocolVersion.add(protocolVersion);
                byte[] array = new byte[frame.getWebSocketBody().limit() - frame.getWebSocketBody().position()];
                frame.getWebSocketBody().get(array);
                log.debug("Unregistered ProtocolVersion found: {}. Byte array: {}", protocolVersion,
                          Arrays.toString(array));
            }
            return new ArrayList<>(0);

        }
        return bilibiliMessageFactory.create(frame);
    }
}
