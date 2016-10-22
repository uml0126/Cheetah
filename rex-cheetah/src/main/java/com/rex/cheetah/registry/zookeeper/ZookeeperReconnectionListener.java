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

import org.apache.commons.collections4.MapUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.container.CacheContainer;
import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.entity.ReferenceEntity;
import com.rex.cheetah.common.entity.ServiceEntity;

public class ZookeeperReconnectionListener implements ConnectionStateListener {
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperReconnectionListener.class);

    private ZookeeperRegistryExecutor registryExecutor;

    public ZookeeperReconnectionListener(ZookeeperRegistryExecutor registryExecutor) {
        this.registryExecutor = registryExecutor;
    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState state) {
        if (state == ConnectionState.RECONNECTED) {
            LOG.info("Zookeeper session is timeout, register and add watcher again");
            
            CacheContainer cacheContainer = registryExecutor.getCacheContainer();
            ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();

            /*try {
                if (client.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
                    ApplicationConfig applicationConfig = cacheContainer.getApplicationConfig();
                    registryExecutor.addApplicationConfigWatcher(applicationConfig);
                }
            } catch (Exception e) {
                LOG.error("Re-add application config watcher failed", e);
            }*/
            
            try {
                if (client.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
                    Map<String, ServiceEntity> serviceEntityMap = cacheContainer.getServiceEntityMap();
                    if (MapUtils.isNotEmpty(serviceEntityMap)) {
                        for (Map.Entry<String, ServiceEntity> entry : serviceEntityMap.entrySet()) {
                            String interfaze = entry.getKey();
                            registryExecutor.registerService(interfaze, applicationEntity);
                            // registryExecutor.addServiceConfigWatcher(interfaze, applicationEntity);
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error("Re-register services and add watcher failed", e);
            }

            try {
                if (client.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
                    Map<String, ReferenceEntity> referenceEntityMap = cacheContainer.getReferenceEntityMap();
                    if (MapUtils.isNotEmpty(referenceEntityMap)) {
                        for (Map.Entry<String, ReferenceEntity> entry : referenceEntityMap.entrySet()) {
                            String interfaze = entry.getKey();
                            registryExecutor.registerReference(interfaze, applicationEntity);
                            // registryExecutor.addReferenceConfigWatcher(interfaze, applicationEntity);
                            // registryExecutor.addServiceInstanceWatcher(interfaze, applicationEntity);
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error("Re-register references and add watcher failed", e);
            }
            
            /*MonitorEntity monitorEntity = cacheContainer.getMonitorEntity();
            if (monitorEntity != null) {
                boolean hasWebService = monitorEntity.hasWebService();
                if (hasWebService) {
                    try {
                        if (client.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
                            registryExecutor.addMonitorInstanceWatcher();
                        }
                    } catch (Exception e) {
                        LOG.error("Re-add monitor watcher failed", e);
                    }
                }
            }*/
        }
    }
}