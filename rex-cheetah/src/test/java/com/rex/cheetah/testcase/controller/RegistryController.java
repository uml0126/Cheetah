package com.rex.cheetah.testcase.controller;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import org.junit.Test;

import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.entity.ProtocolType;
import com.rex.cheetah.registry.RegistryExecutor;
import com.rex.cheetah.registry.RegistryLauncher;
import com.rex.cheetah.registry.zookeeper.ZookeeperRegistryLauncher;

public class RegistryController {
    
    @Test
    public void test() throws Exception {
        // 启动注册中心连接
        RegistryLauncher registryLauncher = new ZookeeperRegistryLauncher();
        registryLauncher.start("localhost:2181", ProtocolType.NETTY);

        RegistryExecutor registryExecutor = registryLauncher.getRegistryExecutor();

        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setApplication("APP-IOS");
        applicationEntity.setGroup("POA_EA_INF");
        String interfaze = "com.rex.cheetah.test.service.UserService";
        
        registryExecutor.resetApplication(applicationEntity);
        registryExecutor.resetService(interfaze, applicationEntity);
        registryExecutor.resetReference(interfaze, applicationEntity);
//        registryExecutor.modifyApplicationFrequency(applicationEntity, 60000);
//        registryExecutor.modifyServiceSecretKey(interfaze, applicationEntity, "123456");
//        registryExecutor.modifyServiceVersion(interfaze, applicationEntity, 1);
//        registryExecutor.modifyServiceToken(interfaze, applicationEntity, 10);
//        registryExecutor.modifyReferenceSecretKey(interfaze, applicationEntity, "123456");
//        registryExecutor.modifyReferenceVersion(interfaze, applicationEntity, 1);
      
        // 停止注册中心连接
        // registryLauncher.stop();
        
        System.in.read();
    }
}