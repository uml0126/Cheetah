package com.rex.cheetah.framework.bean;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.entity.MonitorEntity;
import com.rex.cheetah.monitor.MonitorExecutor;
import com.rex.cheetah.registry.RegistryExecutor;

public class MonitorFactoryBean extends AbstractFactoryBean {
    private static final Logger LOG = LoggerFactory.getLogger(MonitorFactoryBean.class);

    private MonitorEntity monitorEntity;
    private List<MonitorExecutor> monitorExecutors;

    @Override
    public void afterPropertiesSet() throws Exception {
        registerMonitor();
        
        watchMonitorInstance();
        
        LOG.info("MonitorFactoryBean has been initialized...");
    }
    
    // 初始化Monitor的注册信息
    protected void registerMonitor() throws Exception {
        boolean hasWebService = monitorEntity.hasWebService();
        if (hasWebService) {
            RegistryExecutor registryExecutor = executorContainer.getRegistryExecutor();

            try {
                List<String> monitorInstanceList = registryExecutor.getMonitorInstanceList();
                monitorEntity.setAddresses(monitorInstanceList);
            } catch (Exception e) {
                LOG.error("No monitors can be retrieved", e);
            }
        }
    }
    
    // 监听Monitor上下线，用来保持注册中心和本地缓存一致
    protected void watchMonitorInstance() throws Exception {
        boolean hasWebService = monitorEntity.hasWebService();
        if (hasWebService) {
            RegistryExecutor registryExecutor = executorContainer.getRegistryExecutor();
            
            try {
                registryExecutor.addMonitorInstanceWatcher();
            } catch (Exception e) {
                LOG.error("Add monitor watcher failed", e);
            }
        }
    }

    @Override
    public MonitorEntity getObject() throws Exception {
        return monitorEntity;
    }

    @Override
    public Class<MonitorEntity> getObjectType() {
        return MonitorEntity.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setMonitorEntity(MonitorEntity monitorEntity) {
        this.monitorEntity = monitorEntity;
    }

    public MonitorEntity getMonitorEntity() {
        return monitorEntity;
    }

    public void setMonitorExecutors(List<MonitorExecutor> monitorExecutors) {
        this.monitorExecutors = monitorExecutors;
    }

    public List<MonitorExecutor> getMonitorExecutors() {
        return monitorExecutors;
    }
}