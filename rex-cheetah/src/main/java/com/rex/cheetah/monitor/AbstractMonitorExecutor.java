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

import com.rex.cheetah.common.delegate.CheetahDelegateImpl;
import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.entity.LoadBalanceType;
import com.rex.cheetah.common.entity.MethodKey;
import com.rex.cheetah.common.entity.MonitorStat;
import com.rex.cheetah.common.entity.ProtocolEntity;
import com.rex.cheetah.common.entity.StrategyEntity;
import com.rex.cheetah.common.util.ExceptionUtil;
import com.rex.cheetah.protocol.ProtocolMessage;
import com.rex.cheetah.protocol.ProtocolRequest;

public abstract class AbstractMonitorExecutor extends CheetahDelegateImpl implements MonitorExecutor {

    @Override
    public MonitorStat createMonitorStat(ProtocolMessage message) {
        ProtocolEntity protocolEntity = cacheContainer.getProtocolEntity();
        StrategyEntity strategyEntity = cacheContainer.getStrategyEntity();
        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();

        MonitorStat monitorStat = new MonitorStat();
        monitorStat.setTraceId(message.getTraceId());
        monitorStat.setMessageId(message.getMessageId());
        monitorStat.setMessageType(message instanceof ProtocolRequest ? MonitorStat.MESSAGE_TYPE_REQUEST : MonitorStat.MESSAGE_TYPE_RESPONSE);
        monitorStat.setFromCluster(message.getFromCluster());
        monitorStat.setFromUrl(message.getFromUrl());
        monitorStat.setToCluster(message.getToCluster());
        monitorStat.setToUrl(message.getToUrl());
        monitorStat.setProcessStartTime(message.getProcessStartTime());
        monitorStat.setProcessEndTime(message.getProcessEndTime());
        monitorStat.setDeliverStartTime(message.getDeliverStartTime());
        monitorStat.setDeliverEndTime(message.getDeliverEndTime());
        monitorStat.setProtocol(protocolEntity.getType().toString());
        monitorStat.setApplication(applicationEntity.getApplication());
        monitorStat.setGroup(applicationEntity.getGroup());
        monitorStat.setInterfaze(message.getInterface());
        monitorStat.setParameterTypes(MethodKey.toParameterTypes(message.getParameterTypes()));
        monitorStat.setMethod(message.getMethod());
        monitorStat.setAsync(message.isAsync());
        monitorStat.setCallback(message.getCallback());
        monitorStat.setTimeout(message.getTimeout());
        monitorStat.setBroadcast(message.isBroadcast());
        if (strategyEntity != null) {
            LoadBalanceType loadBalanceType = strategyEntity.getLoadBalanceType();
            if (loadBalanceType != null) {
                monitorStat.setLoadBalance(loadBalanceType.toString());
            }
        }
        monitorStat.setFeedback(message.isFeedback());
        if (message.getException() != null) {
            monitorStat.setException(ExceptionUtil.toExceptionString(message.getException()));
        }

        return monitorStat;
    }
}