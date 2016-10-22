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
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.entity.UserEntity;
import com.rex.cheetah.registry.zookeeper.common.ZookeeperInvoker;
import com.rex.cheetah.registry.zookeeper.common.listener.ZookeeperTreeCacheListener;

public class ZookeeperUserWatcher extends ZookeeperTreeCacheListener {
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperUserWatcher.class);

    private ZookeeperInvoker invoker;
    private ZookeeperUserWatcherCallback<UserEntity> callback;

    public ZookeeperUserWatcher(CuratorFramework client, ZookeeperInvoker invoker, ZookeeperUserWatcherCallback<UserEntity> callback, String path) throws Exception {
        super(client, path);

        this.invoker = invoker;
        this.callback = callback;
    }

    @Override
    public void initialized(TreeCacheEvent event) throws Exception {

    }

    @Override
    public void nodeAdded(TreeCacheEvent event) throws Exception {

    }

    @Override
    public void nodeUpdated(TreeCacheEvent event) throws Exception {
        UserEntity userEntity = invoker.getObject(client, path, UserEntity.class);

        LOG.info("Watched - user [{}] is changed", userEntity.getName());

        callback.onUserChanged(userEntity);
    }

    @Override
    public void nodeRemoved(TreeCacheEvent event) throws Exception {
        LOG.info("Watched - user [{}] is deleted", path);

        callback.onUserDeleted();
    }

    @Override
    public void connectionSuspended(TreeCacheEvent event) throws Exception {

    }

    @Override
    public void connectionReconnected(TreeCacheEvent event) throws Exception {

    }

    @Override
    public void connectionLost(TreeCacheEvent event) throws Exception {

    }
}