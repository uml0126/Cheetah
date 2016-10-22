package com.rex.cheetah.registry.zookeeper;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Rex
 * @email uml0126@126.com
 * @version 1.0
 */

import com.google.common.eventbus.Subscribe;
import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.entity.ProtocolType;
import com.rex.cheetah.event.eventbus.EventControllerFactory;
import com.rex.cheetah.event.eventbus.EventControllerType;
import com.rex.cheetah.event.registry.ReferenceInstanceEvent;
import com.rex.cheetah.event.registry.ServiceInstanceEvent;
import com.rex.cheetah.registry.RegistryExecutor;
import com.rex.cheetah.registry.RegistryLauncher;

public abstract class ZookeeperInstanceEventInterceptor {
    private RegistryLauncher registryLauncher;
    
    public ZookeeperInstanceEventInterceptor() {
        EventControllerFactory.getController(ServiceInstanceEvent.getEventName(), EventControllerType.ASYNC).register(this);
        EventControllerFactory.getController(ReferenceInstanceEvent.getEventName(), EventControllerType.ASYNC).register(this);
    }
    
    public void start(String address, ProtocolType protocolType) throws Exception {
        registryLauncher = new ZookeeperRegistryLauncher();
        registryLauncher.start(address, protocolType);
    }
    
    public void stop() throws Exception {
        registryLauncher.stop();
    }
    
    public RegistryExecutor getRegistryExecutor() {
        return registryLauncher.getRegistryExecutor();
    }
    
    public void addServiceInstanceWatcher(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        getRegistryExecutor().addServiceInstanceWatcher(interfaze, applicationEntity);
    }
    
    public void addReferenceInstanceWatcher(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        getRegistryExecutor().addReferenceInstanceWatcher(interfaze, applicationEntity);
    }

    // 监听Service上下线
    @Subscribe
    public void listen(ServiceInstanceEvent event) {
        onEvent(event);
    }
    
    // 监听Reference上下线
    @Subscribe
    public void listen(ReferenceInstanceEvent event) {
        onEvent(event);
    }
    
    protected abstract void onEvent(ServiceInstanceEvent event);
    
    protected abstract void onEvent(ReferenceInstanceEvent event);
}