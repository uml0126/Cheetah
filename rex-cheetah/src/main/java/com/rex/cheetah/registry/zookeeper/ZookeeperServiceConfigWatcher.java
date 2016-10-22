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

import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.config.ServiceConfig;
import com.rex.cheetah.common.container.CacheContainer;
import com.rex.cheetah.common.entity.ServiceEntity;
import com.rex.cheetah.registry.zookeeper.common.ZookeeperInvoker;
import com.rex.cheetah.registry.zookeeper.common.listener.ZookeeperNodeCacheListener;

public class ZookeeperServiceConfigWatcher extends ZookeeperNodeCacheListener {
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperServiceConfigWatcher.class);
    
    private String interfaze;
    
    private ZookeeperInvoker invoker;
    private CacheContainer cacheContainer;
    
    public ZookeeperServiceConfigWatcher(CuratorFramework client, String interfaze, ZookeeperInvoker invoker, CacheContainer cacheContainer, String path) throws Exception {
        super(client, path);
        
        this.interfaze = interfaze;
        this.invoker = invoker;
        this.cacheContainer = cacheContainer;
    }

    @Override
    public void nodeChanged() throws Exception {
        ServiceConfig serviceConfig = invoker.getObject(client, path, ServiceConfig.class);

        Map<String, ServiceConfig> serviceConfigMap = cacheContainer.getServiceConfigMap();
        serviceConfigMap.put(interfaze, serviceConfig);
        
        Map<String, ServiceEntity> serviceEntityMap = cacheContainer.getServiceEntityMap();
        ServiceEntity serviceEntity = serviceEntityMap.get(interfaze);
        long token = serviceConfig.getToken();
        serviceEntity.setDefaultToken(token);
        serviceEntity.setToken(token);
        
        LOG.info("Watched - service config is changed, interface={}", interfaze);
    }
}