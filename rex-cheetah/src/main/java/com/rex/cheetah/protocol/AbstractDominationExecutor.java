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

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.delegate.CheetahDelegateImpl;
import com.rex.cheetah.common.entity.ApplicationType;
import com.rex.cheetah.common.entity.MonitorStat;
import com.rex.cheetah.common.entity.ProtocolType;
import com.rex.cheetah.event.protocol.ProtocolEventFactory;
import com.rex.cheetah.monitor.MonitorExecutor;

public class AbstractDominationExecutor extends CheetahDelegateImpl implements DominationExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractDominationExecutor.class);

    @Override
    public void handleMonitor(ProtocolMessage message) {
        MonitorStat monitorStat = null;
        List<MonitorExecutor> monitorExecutors = getExecutorContainer().getMonitorExecutors();
        if (CollectionUtils.isNotEmpty(monitorExecutors)) {
            for (MonitorExecutor monitorExecutor : monitorExecutors) {
                if (monitorStat == null) {
                    monitorStat = monitorExecutor.createMonitorStat(message);
                }
                try {
                    monitorExecutor.execute(monitorStat);
                } catch (Exception e) {
                    LOG.error("Execute monitor failed, executor={}", monitorExecutor.getClass().getName());
                }
            }
        }
    }

    @Override
    public void handleEvent(ProtocolMessage message, ApplicationType applicationType) {
        ProtocolType protocolType = getCacheContainer().getProtocolEntity().getType();
        
        ProtocolEventFactory.postConsumerEvent(applicationType, protocolType, message);
    }
}