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

import com.rex.cheetah.common.entity.ProtocolEntity;
import com.rex.cheetah.common.entity.ProtocolType;
import com.rex.cheetah.common.entity.RegistryEntity;
import com.rex.cheetah.common.property.CheetahProperties;
import com.rex.cheetah.common.property.CheetahPropertiesManager;
import com.rex.cheetah.registry.RegistryExecutor;
import com.rex.cheetah.registry.RegistryInitializer;
import com.rex.cheetah.registry.RegistryLauncher;

public class ZookeeperRegistryLauncher implements RegistryLauncher {
    private RegistryInitializer registryInitializer;
    private RegistryExecutor registryExecutor;

    @Override
    public void start(String address, ProtocolType protocolType) throws Exception {
        // 读取配置文件
        CheetahProperties properties = CheetahPropertiesManager.getProperties();

        RegistryEntity registryEntity = new RegistryEntity();
        registryEntity.setAddress(address);

        ProtocolEntity protocolEntity = new ProtocolEntity();
        protocolEntity.setType(protocolType);

        // 启动Zookeeper连接
        registryInitializer = new ZookeeperRegistryInitializer();
        registryInitializer.start(registryEntity, properties);

        registryExecutor = new ZookeeperRegistryExecutor();
        registryExecutor.setRegistryInitializer(registryInitializer);
        registryExecutor.setProperties(properties);
        registryExecutor.setProtocolEntity(protocolEntity);
    }

    @Override    
    public void stop() throws Exception {
        // 停止Zookeeper连接
        registryInitializer.stop();
    }

    @Override    
    public RegistryExecutor getRegistryExecutor() {
        return registryExecutor;
    }
}