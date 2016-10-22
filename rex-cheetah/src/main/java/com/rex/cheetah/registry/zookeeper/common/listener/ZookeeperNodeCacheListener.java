package com.rex.cheetah.registry.zookeeper.common.listener;

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
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;

public abstract class ZookeeperNodeCacheListener extends ZookeeperCacheListener implements NodeCacheListener {
    protected NodeCache nodeCache;

    public ZookeeperNodeCacheListener(CuratorFramework client, String path) throws Exception {
        super(client, path);
        
        nodeCache = new NodeCache(client, path, false);
        nodeCache.start(true);
        nodeCache.getListenable().addListener(this);
    }
    
    public NodeCache getNodeCache() {
        return nodeCache;
    }
}