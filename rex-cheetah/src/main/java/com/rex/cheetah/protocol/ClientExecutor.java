package com.rex.cheetah.protocol;

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
import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.entity.ConnectionEntity;

public interface ClientExecutor extends CheetahDelegate {

    // 客户端启动连接
    void start(String interfaze, ApplicationEntity applicationEntity) throws Exception;

    // 客户端是否启动
    boolean started(String interfaze, ApplicationEntity applicationEntity) throws Exception;

    // 客户端上线，更新缓存
    ConnectionEntity online(String interfaze, ApplicationEntity applicationEntity, Object connnectionHandler) throws Exception;

    // 客户端下线，更新缓存
    void offline(String interfaze, ApplicationEntity applicationEntity) throws Exception;
}