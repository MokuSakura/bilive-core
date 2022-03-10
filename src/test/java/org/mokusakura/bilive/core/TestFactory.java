package org.mokusakura.bilive.core;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.mokusakura.bilive.core.api.BilibiliLiveApiClient;
import org.mokusakura.bilive.core.api.HttpLiveApiClient;
import org.mokusakura.bilive.core.client.DanmakuClient;
import org.mokusakura.bilive.core.client.TcpDanmakuClient;
import org.mokusakura.bilive.core.protocol.PopularityProtocolResolver;
import org.mokusakura.bilive.core.protocol.ProtocolVersionDispatcher;

import java.net.http.HttpClient;

/**
 * @author MokuSakura
 */
@Log4j2
public class TestFactory {
    private final DanmakuClient danmakuClient;
    private final BilibiliLiveApiClient bilibiliLiveApiClient;
    private final ProtocolVersionDispatcher bilibiliMessageFactory;
    private final HttpClient httpClient;

    public TestFactory() {
        httpClient = HttpClient.newHttpClient();
        bilibiliLiveApiClient = new HttpLiveApiClient(httpClient);
        bilibiliMessageFactory = ProtocolVersionDispatcher.createDefault();
        PopularityProtocolResolver popularityProtocolResolver = new PopularityProtocolResolver();
        bilibiliMessageFactory.register((short) 1, popularityProtocolResolver);
        danmakuClient = new TcpDanmakuClient(bilibiliLiveApiClient, bilibiliMessageFactory);
        danmakuClient.addMessageReceivedListener(message -> {
            log.info(message.getMessage().getRawMessage());
        });
    }

    @SneakyThrows
    public static void main(String[] args) {
        TestFactory factory = new TestFactory();
        factory.link();
    }

    @SneakyThrows
    void link() {
        danmakuClient.connect(21452505);
    }
}
