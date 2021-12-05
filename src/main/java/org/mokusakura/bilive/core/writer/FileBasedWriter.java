package org.mokusakura.bilive.core.writer;

import org.mokusakura.bilive.core.util.PropertiesUtil;

import java.io.File;

/**
 * @author MokuSakura
 */
public abstract class FileBasedWriter implements DanmakuWriter {
    protected String path;
    protected Boolean replaceFile;
    protected Boolean lineSeparate;
    protected Boolean compress;

    protected void resolveFileProperties(PropertiesUtil properties) {
        path = properties.getProperty("path");
        if (path == null) {
            throw new IllegalArgumentException("property path is mandatory");
        }
        if (path.endsWith(File.separator)) {
            path = path.substring(0, path.length() - 1 - File.separator.length());
        }

        replaceFile = properties.getBooleanProperty("replaceFile", false);
        lineSeparate = properties.getBooleanProperty("lineSeparate", false);
        compress = properties.getBooleanProperty("compress", false);
    }

    protected abstract boolean doWriteSeparator();

    protected boolean writeSeparator() {
        if (this.lineSeparate) {
            return doWriteSeparator();
        }
        return false;
    }
}
