package com.rex.cheetah.protocol;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import com.rex.cheetah.common.delegate.CheetahDelegateImpl;
import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.entity.ConnectionEntity;

public abstract class AbstractClientExecutor extends CheetahDelegateImpl implements ClientExecutor {
    @Override
    public boolean started(String interfaze, ApplicationEntity applicationEntity) throws Exception {        
        return cacheContainer.getConnectionCacheEntity().contains(interfaze, applicationEntity);
    }

    // 只适用于非消息队列的中间件
    @Override
    public ConnectionEntity online(String interfaze, ApplicationEntity applicationEntity, Object connnectionHandler) throws Exception {
        // TCP调用是多个Interface共享一个通道，注册Interface后，如果对应的通道已经开启，不需要重复开启了
        // HTTP调用是单个Interface占用一个连接
        ConnectionEntity connectionEntity = new ConnectionEntity();
        connectionEntity.setApplicationEntity(applicationEntity);
        connectionEntity.setConnectionHandler(connnectionHandler);

        cacheContainer.getConnectionCacheEntity().online(interfaze, connectionEntity);

        return connectionEntity;
    }

    // 只适用于非消息队列的中间件
    @Override
    public void offline(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        // TCP调用是长连接，服务器挂掉会主动通知客户端，同时注册中心也会通知
        // HTTP调用短连接，服务器挂掉不会主动通知客户端，需要注册中心下线来通知
        cacheContainer.getConnectionCacheEntity().offline(interfaze, applicationEntity);
    }
}