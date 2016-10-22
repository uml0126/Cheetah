package com.rex.cheetah.trace.service;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.util.Arrays;

import com.rex.cheetah.common.entity.ActionType;
import com.rex.cheetah.common.entity.ApplicationType;
import com.rex.cheetah.common.entity.ProtocolType;
import com.rex.cheetah.common.util.ExceptionUtil;
import com.rex.cheetah.event.protocol.ProtocolEvent;
import com.rex.cheetah.event.protocol.ProtocolEventInterceptor;
import com.rex.cheetah.protocol.ProtocolMessage;

public class ServiceEventInterceptor extends ProtocolEventInterceptor {
    @Override
    protected void onEvent(ProtocolEvent event) {
        ApplicationType applicationType = event.getApplicationType();
        ActionType actionType = event.getActionType();
        ProtocolType protocolType = event.getProtocolType();
        ProtocolMessage protocolMessage = event.getProtocolMessage();

        if (Constants.PRINT) {
            System.out.println("--------------------收到异步事件通知--------------------");
            System.out.println("Application type=" + applicationType);
            System.out.println("Action type=" + actionType);
            System.out.println("Protocol type=" + protocolType);
            if (actionType != ActionType.SYSTEM) {
                System.out.println("Trace id=" + protocolMessage.getTraceId());
                System.out.println("Interface=" + protocolMessage.getInterface());
                System.out.println("Method=" + protocolMessage.getMethod());
                System.out.println("Parameter types=" + Arrays.asList(protocolMessage.getParameterTypes()));
                System.out.println("Parameters=" + Arrays.asList(protocolMessage.getParameters()));
            } else {
                System.out.println("From url=" + protocolMessage.getFromUrl());
                System.out.println("To url=" + protocolMessage.getToUrl());
            }
            System.out.println("Exception=" + ExceptionUtil.toExceptionString(protocolMessage.getException()));
            System.out.println("-------------------------------------------------------");
        }
    }
}