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
import org.apache.curator.utils.PathUtils;

import com.rex.cheetah.registry.zookeeper.common.ZookeeperException;

public abstract class ZookeeperCacheListener {
    protected CuratorFramework client;
    protected String path;

    public ZookeeperCacheListener(CuratorFramework client, String path) {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }
        
        PathUtils.validatePath(path);
        
        this.client = client;
        this.path = path;
    }
    
    public CuratorFramework getClient() {
        return client;
    }

    public String getPath() {
        return path;
    }
}