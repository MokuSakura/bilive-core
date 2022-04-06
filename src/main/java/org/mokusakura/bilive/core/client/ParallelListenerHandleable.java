package org.mokusakura.bilive.core.client;

/**
 * @author MokuSakura
 */
public interface ParallelListenerHandleable {
    int getBatchSize();

    void setBatchSize(int batchSize);

    int getLingerMillSeconds();

    void setLingerMillSeconds(int lingerMillSeconds);
}
