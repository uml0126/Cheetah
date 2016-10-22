package com.rex.cheetah.common.property;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;

import com.google.common.collect.Maps;
import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.util.IOUtil;
import com.rex.cheetah.common.util.MathsUtil;

public class CheetahProperties implements Serializable {
    private static final long serialVersionUID = -2472070968260967737L;
    public static final String DOT = ".";
    public static final String LINE = "-";

    private final Map<String, Object> map = Maps.newConcurrentMap();

    private String content;

    public CheetahProperties() {

    }

    public CheetahProperties(String path) throws Exception {
        PropertiesConfiguration configuration = new PropertiesConfiguration(path);
        for (Iterator<String> iterator = configuration.getKeys(); iterator.hasNext();) {
            String key = iterator.next();
            String value = configuration.getString(key);
            put(key, value);
        }

        content = IOUtil.read(path, CheetahConstants.ENCODING_FORMAT).trim();
    }

    public CheetahProperties(byte[] bytes) throws Exception {
        InputStream inputStream = new ByteArrayInputStream(bytes);
        try {
            Properties properties = new Properties();
            properties.load(inputStream);
            for (Iterator<Object> iterator = properties.keySet().iterator(); iterator.hasNext();) {
                String key = iterator.next().toString();
                String value = properties.getProperty(key);
                put(key, value);
            }
        } finally {
            if (inputStream != null) {
                IOUtils.closeQuietly(inputStream);
            }
        }
        
        content = new String(bytes, CheetahConstants.ENCODING_FORMAT).trim();
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public String getContent() {
        return content;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) map.get(key);
    }

    public void put(String key, Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Value is null for key=" + key);
        }

        Long result = MathsUtil.calculate(value.toString());
        if (result != null) {
            map.put(key, result);
        } else {
            map.put(key, value);
        }
    }

    public String getString(String key) {
        Object value = map.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Value is null for key=" + key);
        }

        return String.valueOf(value);
    }

    public int getInteger(String key) {
        String value = getString(key);

        return Integer.parseInt(value);
    }

    public long getLong(String key) {
        String value = getString(key);

        return Long.parseLong(value);
    }

    public boolean getBoolean(String key) {
        String value = getString(key);

        return Boolean.valueOf(value);
    }

    public void putString(String key, String value) {
        map.put(key, value);
    }

    public void putInteger(String key, String value) {
        map.put(key, Integer.parseInt(value));
    }

    public void putLong(String key, String value) {
        map.put(key, Long.parseLong(value));
    }

    public void putBoolean(String key, String value) {
        map.put(key, Boolean.valueOf(value));
    }

    public void mergeProperties(CheetahProperties properties) {
        map.putAll(properties.getMap());
    }
    
    // 抽取全局配置中的配置分组，key为组名，例如kafka
    public Map<String, CheetahProperties> extractProperties(String group) {
        Map<String, CheetahProperties> propertiesMap = Maps.newConcurrentMap();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key.toLowerCase().startsWith(group.toLowerCase() + LINE)) {
                int index = key.indexOf(DOT);
                if (index < 0) {
                    throw new IllegalArgumentException("Property " + key + " is an invalid format");
                }

                String tag = key.substring(0, index);
                String name = key.substring(index + 1);
                CheetahProperties subProperties = propertiesMap.get(tag);
                if (subProperties == null) {
                    subProperties = new CheetahProperties();
                    propertiesMap.put(tag, subProperties);
                }
                subProperties.put(name, value);
            }
        }
        
        return propertiesMap;
    }

    @Override
    public String toString() {
        return map.toString();
    }
}