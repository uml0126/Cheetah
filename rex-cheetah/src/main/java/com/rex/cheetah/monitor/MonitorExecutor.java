package com.rex.cheetah.monitor;

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
import com.rex.cheetah.common.entity.MonitorStat;
import com.rex.cheetah.protocol.ProtocolMessage;

public interface MonitorExecutor extends CheetahDelegate {

    // 创建监控对象
    MonitorStat createMonitorStat(ProtocolMessage message);
    
    // 执行监控过程
    void execute(MonitorStat monitorStat) throws Exception;
}