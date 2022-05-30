package org.mokusakura.bilive.core.writer;

import org.mokusakura.bilive.core.util.PropertiesUtils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Any class inherited from this class should
 * support the properties list in {@link #resolveFileProperties(PropertiesUtils)}.
 *
 * @author MokuSakura
 */
public abstract class FileBasedWriter implements MessageWriter {
    protected String path;
    protected Boolean replaceFile;
    protected Boolean compress;
    public static final Pattern BUFF_SIZE_PATTERN = Pattern.compile("(?i)(\\d*)([mgk]?)");
    protected int bufferSize;

    /**
     * @param properties properties that can be used in this class.
     *                   Supported properties:
     *                   - path: String, mandatory
     *                   File that will store all danmaku will be created in this path.
     *                   If the file doesn't exists,
     *                   new file will be created as well as all directories.
     *                   <p>
     *                   - bufferSize: String, default=8k
     *                   Buffer size. Should be integer or
     *                   integer with k,m or b. default unit is b
     *                   <p>
     *                   - compress: Boolean, default=false
     *                   If true, file will be compressed as gzip.
     *                   <p>
     *                   - replaceFile: true or false, default=false
     *                   If true and there is an already existing file,
     *                   new file will replace the old one.
     **/
    protected void resolveFileProperties(PropertiesUtils properties) {
        path = properties.getProperty("path");
        if (path == null) {
            throw new IllegalArgumentException("Property path is mandatory");
        }

        if (path.endsWith(File.separator)) {
            path = path.substring(0, path.length() - 1 - File.separator.length());
        }
        String bufSizeStr = properties.getStringProperty("buffSize", "8k").toLowerCase();
        Matcher matcher = BUFF_SIZE_PATTERN.matcher(bufSizeStr);
        String unit;
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    "Property buffSize is illegal. Value should be integer or integer which ends with m, k, b. Default unit is b.");
        }
        bufferSize = Integer.parseInt(matcher.group(1));
        unit = matcher.group(1);
        if ("k".equals(unit)) {
            bufferSize = bufferSize * 1024;
        } else if ("m".equals(unit)) {
            bufferSize = bufferSize * 1024 * 1024;
        }

        replaceFile = properties.getBooleanProperty("replaceFile", false);

        compress = properties.getBooleanProperty("compress", false);
    }

}
