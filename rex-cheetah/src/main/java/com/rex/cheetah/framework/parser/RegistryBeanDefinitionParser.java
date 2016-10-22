package com.rex.cheetah.framework.parser;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.delegate.CheetahDelegate;
import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.entity.PropertyType;
import com.rex.cheetah.common.entity.RegistryEntity;
import com.rex.cheetah.common.entity.RegistryType;
import com.rex.cheetah.common.object.ObjectPoolFactory;
import com.rex.cheetah.common.property.CheetahPropertiesExecutor;
import com.rex.cheetah.common.property.CheetahPropertiesManager;
import com.rex.cheetah.common.thread.ThreadPoolFactory;
import com.rex.cheetah.event.protocol.ProtocolEventFactory;
import com.rex.cheetah.event.smtp.SmtpEventFactory;
import com.rex.cheetah.framework.bean.RegistryFactoryBean;
import com.rex.cheetah.framework.exception.FrameworkException;
import com.rex.cheetah.protocol.redis.cluster.RedisClusterFactory;
import com.rex.cheetah.protocol.redis.sentinel.RedisSentinelPoolFactory;
import com.rex.cheetah.registry.RegistryExecutor;
import com.rex.cheetah.registry.RegistryInitializer;
import com.rex.cheetah.serialization.SerializerFactory;
import com.rex.cheetah.serialization.compression.CompressorFactory;

public class RegistryBeanDefinitionParser extends AbstractExtensionBeanDefinitionParser {
    private static final Logger LOG = LoggerFactory.getLogger(RegistryBeanDefinitionParser.class);
    
    public RegistryBeanDefinitionParser(CheetahDelegate delegate) {
        super(delegate);
    }
    
    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        super.doParse(element, parserContext, builder);
        
        String typeAttributeName = CheetahConstants.TYPE_ATTRIBUTE_NAME;
        String addressAttributeName = CheetahConstants.ADDRESS_ATTRIBUTE_NAME;
        String addressParameterName = CheetahConstants.REGISTRY_ADDRESS_PARAMETER_NAME;
        String configAttributeName = CheetahConstants.CONFIG_ATTRIBUTE_NAME;
        
        String type = element.getAttribute(typeAttributeName);        
        RegistryType registryType = null;
        if (StringUtils.isNotEmpty(type)) {
            registryType = RegistryType.fromString(type);
        } else {
            registryType = RegistryType.ZOOKEEPER;
        }
        
        LOG.info("Registry type is {}", registryType);
        
        String config = element.getAttribute(configAttributeName);
        PropertyType propertyType = null;
        if (StringUtils.isNotEmpty(config)) {
            propertyType = PropertyType.fromString(config);
        } else {
            propertyType = PropertyType.REMOTE;
        }
        
        LOG.info("Property type is {}", propertyType);
        
        // 在开启远程配置的前提下，先从外部参数化平台获取远程配置，如果失败，再从注册中心获取
        initializeConfiguration(propertyType);
        
        // 通过-DCheetahRegistryAddress获取值
        String address = System.getProperty(addressParameterName);
        if (StringUtils.isEmpty(address)) {
            address = element.getAttribute(addressAttributeName);
            if (StringUtils.isEmpty(address)) {
                try {
                    address = properties.getString(CheetahConstants.ZOOKEEPER_ADDRESS_ATTRIBUTE_NAME);
                } catch (Exception e) {

                }
                if (StringUtils.isEmpty(address)) {
                    throw new FrameworkException("Registry address is null");
                }
            }
        }
        
        LOG.info("Registry address is {}", address);
        
        RegistryEntity registryEntity = new RegistryEntity();
        registryEntity.setType(registryType);
        registryEntity.setAddress(address);
        registryEntity.setPropertyType(propertyType);
                
        cacheContainer.setRegistryEntity(registryEntity);
        builder.addPropertyValue(createBeanName(RegistryEntity.class), registryEntity);

        RegistryInitializer registryInitializer = createRegistryInitializer(registryType);
        builder.addPropertyValue(createBeanName(RegistryInitializer.class), registryInitializer);
        
        RegistryExecutor registryExecutor = createRegistryExecutor(registryType);
        builder.addPropertyValue(createBeanName(RegistryExecutor.class), registryExecutor);
        
        try {
            initializeRegistry(registryInitializer, registryExecutor, registryEntity);
        } catch (Exception e) {
            LOG.error("Initialize registry center failed", e);
        }
        
        try {
            initializeConfiguration(registryExecutor, propertyType);
        } catch (Exception e) {
            LOG.error("Initialize configuration failed", e);
        }
        
        initializeFactory();
    }
    
    protected RegistryInitializer createRegistryInitializer(RegistryType registryType) {
        RegistryInitializer registryInitializer = executorContainer.getRegistryInitializer();
        if (registryInitializer == null) {
            String zookeeperRegistryInitializerId = CheetahConstants.ZOOKEEPER_REGISTRY_INITIALIZER_ID;
            try {
                switch (registryType) {
                    case ZOOKEEPER:
                        registryInitializer = createDelegate(zookeeperRegistryInitializerId);
                        break;
                }
            } catch (Exception e) {
                throw new FrameworkException("Creat RegistryInitializer failed", e);
            }
            
            executorContainer.setRegistryInitializer(registryInitializer);
        }
        
        return registryInitializer;
    }
    
    protected RegistryExecutor createRegistryExecutor(RegistryType registryType) {
        RegistryExecutor registryExecutor = executorContainer.getRegistryExecutor();
        if (registryExecutor == null) {
            String zookeeperRegistryExecutorId = CheetahConstants.ZOOKEEPER_REGISTRY_EXECUTOR_ID;
            try {
                switch (registryType) {
                    case ZOOKEEPER:
                        registryExecutor = createDelegate(zookeeperRegistryExecutorId);
                        break;
                }
            } catch (Exception e) {
                throw new FrameworkException("Creat RegistryExecutor failed", e);
            }
            
            executorContainer.setRegistryExecutor(registryExecutor);
        }
        
        return registryExecutor;
    }
    
    // 初始化注册中心连接
    protected void initializeRegistry(RegistryInitializer registryInitializer, RegistryExecutor registryExecutor, RegistryEntity registryEntity) throws Exception {
        // 启动和注册中心的连接
        registryInitializer.start(registryEntity);
        registryExecutor.setRegistryInitializer(registryInitializer);
    }
    
    // 初始化远程配置信息，通过外部参数化平台的实现类(SPI方式)
    protected void initializeConfiguration(PropertyType propertyType) {
        if (propertyType != PropertyType.REMOTE) {
            return;
        }
        
        try {
            ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();

            CheetahPropertiesExecutor propertiesExecutor = CheetahPropertiesManager.initializePropertiesExecutor();

            CheetahPropertiesManager.initializeRemoteProperties(propertiesExecutor, applicationEntity);
        } catch (Exception e) {

        }
    }
    
    // 初始化远程配置信息，通过注册中心的参数化平台存储基地
    protected void initializeConfiguration(RegistryExecutor registryExecutor, PropertyType propertyType) throws Exception {
        if (propertyType != PropertyType.REMOTE) {
            return;
        }
        
        // 如果已经从外部参数化平台初始化过RemoteProperties，就不再从注册中心读取远程配置了
        if (CheetahPropertiesManager.getRemoteProperties() != null) {
            return;
        }
        
        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();
        
        // 初始化注册中心Configuration相关环境
        registryExecutor.registerConfigurationEnvironment();
        
        // 注册Configuration
        registryExecutor.registerConfiguration(applicationEntity);
        
        // 初始化注册中心的Configuration下远程配置
        CheetahPropertiesManager.initializeRemoteProperties(registryExecutor, applicationEntity);
    }
    
    // 初始化工厂
    protected void initializeFactory() {
        ObjectPoolFactory.initialize(properties);
        ThreadPoolFactory.initialize(properties);
        SerializerFactory.initialize(properties);
        CompressorFactory.initialize(properties);
        ProtocolEventFactory.initialize(properties);
        SmtpEventFactory.initialize(properties);
        RedisSentinelPoolFactory.initialize(properties);
        RedisClusterFactory.initialize(properties);
    }
    
    @Override
    protected Class<?> getBeanClass(Element element) {
        return RegistryFactoryBean.class;
    }
}