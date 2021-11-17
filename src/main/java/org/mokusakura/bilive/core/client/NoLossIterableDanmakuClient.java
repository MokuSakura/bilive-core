package org.mokusakura.bilive.core.client;

/**
 * @author MokuSakura
 * <p>
 *     This class will ensure no message will loss.
 *     But may consume unexpected amount of memory if messages are not consumed in time.
 * </p>
 */
public interface NoLossIterableDanmakuClient extends IterableDanmakuClient {
}
