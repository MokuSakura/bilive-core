package org.mokusakura.bilive.core.factory;

import lombok.extern.log4j.Log4j2;
import org.mokusakura.bilive.core.model.BilibiliWebSocketFrame;
import org.mokusakura.bilive.core.model.BilibiliWebSocketHeader;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * @author MokuSakura
 */
@Log4j2
public class CompressedBilibiliMessageFactory implements BilibiliMessageFactory {
    private final BilibiliMessageFactory defaultBilibiliMessageFactory;

    public CompressedBilibiliMessageFactory(BilibiliMessageFactory defaultBilibiliMessageFactory) {
        this.defaultBilibiliMessageFactory = defaultBilibiliMessageFactory;
    }

    @Override
    public List<GenericBilibiliMessage> create(BilibiliWebSocketFrame frame) {
        byte[] decompressedData = decompressed(frame.getWebSocketBody());
        if (decompressedData == null) {
            return new ArrayList<>();
        }
        List<BilibiliWebSocketFrame> frames = new LinkedList<>();
        int offset = 0;
        while (offset != decompressedData.length) {
            byte[] headerSlice = Arrays.copyOfRange(decompressedData, offset,
                                                    offset + BilibiliWebSocketHeader.HEADER_LENGTH);
            BilibiliWebSocketHeader header = BilibiliWebSocketHeader.newInstance(headerSlice);
            byte[] body = Arrays.copyOfRange(decompressedData,
                                             offset + BilibiliWebSocketHeader.BODY_OFFSET,
                                             (int) (offset + header.getTotalLength()));
            frames.add(new BilibiliWebSocketFrame(header, body));
            offset += header.getTotalLength();
        }
        List<GenericBilibiliMessage> res = new LinkedList<>();
        for (BilibiliWebSocketFrame frame1 : frames) {
            res.addAll(defaultBilibiliMessageFactory.create(frame1));
        }
        return res;
    }

    protected byte[] decompressed(byte[] originalData) {
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
        return baos.toByteArray();
    }
}
