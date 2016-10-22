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

import com.rex.cheetah.common.container.ExecutorContainer;
import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.entity.ApplicationType;
import com.rex.cheetah.event.eventbus.EventControllerFactory;
import com.rex.cheetah.event.eventbus.EventControllerType;
import com.rex.cheetah.event.registry.InstanceEvent;
import com.rex.cheetah.event.registry.InstanceEventType;
import com.rex.cheetah.event.registry.ReferenceInstanceEvent;
import com.rex.cheetah.event.registry.ServiceInstanceEvent;
import com.rex.cheetah.registry.zookeeper.common.ZookeeperInvoker;
import com.rex.cheetah.registry.zookeeper.common.listener.ZookeeperPathChildrenCacheListener;
import com.rex.cheetah.cluster.consistency.ConsistencyExecutor;

public class ZookeeperInstanceWatcher extends ZookeeperPathChildrenCacheListener {
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperInstanceWatcher.class);

    private ZookeeperInvoker invoker;
    private ExecutorContainer executorContainer;
    private ApplicationType applicationType;
    private String interfaze;

    public ZookeeperInstanceWatcher(CuratorFramework client, ZookeeperInvoker invoker, ExecutorContainer executorContainer, ApplicationType applicationType, String interfaze, String path) throws Exception {
        super(client, path);

        this.invoker = invoker;
        this.executorContainer = executorContainer;
        this.applicationType = applicationType;
        this.interfaze = interfaze;
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
        String childPath = event.getData().getPath();
        String applicationJson = childPath.substring(childPath.lastIndexOf("/") + 1);
        ApplicationEntity applicationEntity = ZookeeperApplicationEntityFactory.fromJson(applicationJson);
        
        List<String> applicationJsonList = invoker.getChildNameList(client, path);
        List<ApplicationEntity> applicationEntityList = ZookeeperApplicationEntityFactory.fromJson(applicationJsonList);
        
        InstanceEvent instanceEvent = null;
        switch (applicationType) {
            case SERVICE:
                instanceEvent = new ServiceInstanceEvent(instanceEventType, interfaze, applicationEntity, applicationEntityList);
                if (executorContainer != null) {
                    ConsistencyExecutor consistencyExecutor = executorContainer.getConsistencyExecutor();
                    if (consistencyExecutor != null) {
                        consistencyExecutor.consist((ServiceInstanceEvent) instanceEvent);
                    }
                }
                break;
            case REFERENCE:
                instanceEvent = new ReferenceInstanceEvent(instanceEventType, interfaze, applicationEntity, applicationEntityList);
                break;
        }
        
        EventControllerFactory.getController(instanceEvent.toString(), EventControllerType.ASYNC).post(instanceEvent);

        LOG.info("Watched {} {} - interface={}, {}", applicationType.toString(), instanceEventType.toString(), interfaze, applicationEntity.toString());
    }
}