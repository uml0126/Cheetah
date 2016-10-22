package com.rex.cheetah.common.object;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.property.CheetahProperties;

public class ObjectPoolFactory {
    private static CheetahProperties properties;
    
    public static void initialize(CheetahProperties properties) {
        ObjectPoolFactory.properties = properties;
    }
    
    public static GenericObjectPoolConfig createFSTObjectPoolConfig() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        try {
            config.setMaxTotal(CheetahConstants.CPUS * properties.getInteger(CheetahConstants.FST_OBJECT_POOL_MAX_TOTAL_ATTRIBUTE_NAME));
            config.setMaxIdle(CheetahConstants.CPUS * properties.getInteger(CheetahConstants.FST_OBJECT_POOL_MAX_IDLE_ATTRIBUTE_NAME));
            config.setMinIdle(CheetahConstants.CPUS * properties.getInteger(CheetahConstants.FST_OBJECT_POOL_MIN_IDLE_ATTRIBUTE_NAME));
            config.setMaxWaitMillis(properties.getLong(CheetahConstants.FST_OBJECT_POOL_MAX_WAIT_MILLIS_ATTRIBUTE_NAME));
            config.setTimeBetweenEvictionRunsMillis(properties.getLong(CheetahConstants.FST_OBJECT_POOL_TIME_BETWEEN_EVICTION_RUN_MILLIS_ATTRIBUTE_NAME));
            config.setMinEvictableIdleTimeMillis(properties.getLong(CheetahConstants.FST_OBJECT_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS_ATTRIBUTE_NAME));
            config.setSoftMinEvictableIdleTimeMillis(properties.getLong(CheetahConstants.FST_OBJECT_POOL_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS_ATTRIBUTE_NAME));
            config.setBlockWhenExhausted(properties.getBoolean(CheetahConstants.FST_OBJECT_POOL_BLOCK_WHEN_EXHAUSTED_ATTRIBUTE_NAME));
            config.setLifo(properties.getBoolean(CheetahConstants.FST_OBJECT_POOL_LIFO_ATTRIBUTE_NAME));
            config.setFairness(properties.getBoolean(CheetahConstants.FST_OBJECT_POOL_FAIRNESS_ATTRIBUTE_NAME));
            config.setTestOnBorrow(false);
            config.setTestOnReturn(false);
            config.setTestOnCreate(false);
            config.setTestWhileIdle(false);
            config.setNumTestsPerEvictionRun(-1);
        } catch (Exception e) {
            throw new IllegalArgumentException("Properties maybe isn't initialized");
        }

        return config;
    }
    
    public static GenericObjectPoolConfig createRedisObjectPoolConfig() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        try {
            config.setMaxTotal(CheetahConstants.CPUS * properties.getInteger(CheetahConstants.REDIS_OBJECT_POOL_MAX_TOTAL_ATTRIBUTE_NAME));
            config.setMaxIdle(CheetahConstants.CPUS * properties.getInteger(CheetahConstants.REDIS_OBJECT_POOL_MAX_IDLE_ATTRIBUTE_NAME));
            config.setMinIdle(CheetahConstants.CPUS * properties.getInteger(CheetahConstants.REDIS_OBJECT_POOL_MIN_IDLE_ATTRIBUTE_NAME));
            config.setMaxWaitMillis(properties.getLong(CheetahConstants.REDIS_OBJECT_POOL_MAX_WAIT_MILLIS_ATTRIBUTE_NAME));
            config.setTimeBetweenEvictionRunsMillis(properties.getLong(CheetahConstants.REDIS_OBJECT_POOL_TIME_BETWEEN_EVICTION_RUN_MILLIS_ATTRIBUTE_NAME));
            config.setMinEvictableIdleTimeMillis(properties.getLong(CheetahConstants.REDIS_OBJECT_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS_ATTRIBUTE_NAME));
            config.setSoftMinEvictableIdleTimeMillis(properties.getLong(CheetahConstants.REDIS_OBJECT_POOL_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS_ATTRIBUTE_NAME));
            config.setBlockWhenExhausted(properties.getBoolean(CheetahConstants.REDIS_OBJECT_POOL_BLOCK_WHEN_EXHAUSTED_ATTRIBUTE_NAME));
            config.setLifo(properties.getBoolean(CheetahConstants.REDIS_OBJECT_POOL_LIFO_ATTRIBUTE_NAME));
            config.setFairness(properties.getBoolean(CheetahConstants.REDIS_OBJECT_POOL_FAIRNESS_ATTRIBUTE_NAME));
            config.setTestOnBorrow(false);
            config.setTestOnReturn(false);
            config.setTestOnCreate(false);
            config.setTestWhileIdle(false);
            config.setNumTestsPerEvictionRun(-1);
        } catch (Exception e) {
            throw new IllegalArgumentException("Properties maybe isn't initialized");
        }

        return config;
    }
}