package org.mokusakura.bilive.core.api;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mokusakura.bilive.core.api.model.BilibiliApiResponse;
import org.mokusakura.bilive.core.exception.BilibiliApiCodeNotZeroException;
import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;

/**
 * @author MokuSakura
 */
public abstract class AbstractBilibiliApiClient implements BilibiliApiClient {
    private static final Logger log = LogManager.getLogger(AbstractBilibiliApiClient.class);
    public static final String HEADER_ACCEPT = "application/json, text/javascript, */*; q=0.01";
    public static final String HEADER_ORIGIN = "https://live.bilibili.com";
    public static final String HEADER_REFERER = "https://live.bilibili.com/";
    public static final String HEADER_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36";
    public static final String BILIBILI_LIVE_API_HOST = "api.live.bilibili.com";
    private final HttpClient httpClient;

    public AbstractBilibiliApiClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }


    @Override
    public <T> BilibiliApiResponse<T> get(String path, Class<T> actualClass) throws NoNetworkConnectionException {
        try {
            var request = HttpRequest.newBuilder()
                    .GET()
                    .uri(new URI("https://" + BILIBILI_LIVE_API_HOST + path))
                    .header("Accept", HEADER_ACCEPT)
                    .header("Origin", HEADER_ORIGIN)
                    .header("Referer", HEADER_REFERER)
                    .header("User-Agent", HEADER_USER_AGENT)
                    .build();
            //log.debug(request.uri().toString());
            var response = httpClient.send(request,
                                           HttpResponse.BodyHandlers.ofString());
            var res = JSONObject.parseObject(response.body(),
                                             new TypeReference<BilibiliApiResponse<T>>(actualClass) {});
            if (!res.getCode().equals(0)) {
                throw new BilibiliApiCodeNotZeroException(request.uri().toString(), res.getMessage(), res.getCode());
            }
            return res;
        } catch (IOException | InterruptedException e) {
            log.info("No network connection");
//            log.debug(e.getClass().toString());
            throw new NoNetworkConnectionException();
        } catch (BilibiliApiCodeNotZeroException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unknown exception when getting room info.");
            log.error(e.getClass().toString());
            log.error(Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }
    }
}
