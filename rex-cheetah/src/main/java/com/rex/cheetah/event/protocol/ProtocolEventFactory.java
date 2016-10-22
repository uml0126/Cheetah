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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.entity.ActionType;
import com.rex.cheetah.common.entity.ApplicationType;
import com.rex.cheetah.common.entity.ProtocolType;
import com.rex.cheetah.common.property.CheetahProperties;
import com.rex.cheetah.event.eventbus.EventControllerFactory;
import com.rex.cheetah.event.eventbus.EventControllerType;
import com.rex.cheetah.protocol.ProtocolMessage;

public class ProtocolEventFactory {
    private static final Logger LOG = LoggerFactory.getLogger(ProtocolEventFactory.class);
    
    private static boolean eventNotification = false;
    
    public static void initialize(CheetahProperties properties) {
        eventNotification = properties.getBoolean(CheetahConstants.EVENT_NOTIFICATION_ATTRIBUTE_NAME);
        if (eventNotification) {
            LOG.info("Event notification is enabled...");
        }
    }
    
    public static void postServerConsumerEvent(ProtocolType protocolType, ProtocolMessage message) {
        ApplicationType applicationType = ApplicationType.SERVICE;

        postConsumerEvent(applicationType, protocolType, message);
    }

    public static void postClientConsumerEvent(ProtocolType protocolType, ProtocolMessage message) {
        ApplicationType applicationType = ApplicationType.REFERENCE;

        postConsumerEvent(applicationType, protocolType, message);
    }

    public static void postConsumerEvent(ApplicationType applicationType, ProtocolType protocolType, ProtocolMessage message) {
        ActionType actionType = ActionType.CONSUME;

        postEvent(applicationType, actionType, protocolType, message);
    }

    public static void postServerProducerEvent(ProtocolType protocolType, ProtocolMessage message) {
        ApplicationType applicationType = ApplicationType.SERVICE;

        postProducerEvent(applicationType, protocolType, message);
    }

    public static void postClientProducerEvent(ProtocolType protocolType, ProtocolMessage message) {
        ApplicationType applicationType = ApplicationType.REFERENCE;

        postProducerEvent(applicationType, protocolType, message);
    }

    public static void postProducerEvent(ApplicationType applicationType, ProtocolType protocolType, ProtocolMessage message) {
        ActionType actionType = ActionType.PRODUCE;

        postEvent(applicationType, actionType, protocolType, message);
    }
    
    public static void postServerSystemEvent(ProtocolType protocolType, ProtocolMessage message) {
        ApplicationType applicationType = ApplicationType.SERVICE;

        postSystemEvent(applicationType, protocolType, message);
    }
    
    public static void postClientSystemEvent(ProtocolType protocolType, ProtocolMessage message) {
        ApplicationType applicationType = ApplicationType.REFERENCE;

        postSystemEvent(applicationType, protocolType, message);
    }
    
    public static void postSystemEvent(ApplicationType applicationType, ProtocolType protocolType, ProtocolMessage message) {
        ActionType actionType = ActionType.SYSTEM;

        postEvent(applicationType, actionType, protocolType, message);
    }

    public static void postEvent(ApplicationType applicationType, ActionType actionType, ProtocolType protocolType, ProtocolMessage message) {
        if (message.getException() == null) {
            return;
        }

        ProtocolEvent protocolEvent = new ProtocolEvent(applicationType, actionType, protocolType, message);
        
        if (eventNotification) {
            EventControllerFactory.getSingletonController(EventControllerType.ASYNC).post(protocolEvent);
        }
    }
}