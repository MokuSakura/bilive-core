package org.mokusakura.bilive.core.api;

import org.mokusakura.bilive.core.api.model.BilibiliApiResponse;

/**
 * @author MokuSakura
 */
public interface BilibiliApiClient {
    <T> BilibiliApiResponse<T> get(String fullUrl);
}
