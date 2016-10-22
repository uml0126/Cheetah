package com.rex.cheetah.registry.zookeeper.common.watcher;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.util.concurrent.Executor;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.BackgroundPathable;

public abstract class ZookeeperCallbackWatcher extends ZookeeperWatcher {

    private BackgroundCallback callback;
    private Object context;
    private Executor executor;

    public ZookeeperCallbackWatcher(CuratorFramework client, BackgroundCallback callback, Object context, Executor executor) {
        super(client);

        this.callback = callback;
        this.context = context;
        this.executor = executor;
    }

    @Override
    public void usingWatcher(BackgroundPathable<?> backgroundPathable, String path) throws Exception {
        backgroundPathable.inBackground(callback, context, executor).forPath(path);
    }
}