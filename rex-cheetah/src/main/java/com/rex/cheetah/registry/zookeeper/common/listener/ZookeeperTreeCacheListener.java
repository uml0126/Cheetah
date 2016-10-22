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
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

public abstract class ZookeeperTreeCacheListener extends ZookeeperCacheListener implements TreeCacheListener {
    protected TreeCache treeCache;
    
    public ZookeeperTreeCacheListener(CuratorFramework client, String path) throws Exception {
        super(client, path);
        
        treeCache = new TreeCache(client, path);
        treeCache.start();
        treeCache.getListenable().addListener(this);
    }
    
    public TreeCache getTreeCache() {
        return treeCache;
    }

    @Override
    public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
        TreeCacheEvent.Type type = event.getType();
        switch (type) {
            case INITIALIZED:
                initialized(event);
                break;
            case NODE_ADDED:
                nodeAdded(event);
                break;
            case NODE_UPDATED:
                nodeUpdated(event);
                break;
            case NODE_REMOVED:
                nodeRemoved(event);
                break;
            case CONNECTION_SUSPENDED:
                connectionSuspended(event);
                break;
            case CONNECTION_RECONNECTED:
                connectionReconnected(event);
                break;
            case CONNECTION_LOST:
                connectionLost(event);
                break;
        }
    }

    public abstract void initialized(TreeCacheEvent event) throws Exception;

    public abstract void nodeAdded(TreeCacheEvent event) throws Exception;

    public abstract void nodeUpdated(TreeCacheEvent event) throws Exception;

    public abstract void nodeRemoved(TreeCacheEvent event) throws Exception;

    public abstract void connectionSuspended(TreeCacheEvent event) throws Exception;

    public abstract void connectionReconnected(TreeCacheEvent event) throws Exception;

    public abstract void connectionLost(TreeCacheEvent event) throws Exception;
}