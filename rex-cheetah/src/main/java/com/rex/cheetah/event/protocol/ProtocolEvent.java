package com.rex.cheetah.event.protocol;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.entity.ActionType;
import com.rex.cheetah.common.entity.ApplicationType;
import com.rex.cheetah.common.entity.MethodKey;
import com.rex.cheetah.common.entity.ProtocolType;
import com.rex.cheetah.common.util.ExceptionUtil;
import com.rex.cheetah.event.eventbus.Event;
import com.rex.cheetah.protocol.ProtocolMessage;

public class ProtocolEvent extends Event implements Serializable {
    private static final long serialVersionUID = 8471746963028869465L;

    // 标识服务提供方，服务调用方
    private ApplicationType applicationType;

    // 标识系统事件，生产事件，消费事件
    private ActionType actionType;

    // 标识协议类型
    private ProtocolType protocolType;

    // 标识协议事件
    private ProtocolMessage protocolMessage;

    public ProtocolEvent(ApplicationType applicationType, ActionType actionType, ProtocolType protocolType, ProtocolMessage protocolMessage) {
        super();

        this.applicationType = applicationType;
        this.actionType = actionType;
        this.protocolType = protocolType;
        this.protocolMessage = protocolMessage;
    }

    public ApplicationType getApplicationType() {
        return applicationType;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public ProtocolType getProtocolType() {
        return protocolType;
    }

    public ProtocolMessage getProtocolMessage() {
        return protocolMessage;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("applicationType", applicationType);
        map.put("actionType", actionType);
        map.put("protocolType", protocolType);
        if (actionType != ActionType.SYSTEM) {
            map.put("traceId", protocolMessage.getTraceId());
            map.put("messageId", protocolMessage.getMessageId());
            map.put("fromCluster", protocolMessage.getFromCluster());
        }
        map.put("fromUrl", protocolMessage.getFromUrl());
        if (actionType != ActionType.SYSTEM) {
            map.put("toCluster", protocolMessage.getToCluster());
        }
        map.put("toUrl", protocolMessage.getToUrl());
        if (actionType != ActionType.SYSTEM) {
            map.put("processStartTime", new SimpleDateFormat(CheetahConstants.DATE_FORMAT).format(new Date(protocolMessage.getProcessStartTime())));
            map.put("processEndTime", new SimpleDateFormat(CheetahConstants.DATE_FORMAT).format(new Date(protocolMessage.getProcessEndTime())));
            map.put("deliverStartTime", new SimpleDateFormat(CheetahConstants.DATE_FORMAT).format(new Date(protocolMessage.getDeliverStartTime())));
            map.put("deliverEndTime", new SimpleDateFormat(CheetahConstants.DATE_FORMAT).format(new Date(protocolMessage.getDeliverEndTime())));
            map.put("processedTime", (protocolMessage.getProcessEndTime() - protocolMessage.getProcessStartTime()) + " ms");
            map.put("deliveredTime", (protocolMessage.getDeliverEndTime() - protocolMessage.getDeliverStartTime()) + " ms");
            map.put("totalTime", (protocolMessage.getProcessEndTime() - protocolMessage.getProcessStartTime() + protocolMessage.getDeliverEndTime() - protocolMessage.getDeliverStartTime()) + " ms");
            map.put("interface", protocolMessage.getInterface());
            map.put("method", protocolMessage.getMethod());
            map.put("parameterTypes", MethodKey.toParameterTypes(protocolMessage.getParameterTypes()));
            map.put("async", protocolMessage.isAsync());
            map.put("callback", protocolMessage.getCallback());
            map.put("timeout", protocolMessage.getTimeout());
            map.put("broadcast", protocolMessage.isBroadcast());
            map.put("feedback", protocolMessage.isFeedback());
        }
        map.put("exception", toException());

        return map;
    }
    
    public String toException() {
        return ExceptionUtil.toExceptionString(protocolMessage.getException());
    }
}