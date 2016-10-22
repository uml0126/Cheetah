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
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.container.ExecutorContainer;
import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.property.CheetahProperties;
import com.rex.cheetah.protocol.ProtocolRequest;
import com.rex.cheetah.protocol.ProtocolResponse;
import com.rex.cheetah.protocol.ServerExecutorAdapter;
import com.rex.cheetah.serialization.SerializerExecutor;

public class RedisSubscriber extends RedisHierachy {
    private static final Logger LOG = LoggerFactory.getLogger(RedisSubscriber.class);
    
    private ExecutorContainer executorContainer;
    
    public RedisSubscriber(ExecutorContainer executorContainer) {
        this.executorContainer = executorContainer;
    }

    public void subscribe(final String interfaze, final ApplicationEntity applicationEntity) throws Exception {
        final Jedis jedis = RedisSentinelPoolFactory.getResource();
        if (jedis == null) {
            LOG.error("No redis sentinel resource found, subscribe failed");
            
            return;
        }
        
        Executors.newCachedThreadPool().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                String channel = createChannel(interfaze, applicationEntity);

                try {
                    jedis.subscribe(new JedisPubSub() {
                        @Override
                        public void onMessage(String channel, String message) {
                            try {
                                ProtocolResponse response = new ProtocolResponse();
                                ProtocolRequest request = SerializerExecutor.fromJson(message, ProtocolRequest.class);

                                ServerExecutorAdapter serverExecutorAdapter = executorContainer.getServerExecutorAdapter();
                                serverExecutorAdapter.handle(request, response);
                            } catch (Exception e) {
                                LOG.error("Subscribe failed, channel={}", channel, e);
                            }
                        }
                    }, channel); // 子线程在这里阻塞
                } catch (Exception e) {
                    LOG.error("Subscribe failed, reconnect it", e);
                    if (jedis != null) {
                        jedis.close();
                    }
                } finally {
                    try {
                        CheetahProperties properties = RedisSentinelPoolFactory.getProperties();
                        TimeUnit.SECONDS.sleep(properties.getLong(CheetahConstants.REDIS_RECONNECTION_WAIT_ATTRIBUTE_NAME));
                    } catch (InterruptedException e) {
                    }
                    subscribe(interfaze, applicationEntity);
                }

                return null;
            }
        });
    }
}