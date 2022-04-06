package org.mokusakura.bilive.core.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mokusakura.bilive.core.client.TcpDanmakuClient;
import org.mokusakura.bilive.core.model.BilibiliWebSocketFrame;
import org.mokusakura.bilive.core.model.BilibiliWebSocketHeader;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.Inflater;

/**
 * @author MokuSakura
 */
public class CompressedBilibiliMessageFactory implements BilibiliMessageFactory {
    private final BilibiliMessageFactory defaultBilibiliMessageFactory;
    private static final Logger log = LogManager.getLogger(TcpDanmakuClient.class);
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
                    .limit(decompressedData.position() + header.getTotalLength() - header.getHeaderLength());
            decompressedData.position(decompressedData.position() + header.getTotalLength() - header.getHeaderLength());
            frames.add(new BilibiliWebSocketFrame(header, body));
        }
        return frames.stream()
                .flatMap(webSocketFrame -> defaultBilibiliMessageFactory.create(webSocketFrame).stream())
                .collect(Collectors.toList());
    }

    protected ByteBuffer decompressed(ByteBuffer originalData) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Inflater inflater = new Inflater();
            byte[] buffer = new byte[1024];
            inflater.setInput(originalData);
            int inflateLength;
            while ((inflateLength = inflater.inflate(buffer, 0, buffer.length)) != 0) {
                baos.write(buffer, 0, inflateLength);
            }
            inflater.end();
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()));
            log.error(e.getMessage());
            return null;
        }
        return baos.toByteArray().length == 0 ? null : ByteBuffer.wrap(baos.toByteArray());
    }
}
