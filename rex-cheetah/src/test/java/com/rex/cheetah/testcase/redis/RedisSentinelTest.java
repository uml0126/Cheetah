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

import redis.clients.jedis.Jedis;

import com.rex.cheetah.common.entity.MonitorStat;
import com.rex.cheetah.common.object.ObjectPoolFactory;
import com.rex.cheetah.common.property.CheetahProperties;
import com.rex.cheetah.common.property.CheetahPropertiesManager;
import com.rex.cheetah.common.thread.ThreadPoolFactory;
import com.rex.cheetah.protocol.redis.sentinel.RedisSentinelPoolFactory;
import com.rex.cheetah.serialization.json.JacksonSerializer;

public class RedisSentinelTest {
    private static final Logger LOG = LoggerFactory.getLogger(RedisSentinelTest.class);

    @Test
    public void test() throws Exception {
        CheetahProperties properties = CheetahPropertiesManager.getProperties();
        ObjectPoolFactory.initialize(properties);
        ThreadPoolFactory.initialize(properties);
        RedisSentinelPoolFactory.initialize(properties);

        Jedis jedis = RedisSentinelPoolFactory.getResource();
        del(jedis);
        hget(jedis);
    }

    protected void del(Jedis jedis) {
        for (int i = 0; i < 200; i++) {
            jedis.del("A1(" + i + ")");
            jedis.del("A2(" + i + ")");
        }
        LOG.info("Clear all data");
    }

    protected void hget(Jedis jedis) {
        Map<String, String> records = jedis.hgetAll("A1(1)");
        for (Map.Entry<String, String> entry : records.entrySet()) {
            MonitorStat monitorStat = JacksonSerializer.fromJson(entry.getValue(), MonitorStat.class);
            System.out.println(monitorStat.getException());
        }
    }

    protected void hset(Jedis jedis) {
        jedis.hset("abc", "1", "aaa");
        jedis.hset("abc", "2", "bbb");
        jedis.hset("abc", "3", "ccc");
        Map<String, String> value = jedis.hgetAll("abc");

        LOG.info("Value={}", value.toString());
    }
}