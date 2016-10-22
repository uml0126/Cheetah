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

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.container.CacheContainer;
import com.rex.cheetah.common.entity.MonitorEntity;
import com.rex.cheetah.event.registry.InstanceEventType;
import com.rex.cheetah.registry.zookeeper.common.ZookeeperInvoker;
import com.rex.cheetah.registry.zookeeper.common.listener.ZookeeperPathChildrenCacheListener;

public class ZookeeperMonitorInstanceWatcher extends ZookeeperPathChildrenCacheListener {
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperMonitorInstanceWatcher.class);

    private ZookeeperInvoker invoker;
    private CacheContainer cacheContainer;

    public ZookeeperMonitorInstanceWatcher(CuratorFramework client, ZookeeperInvoker invoker, CacheContainer cacheContainer, String path) throws Exception {
        super(client, path);

        this.invoker = invoker;
        this.cacheContainer = cacheContainer;
    }

    @Override
    public void initialized(PathChildrenCacheEvent event) throws Exception {

    }

    @Override
    public void childAdded(PathChildrenCacheEvent event) throws Exception {
        onEvent(event, InstanceEventType.ONLINE);
    }

    @Override
    public void childUpdated(PathChildrenCacheEvent event) throws Exception {

    }

    @Override
    public void childRemoved(PathChildrenCacheEvent event) throws Exception {
        onEvent(event, InstanceEventType.OFFLINE);
    }

    @Override
    public void connectionSuspended(PathChildrenCacheEvent event) throws Exception {

    }

    @Override
    public void connectionReconnected(PathChildrenCacheEvent event) throws Exception {

    }

    @Override
    public void connectionLost(PathChildrenCacheEvent event) throws Exception {

    }

    private void onEvent(PathChildrenCacheEvent event, InstanceEventType instanceEventType) throws Exception { 
        MonitorEntity monitorEntity = cacheContainer.getMonitorEntity();
        
        String childPath = event.getData().getPath();
        String address = childPath.substring(childPath.lastIndexOf("/") + 1);
        /*switch (instanceEventType) {
            case ONLINE:
                monitorEntity.addAddress(address);
                break;
            case OFFLINE:
                monitorEntity.removeAddress(address);
                break;
        }*/
        List<String> addresses = invoker.getChildNameList(client, path);
        monitorEntity.setAddresses(addresses);

        LOG.info("Watched {} - address={}", instanceEventType.toString(), address);
    }
}