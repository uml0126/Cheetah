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

import com.rex.cheetah.common.config.ReferenceConfig;
import com.rex.cheetah.common.container.CacheContainer;
import com.rex.cheetah.registry.zookeeper.common.ZookeeperInvoker;
import com.rex.cheetah.registry.zookeeper.common.listener.ZookeeperNodeCacheListener;

public class ZookeeperReferenceConfigWatcher extends ZookeeperNodeCacheListener {
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperReferenceConfigWatcher.class);
    
    private String interfaze;
    
    private ZookeeperInvoker invoker;
    private CacheContainer cacheContainer;
    
    public ZookeeperReferenceConfigWatcher(CuratorFramework client, String interfaze, ZookeeperInvoker invoker, CacheContainer cacheContainer, String path) throws Exception {
        super(client, path);
        
        this.interfaze = interfaze;
        this.invoker = invoker;
        this.cacheContainer = cacheContainer;
    }

    @Override
    public void nodeChanged() throws Exception {
        ReferenceConfig referenceConfig = invoker.getObject(client, path, ReferenceConfig.class);

        Map<String, ReferenceConfig> referenceConfigMap = cacheContainer.getReferenceConfigMap();
        referenceConfigMap.put(interfaze, referenceConfig);
        
        LOG.info("Watched - reference config is changed, interface={}", interfaze);
    }
}