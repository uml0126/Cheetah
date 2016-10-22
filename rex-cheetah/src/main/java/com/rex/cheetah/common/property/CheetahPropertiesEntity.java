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

import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.Maps;

public abstract class CheetahPropertiesEntity implements Serializable {
    private static final long serialVersionUID = 5403317977587754742L;
    
    protected CheetahProperties properties;

    public CheetahPropertiesEntity(CheetahProperties properties) {
        this.properties = properties;
    }

    public CheetahProperties getProperties() {
        return properties;
    }

    // 在局部变量找不到相关值的时候，就取全局变量值
    // 例如：
    // 当全局变量配置为 kafka.producer.send.buffer.bytes = 20
    // 局部变量配置应为 kafka-1.kafka.producer.send.buffer.bytes = 20
    public String getString(String key) throws Exception {
        CheetahProperties subProperties = getSubProperties();
        try {
            return subProperties.getString(key);
        } catch (Exception e) {
            return properties.getString(key);
        }
    }

    public int getInteger(String key) throws Exception {
        CheetahProperties subProperties = getSubProperties();
        try {
            return subProperties.getInteger(key);
        } catch (Exception e) {
            return properties.getInteger(key);
        }
    }

    public long getLong(String key) throws Exception {
        CheetahProperties subProperties = getSubProperties();
        try {
            return subProperties.getLong(key);
        } catch (Exception e) {
            return properties.getLong(key);
        }
    }

    public boolean getBoolean(String key) throws Exception {
        CheetahProperties subProperties = getSubProperties();
        try {
            return subProperties.getBoolean(key);
        } catch (Exception e) {
            return properties.getBoolean(key);
        }
    }

    public Map<String, Object> summarizeProperties(String prefix) throws Exception {
        Map<String, Object> map = Maps.newConcurrentMap();

        // 从全局变量中归类
        summarizeProperties(properties, map, prefix.endsWith(CheetahProperties.DOT) ? prefix : prefix + CheetahProperties.DOT);

        // 从局部变量中归类，如果全局变量和局部变量都存在某个属性，那么就使用局部变量，否则使用全部变量
        CheetahProperties subProperties = getSubProperties();
        summarizeProperties(subProperties, map, prefix.endsWith(CheetahProperties.DOT) ? prefix : prefix + CheetahProperties.DOT);

        return map;
    }

    private void summarizeProperties(CheetahProperties properties, Map<String, Object> map, String prefix) {
        for (Map.Entry<String, Object> entry : properties.getMap().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (key.startsWith(prefix)) {
                int index = key.indexOf(prefix);
                String name = key.substring(index + prefix.length());
                map.put(name, value.toString());
            }
        }
    }

    public abstract CheetahProperties getSubProperties() throws Exception;
}