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

import com.rex.cheetah.common.entity.ProtocolType;

// 提供给外部程序所用
public interface RegistryLauncher {

    // 启动注册中心连接
    void start(String address, ProtocolType protocolType) throws Exception;

    // 停止注册中心连接
    void stop() throws Exception;

    // 获取注册中心执行器
    RegistryExecutor getRegistryExecutor();
}