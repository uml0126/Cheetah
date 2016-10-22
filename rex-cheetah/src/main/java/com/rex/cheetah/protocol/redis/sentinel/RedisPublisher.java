package com.rex.cheetah.protocol.redis.sentinel;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.thread.ThreadPoolFactory;
import com.rex.cheetah.protocol.ProtocolRequest;
import com.rex.cheetah.serialization.SerializerExecutor;

public class RedisPublisher extends RedisHierachy {
    private static final Logger LOG = LoggerFactory.getLogger(RedisPublisher.class);

    public void publish(final ProtocolRequest request, final ApplicationEntity applicationEntity) throws Exception {
        final Jedis jedis = RedisSentinelPoolFactory.getResource();
        if (jedis == null) {
            LOG.error("No redis sentinel resource found, publish failed");
            
            return;
        }
        
        final String interfaze = request.getInterface();
        ThreadPoolFactory.createThreadPoolClientExecutor(interfaze).submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                String channel = createChannel(interfaze, applicationEntity);

                try {
                    String json = SerializerExecutor.toJson(request);
                    jedis.publish(channel, json);
                } catch (Exception e) {
                    LOG.error("Publish failed", e);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }

                return null;
            }
        });
    }
}