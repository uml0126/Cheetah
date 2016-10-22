package com.rex.cheetah.testcase.redis;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisCluster;

import com.rex.cheetah.common.entity.MonitorStat;
import com.rex.cheetah.common.object.ObjectPoolFactory;
import com.rex.cheetah.common.property.CheetahProperties;
import com.rex.cheetah.common.property.CheetahPropertiesManager;
import com.rex.cheetah.common.thread.ThreadPoolFactory;
import com.rex.cheetah.protocol.redis.cluster.RedisClusterFactory;
import com.rex.cheetah.serialization.json.JacksonSerializer;

public class RedisClusterTest {
    private static final Logger LOG = LoggerFactory.getLogger(RedisClusterTest.class);

    @Test
    public void test() throws Exception {
        CheetahProperties properties = CheetahPropertiesManager.getProperties();
        ObjectPoolFactory.initialize(properties);
        ThreadPoolFactory.initialize(properties);
        RedisClusterFactory.initialize(properties);

        JedisCluster cluster = RedisClusterFactory.getCluster();
        del(cluster);
        hget(cluster);
    }

    protected void del(JedisCluster cluster) {
        for (int i = 0; i < 200; i++) {
            cluster.del("A1(" + i + ")");
            cluster.del("A2(" + i + ")");
        }
        LOG.info("Clear all data");
    }

    protected void hget(JedisCluster cluster) {        
        Map<String, String> records = cluster.hgetAll("A1(1)");
        for (Map.Entry<String, String> entry : records.entrySet()) {
            MonitorStat monitorStat = JacksonSerializer.fromJson(entry.getValue(), MonitorStat.class);
            System.out.println(monitorStat.getException());
        }
    }

    protected void hset(JedisCluster cluster) {
        cluster.hset("abc", "1", "aaa");
        cluster.hset("abc", "2", "bbb");
        cluster.hset("abc", "3", "ccc");
        Map<String, String> value = cluster.hgetAll("abc");

        LOG.info("Value={}", value.toString());
    }
}