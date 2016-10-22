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

public interface ServerExecutor extends CheetahDelegate {
    // 服务端启动
    void start(String interfaze, ApplicationEntity applicationEntity) throws Exception;
    
    // 服务端是否启动
    boolean started(String interfaze, ApplicationEntity applicationEntity) throws Exception;
}