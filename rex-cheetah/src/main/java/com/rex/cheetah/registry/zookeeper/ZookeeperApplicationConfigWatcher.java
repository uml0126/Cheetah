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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.config.ApplicationConfig;
import com.rex.cheetah.common.container.CacheContainer;
import com.rex.cheetah.registry.zookeeper.common.ZookeeperInvoker;
import com.rex.cheetah.registry.zookeeper.common.listener.ZookeeperNodeCacheListener;
import com.rex.cheetah.security.SecurityBootstrap;

public class ZookeeperApplicationConfigWatcher extends ZookeeperNodeCacheListener {
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperApplicationConfigWatcher.class);

    private ZookeeperInvoker invoker;
    private CacheContainer cacheContainer;

    public ZookeeperApplicationConfigWatcher(CuratorFramework client, ZookeeperInvoker invoker, CacheContainer cacheContainer, String path) throws Exception {
        super(client, path);

        this.invoker = invoker;
        this.cacheContainer = cacheContainer;
    }

    @Override
    public void nodeChanged() throws Exception {
        ApplicationConfig applicationConfig = invoker.getObject(client, path, ApplicationConfig.class);

        ApplicationConfig config = cacheContainer.getApplicationConfig();

        if (applicationConfig.getFrequency() != config.getFrequency()) {
            SecurityBootstrap securityBootstrap = cacheContainer.getSecurityBootstrap();
            if (securityBootstrap != null) {
                int frequency = applicationConfig.getFrequency();

                securityBootstrap.restart(frequency);
            }
        }

        cacheContainer.setApplicationConfig(applicationConfig);

        LOG.info("Watched - application config is changed");
    }
}