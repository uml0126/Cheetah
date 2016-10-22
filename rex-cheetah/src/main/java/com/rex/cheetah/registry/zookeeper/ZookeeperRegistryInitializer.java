package com.rex.cheetah.registry.zookeeper;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import org.apache.curator.framework.CuratorFramework;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.delegate.CheetahDelegateImpl; 
import com.rex.cheetah.common.entity.RegistryEntity;
import com.rex.cheetah.common.property.CheetahProperties;
import com.rex.cheetah.registry.RegistryInitializer;
import com.rex.cheetah.registry.zookeeper.common.ZookeeperException;
import com.rex.cheetah.registry.zookeeper.common.ZookeeperInvoker;

public class ZookeeperRegistryInitializer extends CheetahDelegateImpl implements RegistryInitializer {
    private ZookeeperInvoker invoker = new ZookeeperInvoker();
    private CuratorFramework client;

    @Override
    public void start(RegistryEntity registryEntity) throws Exception {
        if (client != null) {
            throw new ZookeeperException("Zookeeper has started");
        }

        if (properties == null) {
            throw new ZookeeperException("properties is null");
        }

        String address = registryEntity.getAddress();
        int sessionTimeout = properties.getInteger(CheetahConstants.ZOOKEEPER_SESSION_TIMOUT_ATTRIBUTE_NAME);
        int connectTimeout = properties.getInteger(CheetahConstants.ZOOKEEPER_CONNECT_TIMEOUT_ATTRIBUTE_NAME);
        int connectWaitTime = properties.getInteger(CheetahConstants.ZOOKEEPER_CONNECT_WAIT_TIME_ATTRIBUTE_NAME);
        client = invoker.create(address, sessionTimeout, connectTimeout, connectWaitTime);
        invoker.startAndBlock(client);
    }

    @Override
    public void start(RegistryEntity registryEntity, CheetahProperties properties) throws Exception {
        if (client != null) {
            throw new ZookeeperException("Zookeeper has started");
        }

        setProperties(properties);

        start(registryEntity);
    }

    @Override
    public void stop() throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        invoker.close(client);
    }

    public ZookeeperInvoker getInvoker() {
        return invoker;
    }

    public CuratorFramework getClient() {
        return client;
    }
}