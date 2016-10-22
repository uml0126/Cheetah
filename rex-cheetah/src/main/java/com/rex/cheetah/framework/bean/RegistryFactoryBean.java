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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

import com.rex.cheetah.common.config.ApplicationConfig;
import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.entity.ProtocolEntity;
import com.rex.cheetah.common.entity.RegistryEntity;
import com.rex.cheetah.registry.RegistryExecutor;
import com.rex.cheetah.registry.RegistryInitializer;
import com.rex.cheetah.serialization.SerializerException;

public class RegistryFactoryBean extends AbstractFactoryBean implements DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(RegistryFactoryBean.class);

    private RegistryEntity registryEntity;
    private RegistryInitializer registryInitializer;
    private RegistryExecutor registryExecutor;

    @Override
    public void afterPropertiesSet() throws Exception {
        initializeRegistry();
        
        registerApplication();
        
        LOG.info("Connect and register to Registry Center successfully, address={}", registryEntity.getAddress());
        
        LOG.info("RegistryFactoryBean has been initialized...");
    }

    // 初始化注册中心
    protected void initializeRegistry() throws Exception {
        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();
        ProtocolEntity protocolEntity = cacheContainer.getProtocolEntity();

        // 设置上下文对象
        registryExecutor.setProtocolEntity(protocolEntity);
        
        // 初始化注册中心Application相关环境
        registryExecutor.registerApplicationEnvironment(applicationEntity);
        
        // 初始化注册中心Monitor相关环境
        registryExecutor.registerMonitorEnvironment();
        
        // 初始化注册中心User相关环境
        registryExecutor.registerUserEnvironment();
    }
    
    // 注册应用信息
    protected void registerApplication() throws Exception {
        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();
        
        // 注册Application
        registryExecutor.registerApplication(applicationEntity);

        // 持久化Application配置信息
        ApplicationConfig applicationConfig = null;
        try {
            applicationConfig = registryExecutor.retrieveApplication(applicationEntity);
        } catch (SerializerException e) {
            LOG.warn("ApplicationConfig class was upgraded, Registry Center will initialize it again");
        }
        if (applicationConfig == null) {
            applicationConfig = new ApplicationConfig();
            applicationConfig.setApplication(applicationEntity.getApplication());
            applicationConfig.setGroup(applicationEntity.getGroup());
            applicationConfig.setFrequency(properties.getInteger(CheetahConstants.FREQUENCY_ATTRIBUTE_NAME));
            
            registryExecutor.persistApplication(applicationConfig);
        }
        cacheContainer.setApplicationConfig(applicationConfig);
        
        // Application配置信息更改后通知服务端和客户端
        registryExecutor.addApplicationConfigWatcher(applicationConfig);
        
        // 应用与注册中心断开后重连成功，应该重新写入应用信息
        registryExecutor.addReconnectionListener();
    }
    
    @Override
    public RegistryEntity getObject() throws Exception {
        return registryEntity;
    }

    @Override
    public Class<RegistryEntity> getObjectType() {
        return RegistryEntity.class;
    }

    @Override
    public void destroy() throws Exception {
        registryInitializer.stop();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setRegistryEntity(RegistryEntity registryEntity) {
        this.registryEntity = registryEntity;
    }

    public RegistryEntity getRegistryEntity() {
        return registryEntity;
    }

    public RegistryInitializer getRegistryInitializer() {
        return registryInitializer;
    }

    public void setRegistryInitializer(RegistryInitializer registryInitializer) {
        this.registryInitializer = registryInitializer;
    }

    public void setRegistryExecutor(RegistryExecutor registryExecutor) {
        this.registryExecutor = registryExecutor;
    }
    
    public RegistryExecutor getRegistryExecutor() {
        return registryExecutor;
    }
}