package com.rex.cheetah.protocol.mq;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import javax.jms.JMSException;
import javax.jms.Message;

import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.entity.SelectorType;

public class MQSelectorUtil {

    public static String getRequestSelector(Message message) throws JMSException {
        return message.getStringProperty(SelectorType.REQUEST_SELECTOR.toString());
    }

    public static void setRequestSelector(Message message, String selector) throws JMSException {
        message.setStringProperty(SelectorType.REQUEST_SELECTOR.toString(), selector);
    }

    public static void setRequestSelector(Message message, ApplicationEntity applicationEntity) throws JMSException {
        String selector = applicationEntity.toUrl();
        setRequestSelector(message, selector);
    }

    public static String getResponseSelector(Message message) throws JMSException {
        return message.getStringProperty(SelectorType.RESPONSE_SELECTOR.toString());
    }

    public static void setResponseSelector(Message message, String selector) throws JMSException {
        message.setStringProperty(SelectorType.RESPONSE_SELECTOR.toString(), selector);
    }

    public static void setResponseSelector(Message message, ApplicationEntity applicationEntity) throws JMSException {
        String selector = applicationEntity.toUrl();
        setResponseSelector(message, selector);
    }
}