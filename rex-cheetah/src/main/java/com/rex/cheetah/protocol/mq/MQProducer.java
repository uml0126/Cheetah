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

import java.util.concurrent.Callable;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.MessageCreator;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.entity.ProtocolType;
import com.rex.cheetah.common.thread.ThreadPoolFactory;
import com.rex.cheetah.event.protocol.ProtocolEventFactory;
import com.rex.cheetah.protocol.ProtocolRequest;
import com.rex.cheetah.protocol.ProtocolResponse;

public class MQProducer {
    private static final Logger LOG = LoggerFactory.getLogger(MQProducer.class);

    private ProtocolType protocolType;
    private MQBytesMessageConverter mqMessageConverter = new MQBytesMessageConverter();
    private MQTemplate mqTemplate;

    public void produceRequest(final Destination responseDestination, final Destination requestDestination, final ApplicationEntity applicationEntity, final ProtocolRequest request) throws Exception {        
        String interfaze = request.getInterface();
        ThreadPoolFactory.createThreadPoolClientExecutor(interfaze).submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                if (responseDestination == null) {
                    LOG.error("Response destination can't be null");
                    return null;
                }

                if (responseDestination == requestDestination) {
                    LOG.error("Response and request destinations can't be same");
                    return null;
                }
                
                try {
                    mqTemplate.send(responseDestination, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            Message message = mqMessageConverter.toMessage(request, session);
                            message.setBooleanProperty(CheetahConstants.ASYNC_ATTRIBUTE_NAME, request.isAsync());
                            message.setLongProperty(CheetahConstants.TIMEOUT_ATTRIBUTE_NAME, request.getTimeout());

                            if (requestDestination != null) {
                                message.setJMSReplyTo(requestDestination);
                            }
                            
                            MQSelectorUtil.setRequestSelector(message, applicationEntity);

                            return message;
                        }
                    });
                } catch (Exception e) {
                    LOG.error("Produce request failed", e);
                    
                    ProtocolEventFactory.postClientProducerEvent(protocolType, request);
                }

                return null;
            }
        });
    }
    
    public void produceResponse(final Destination requestDestination, final ApplicationEntity applicationEntity, final ProtocolResponse response, final String selector) {
        if (requestDestination == null) {
            LOG.error("Response destination can't be null");

            return;
        }

        try {
            mqTemplate.send(requestDestination, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    Message message = mqMessageConverter.toMessage(response, session);
                    message.setBooleanProperty(CheetahConstants.ASYNC_ATTRIBUTE_NAME, response.isAsync());
                    message.setLongProperty(CheetahConstants.TIMEOUT_ATTRIBUTE_NAME, response.getTimeout());

                    MQSelectorUtil.setRequestSelector(message, selector);
                    MQSelectorUtil.setResponseSelector(message, applicationEntity);

                    return message;
                }
            });
        } catch (Exception e) {
            LOG.error("Produce response failed", e);
            
            ProtocolEventFactory.postServerProducerEvent(protocolType, response);
        }
    }

    public ProtocolType getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(ProtocolType protocolType) {
        this.protocolType = protocolType;
    }

    public MQTemplate getMQTemplate() {
        return mqTemplate;
    }

    public void setMQTemplate(MQTemplate mqTemplate) {
        this.mqTemplate = mqTemplate;
    }
}