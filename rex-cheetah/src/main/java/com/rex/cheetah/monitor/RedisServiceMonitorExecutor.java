package com.rex.cheetah.monitor;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.entity.MonitorStat;
import com.rex.cheetah.common.thread.ThreadPoolFactory;
import com.rex.cheetah.common.util.RandomUtil;
import com.rex.cheetah.protocol.redis.cluster.RedisClusterFactory;
import com.rex.cheetah.protocol.redis.sentinel.RedisSentinelPoolFactory;
import com.rex.cheetah.serialization.SerializerExecutor;

public class RedisServiceMonitorExecutor extends AbstractMonitorExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(RedisServiceMonitorExecutor.class);

    @Override
    public void execute(final MonitorStat monitorStat) throws Exception {
        final String traceId = monitorStat.getTraceId();
        if (StringUtils.isEmpty(traceId)) {
            LOG.error("Trace ID is null, monitor stat can't be put into redis");

            return;
        }

        if (RedisSentinelPoolFactory.enabled()) {
            executeToSentinel(traceId, monitorStat);
        } else if (RedisClusterFactory.enabled()) {
            executeToCluster(traceId, monitorStat);
        }
    }

    public void executeToSentinel(final String traceId, final MonitorStat monitorStat) throws Exception {
        final Jedis jedis = RedisSentinelPoolFactory.getResource();
        if (jedis == null) {
            LOG.error("No redis sentinel resource found, execute failed");

            return;
        }

        ThreadPoolFactory.createThreadPoolDefaultExecutor().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                try {
                    jedis.hset(traceId, RandomUtil.uuidRandom(), SerializerExecutor.toJson(monitorStat));
                    jedis.pexpire(traceId, properties.getLong(CheetahConstants.REDIS_DATA_EXPIRATION_ATTRIBUTE_NAME));
                } catch (Exception e) {
                    LOG.error("Execute failed", e);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }

                return null;
            }
        });
    }

    public void executeToCluster(final String traceId, final MonitorStat monitorStat) throws Exception {
        final JedisCluster cluster = RedisClusterFactory.getCluster();
        if (cluster == null) {
            LOG.error("No redis cluster found, execute failed");

            return;
        }

        ThreadPoolFactory.createThreadPoolDefaultExecutor().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                try {
                    cluster.hset(traceId, RandomUtil.uuidRandom(), SerializerExecutor.toJson(monitorStat));
                    cluster.pexpire(traceId, properties.getLong(CheetahConstants.REDIS_DATA_EXPIRATION_ATTRIBUTE_NAME));
                } catch (Exception e) {
                    LOG.error("Execute failed", e);
                } finally {
                    if (cluster != null) {
                        cluster.close();
                    }
                }

                return null;
            }
        });
    }
}