package org.mokusakura.bilive.core.api;

import org.mokusakura.bilive.core.api.model.BilibiliApiResponse;
import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;

/**
 * @author MokuSakura
 */
public interface BilibiliApiClient {
    <T> BilibiliApiResponse<T> get(String fullUrl, Class<T> tClass) throws NoNetworkConnectionException;
}
