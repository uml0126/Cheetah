package com.rex.cheetah.registry;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import com.rex.cheetah.common.delegate.CheetahDelegate;
import com.rex.cheetah.common.entity.RegistryEntity;
import com.rex.cheetah.common.property.CheetahProperties;

public interface RegistryInitializer extends CheetahDelegate {
    
    // 启动和注册中心的连接
    void start(RegistryEntity registryEntity) throws Exception;

    // 启动和注册中心的连接
    void start(RegistryEntity registryEntity, CheetahProperties properties) throws Exception;

    // 停止注册中心的连接
    void stop() throws Exception;
}