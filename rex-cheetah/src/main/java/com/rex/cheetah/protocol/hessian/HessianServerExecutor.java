package com.rex.cheetah.protocol.hessian;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.util.concurrent.atomic.AtomicBoolean;

import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.protocol.AbstractServerExecutor;
import com.rex.cheetah.protocol.redis.sentinel.RedisSentinelPoolFactory;
import com.rex.cheetah.protocol.redis.sentinel.RedisSubscriber;

public class HessianServerExecutor extends AbstractServerExecutor {
    private AtomicBoolean start = new AtomicBoolean(false);
    
    @Override
    public void start(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        boolean redisEnabled = RedisSentinelPoolFactory.enabled();
        if (redisEnabled) {
            RedisSubscriber subscriber = new RedisSubscriber(executorContainer);
            subscriber.subscribe(interfaze, applicationEntity);
        }
        
        boolean started = started(interfaze, applicationEntity);
        if (started) {
            return;
        }
        
        start.set(true);
    }

    @Override
    public boolean started(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        return start.get();
    }
}