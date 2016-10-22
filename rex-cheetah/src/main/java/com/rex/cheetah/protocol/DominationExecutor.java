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
import com.rex.cheetah.common.entity.ApplicationType;

public interface DominationExecutor extends CheetahDelegate {

    // 监控处理
    void handleMonitor(ProtocolMessage message);
    
    // EventBus异步事件处理
    void handleEvent(ProtocolMessage message, ApplicationType applicationType);
}