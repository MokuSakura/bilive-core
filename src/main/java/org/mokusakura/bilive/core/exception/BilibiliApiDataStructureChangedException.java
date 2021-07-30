package org.mokusakura.bilive.core.exception;

/**
 * @author MokuSakura
 */
public class BilibiliApiDataStructureChangedException extends BilibiliApiException {
    private String currentDataJson;

    public BilibiliApiDataStructureChangedException(String url, String message, String currentDataJson) {
        super(url, message);
        this.currentDataJson = currentDataJson;
    }

    public String getCurrentDataJson() {
        return currentDataJson;
    }

    public BilibiliApiDataStructureChangedException setCurrentDataJson(String currentDataJson) {
        this.currentDataJson = currentDataJson;
        return this;
    }
}
