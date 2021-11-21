package org.mokusakura.bilive.core.writer;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

/**
 * @author MokuSakura
 */

public class TestXmlWriter {
    @Test
    @SneakyThrows
    public void testEnable() {
        XmlDanmakuWriter danmakuWriter = new XmlDanmakuWriter();
        danmakuWriter.enable("C:\\Users\\MokuSakura\\Desktop\\test\\fa\\1.xml");
    }
}
