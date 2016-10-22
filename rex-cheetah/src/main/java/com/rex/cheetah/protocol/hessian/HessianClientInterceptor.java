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

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.callback.CheetahCallback;
import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.entity.ConnectionEntity;
import com.rex.cheetah.common.entity.MethodEntity;
import com.rex.cheetah.common.entity.MethodKey;
import com.rex.cheetah.common.thread.ThreadPoolFactory;
import com.rex.cheetah.event.protocol.ProtocolEventFactory;
import com.rex.cheetah.protocol.AbstractClientInterceptor;
import com.rex.cheetah.protocol.ClientExecutor;
import com.rex.cheetah.protocol.ProtocolRequest;
import com.rex.cheetah.protocol.redis.sentinel.RedisPublisher;
import com.rex.cheetah.protocol.redis.sentinel.RedisSentinelPoolFactory;
import com.rex.cheetah.security.SecurityException;
import com.rex.cheetah.security.SecurityExceptionFactory;
import com.rex.cheetah.cluster.loadbalance.LoadBalanceExecutor;

public class HessianClientInterceptor extends AbstractClientInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(HessianClientInterceptor.class);

    @SuppressWarnings("all")
    @Override
    public void invokeAsync(final ProtocolRequest request) throws Exception {
        final String interfaze = request.getInterface();
        
        LoadBalanceExecutor loadBalanceExecutor = executorContainer.getLoadBalanceExecutor();
        ConnectionEntity connectionEntity = null;
        try {
            connectionEntity = loadBalanceExecutor.loadBalance(interfaze);
        } catch (Exception e) {
            request.setException(e);
            ProtocolEventFactory.postClientProducerEvent(cacheContainer.getProtocolEntity().getType(), request);
            
            throw e;
        }
        
        if (connectionEntity == null) {
            return;
        }
        
        final ConnectionEntity connectEntity = connectionEntity;
        
        ThreadPoolFactory.createThreadPoolClientExecutor(interfaze).submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                MethodKey methodKey = request.createMethodKey();

                MethodEntity methodEntity = cacheContainer.getMethodEntity(interfaze, methodKey);
                CheetahCallback callback = methodEntity.getCallback();

                Object result = null;
                Exception exception = null;
                try {
                    result = invokeSync(request, connectEntity);
                } catch (Exception e) {
                    ApplicationEntity applicationEntity = connectEntity.getApplicationEntity();
                    if (HessianUtil.isConnectionException(e)) {                        
                        LOG.error("Invoke failed for server [{}:{}]", applicationEntity.getHost(), applicationEntity.getPort(), e);
                        
                        ClientExecutor clientExecutor = executorContainer.getClientExecutor();
                        clientExecutor.offline(interfaze, applicationEntity);
                        
                        LOG.info("Try to re-invoke for connection exception...");
                        
                        // 为单次调用补偿和重试
                        invokeAsync(request);
                        
                        exception = e;
                    }
                    
                    SecurityException securityException = SecurityExceptionFactory.createException(interfaze, applicationEntity, e);
                    if (securityException != null) {
                        exception = securityException;
                    }
                }

                if (callback != null) {
                    callback.call(result, exception);
                }

                return null;
            }
        });
    }

    @Override
    public Object invokeSync(ProtocolRequest request) throws Exception {
        String interfaze = request.getInterface();
        
        LoadBalanceExecutor loadBalanceExecutor = executorContainer.getLoadBalanceExecutor();
        ConnectionEntity connectionEntity = null;
        try {
            connectionEntity = loadBalanceExecutor.loadBalance(interfaze);
        } catch (Exception e) {
            request.setException(e);
            ProtocolEventFactory.postClientProducerEvent(cacheContainer.getProtocolEntity().getType(), request);
            
            throw e;
        }
        
        if (connectionEntity == null) {
            return null;
        }
                
        Object result = null;
        try {
            result = invokeSync(request, connectionEntity);
        } catch (Exception e) {
            ApplicationEntity applicationEntity = connectionEntity.getApplicationEntity();
            if (HessianUtil.isConnectionException(e)) {
                LOG.error("Invoke failed for server [{}:{}]", applicationEntity.getHost(), applicationEntity.getPort(), e);
                
                ClientExecutor clientExecutor = executorContainer.getClientExecutor();
                clientExecutor.offline(interfaze, applicationEntity);
                
                LOG.info("Try to re-invoke for connection exception...");
                
                // 为单次调用补偿和重试
                return invokeSync(request);
            }
            
            SecurityException securityException = SecurityExceptionFactory.createException(interfaze, applicationEntity, e);
            if (securityException != null) {
                throw securityException;
            }
            
            throw e;
        }

        return result;
    }
    
    private void invokeAsync(final ProtocolRequest request, final ConnectionEntity connectionEntity) throws Exception {
        String interfaze = request.getInterface();
        ThreadPoolFactory.createThreadPoolClientExecutor(interfaze).submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {                
                invokeSync(request, connectionEntity);

                return null;
            }
        });
    }
    
    private Object invokeSync(ProtocolRequest request, ConnectionEntity connectionEntity) throws Exception {
        String interfaze = request.getInterface();
        String methodName = request.getMethod();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] arguments = request.getParameters();

        Class<?> interfaceClass = Class.forName(interfaze);
        Method method = interfaceClass.getMethod(methodName, parameterTypes);
        
        Object proxy = connectionEntity.getConnectionHandler();
        Object result = method.invoke(proxy, arguments);

        return result;
    }

    @Override
    public void invokeBroadcast(ProtocolRequest request) throws Exception {
        boolean redisEnabled = RedisSentinelPoolFactory.enabled();
        if (redisEnabled) {
            invokeRedisBroadcast(request);
        } else {
            LOG.info("Redis broadcast is disabled, use round broadcast");
            
            invokeRoundBroadcast(request);
        }
    }
    
    private void invokeRedisBroadcast(ProtocolRequest request) throws Exception {
        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();
        
        RedisPublisher publisher = new RedisPublisher();
        publisher.publish(request, applicationEntity);
    }
    
    private void invokeRoundBroadcast(ProtocolRequest request) throws Exception {
        String interfaze = request.getInterface();

        List<ConnectionEntity> connectionEntityList = cacheContainer.getConnectionCacheEntity().getConnectionEntityList(interfaze);
        for (ConnectionEntity connectionEntity : connectionEntityList) {
            ApplicationEntity applicationEntity = connectionEntity.getApplicationEntity();
            try {
                invokeAsync(request, connectionEntity);
            } catch (Exception e) {
                LOG.error("Async broadcast failed, host={}, port={}, service={}, method={}", applicationEntity.getHost(), applicationEntity.getPort(), request.getInterface(), request.getMethod());
                throw e;
            }
        }
    }
}