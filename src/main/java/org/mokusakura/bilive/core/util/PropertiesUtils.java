package org.mokusakura.bilive.core.util;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;

/**
 * @author MokuSakura
 */
public class PropertiesUtils extends Properties {
    public PropertiesUtils(Properties defaults) {
        for (Map.Entry<Object, Object> entry : defaults.entrySet()) {
            super.put(entry.getKey(), entry.getValue());
        }
    }

    public PropertiesUtils() {
    }

    public PropertiesUtils(int initialCapacity) {
        super(initialCapacity);
    }

    public String getStringProperty(String key) {
        String res = getStringProperty(key, null);
        checkNull(res, key);
        return res;
    }

    public String getStringProperty(String key, String defaultValue) {
        String str = super.getProperty(key);
        return str == null ? defaultValue : str;
    }

    public String getStringPropertyResolveNull(String key) {
        return getStringPropertyResolveNull(key, null);
    }

    public String getStringPropertyResolveNull(String key, String defaultValue) {
        String str = super.getProperty(key);
        if (str == null) {
            return defaultValue;
        }
        if ("null".equalsIgnoreCase(str)) {
            return null;
        }
        return str;
    }


    public Long getLongProperty(String key) {
        Long res = getLongProperty(key, null);
        checkNull(res, key);
        return res;
    }

    public Long getLongProperty(String key, Long defaultValue) {
        Object obj = super.get(key);
        if (obj == null) {
            return defaultValue;
        }
        if (obj instanceof Long) {
            return (Long) obj;
        }
        if (obj instanceof String) {
            try {
                return Long.parseLong((String) obj);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(MessageFormat.format("{0} is not a long value", key));
            }
        } else {
            throw new IllegalArgumentException(MessageFormat.format("{0} is not a Long value", key));
        }
    }

    public Boolean getBooleanProperty(String key) {
        Boolean res = this.getBooleanProperty(key, null);
        checkNull(res, key);
        return res;
    }

    private void checkNull(Object res, String key) {
        if (res == null) {
            throw new IllegalArgumentException(MessageFormat.format("{0} is not specified", key));
        }
    }

    public Boolean getBooleanProperty(String key, Boolean defaultValue) {
        Object obj = super.get(key);
        return obj == null ? defaultValue : resolveBoolean(key, obj);
    }

    private Boolean resolveBoolean(String key, Object obj) {
        if (obj instanceof String) {
            String str = (String) obj;
            str = str.toLowerCase();
            switch (str) {
                case "true":
                    return true;
                case "false":
                    return false;
                default:
                    throw new IllegalArgumentException(MessageFormat.format("{0} is not a boolean value", str));
            }
        } else if (obj instanceof Boolean) {
            return (Boolean) obj;
        } else {
            throw new IllegalArgumentException(MessageFormat.format("{0} is not a Boolean value", key));
        }
    }
}
