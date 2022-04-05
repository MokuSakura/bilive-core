package org.mokusakura.bilive.core.factory;

import org.mokusakura.bilive.core.model.BilibiliWebSocketFrame;
import org.mokusakura.bilive.core.model.BilibiliWebSocketHeader;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author MokuSakura
 */
public class BilibiliMessageFactoryDispatcher extends Register<Short, BilibiliMessageFactory> implements
        BilibiliMessageFactory {

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
        BilibiliMessageFactory bilibiliMessageFactory = map.get(
                frame.getBilibiliWebSocketHeader().getProtocolVersion());
        if (bilibiliMessageFactory == null) {
            return new ArrayList<>(0);
        }
        return bilibiliMessageFactory.create(frame);
    }
}
