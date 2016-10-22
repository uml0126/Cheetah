package com.rex.cheetah.protocol.redis.cluster;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.io.IOException;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.object.ObjectPoolFactory;
import com.rex.cheetah.common.property.CheetahProperties;

/**
 * 非线程安全类，initialize和close需要在单线程里面执行
 */
public class RedisClusterFactory {
    private static final Logger LOG = LoggerFactory.getLogger(RedisClusterFactory.class);

    private static CheetahProperties properties;
    private static JedisCluster cluster;

    public static void initialize(CheetahProperties properties) {
        RedisClusterFactory.properties = properties;

        String clusterValue = null;
        try {
            clusterValue = properties.getString(CheetahConstants.REDIS_CLUSTER_ATTRIBUTE_NAME);
        } catch (Exception e) {
            LOG.warn("Redis cluster address is null, redis won't start");

            return;
        }

        if (StringUtils.isEmpty(clusterValue)) {
            LOG.warn("Redis cluster address is null, redis won't start");

            return;
        }

        try {
            HashSet<HostAndPort> clusterSet = new HashSet<HostAndPort>();
            String[] clusterArray = StringUtils.split(clusterValue, ";");
            for (String cluster : clusterArray) {
                String[] info = StringUtils.split(cluster, ":");
                clusterSet.add(new HostAndPort(info[0].trim(), Integer.valueOf(info[1].trim())));
            }

            cluster = new JedisCluster(clusterSet,
                    properties.getInteger(CheetahConstants.REDIS_CONNECTION_TIMEOUT_ATTRIBUTE_NAME),
                    properties.getInteger(CheetahConstants.REDIS_SO_TIMEOUT_ATTRIBUTE_NAME),
                    properties.getInteger(CheetahConstants.REDIS_MAX_REDIRECTIONS_ATTRIBUTE_NAME),
                    ObjectPoolFactory.createRedisObjectPoolConfig());
            LOG.info("Redis cluster is initialized...");
        } catch (Exception e) {
            LOG.error("Redis cluster is initialized failed", e);
        }
    }

    public static CheetahProperties getProperties() {
        return properties;
    }

    public static JedisCluster getCluster() {
        return cluster;
    }

    public static void close() throws IOException {
        if (cluster != null) {
            cluster.close();
            cluster = null;
        }
    }

    public static boolean enabled() {
        return cluster != null;
    }
}