package com.rex.cheetah.protocol.mq;

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
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.entity.ServiceEntity;
import com.rex.cheetah.protocol.AbstractServerExecutor;
import com.rex.cheetah.protocol.ProtocolException;

public class MQServerExecutor extends AbstractServerExecutor implements MQExecutorDelegate {
    private static final Logger LOG = LoggerFactory.getLogger(MQServerExecutor.class);
    
    @Override
    public void start(final String interfaze, final ApplicationEntity applicationEntity) throws Exception {
        final String server = getServer(interfaze);
        
        final Map<String, MQContext> contextMap = MQCacheContainer.getServiceContextMap();

        final CyclicBarrier barrier = new CyclicBarrier(2);
        Executors.newCachedThreadPool().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                try {
                    MQContext context = contextMap.get(server);
                    if (context == null) {
                        context = new MQContext(MQServerExecutor.this);
                        context.initializeContext(interfaze, server);
                        contextMap.put(server, context);
                    }
                    context.initializeResponseContext(interfaze, applicationEntity, true);
                } catch (Exception e) {
                    LOG.error("Start MQ failed", e);
                }
                
                barrier.await();

                return null;
            }
        });

        barrier.await();
        
        try {
            contextMap.get(server).stopRetryNotification();
        } catch (Exception e) {
            throw new ProtocolException("Get MQ server failed, check it in config file", e);
        }
    }
    
    @Override
    public boolean started(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        String server = getServer(interfaze);
        
        Map<String, MQContext> contextMap = MQCacheContainer.getServiceContextMap();
                
        return contextMap.get(server) != null;
    }
    
    private String getServer(String interfaze) {
        Map<String, ServiceEntity> serviceEntityMap = cacheContainer.getServiceEntityMap();
        ServiceEntity serviceEntity = serviceEntityMap.get(interfaze);
        
        return serviceEntity.getServer();
    }
}