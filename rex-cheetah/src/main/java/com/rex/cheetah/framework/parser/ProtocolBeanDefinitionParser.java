package com.rex.cheetah.framework.parser;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.delegate.CheetahDelegate;
import com.rex.cheetah.common.entity.MQEntity;
import com.rex.cheetah.common.entity.ProtocolEntity;
import com.rex.cheetah.common.entity.ProtocolType;
import com.rex.cheetah.common.entity.WebServiceEntity;
import com.rex.cheetah.framework.bean.ProtocolFactoryBean;
import com.rex.cheetah.framework.exception.FrameworkException;

@SuppressWarnings("all")
public class ProtocolBeanDefinitionParser extends AbstractExtensionBeanDefinitionParser {
    private static final Logger LOG = LoggerFactory.getLogger(ProtocolBeanDefinitionParser.class);

    public ProtocolBeanDefinitionParser(CheetahDelegate delegate) {
        super(delegate);
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        super.doParse(element, parserContext, builder);

        String typeAttributeName = CheetahConstants.TYPE_ATTRIBUTE_NAME;
        String pathAttributeName = CheetahConstants.PATH_ATTRIBUTE_NAME;

        String nettyServerExecutorId = CheetahConstants.NETTY_SERVER_EXECUTOR_ID;
        String nettyClientExecutorId = CheetahConstants.NETTY_CLIENT_EXECUTOR_ID;
        String nettyClientInterceptorId = CheetahConstants.NETTY_CLIENT_INTERCEPTOR_ID;

        String hessianServerExecutorId = CheetahConstants.HESSIAN_SERVER_EXECUTOR_ID;
        String hessianClientExecutorId = CheetahConstants.HESSIAN_CLIENT_EXECUTOR_ID;
        String hessianClientInterceptorId = CheetahConstants.HESSIAN_CLIENT_INTERCEPTOR_ID;

        String mqServerExecutorId = CheetahConstants.MQ_SERVER_EXECUTOR_ID;
        String mqClientExecutorId = CheetahConstants.MQ_CLIENT_EXECUTOR_ID;
        String mqClientInterceptorId = CheetahConstants.MQ_CLIENT_INTERCEPTOR_ID;

        String kafkaServerExecutorId = CheetahConstants.KAFKA_SERVER_EXECUTOR_ID;
        String kafkaClientExecutorId = CheetahConstants.KAFKA_CLIENT_EXECUTOR_ID;
        String kafkaClientInterceptorId = CheetahConstants.KAFKA_CLIENT_INTERCEPTOR_ID;
        
        String activeMQQueueId = CheetahConstants.ACTIVE_MQ_QUEUE_ID;
        String activeMQTopicId = CheetahConstants.ACTIVE_MQ_TOPIC_ID;
        String activeMQJndiInitialContextFactoryId = CheetahConstants.ACTIVE_MQ_JNDI_INITIAL_CONTEXT_FACTORY_ID;
        String activeMQInitialConnectionFactory = CheetahConstants.ACTIVE_MQ_INITIAL_CONNECTION_FACTORY_ID;
        String activeMQPooledConnectionFactoryId = CheetahConstants.ACTIVE_MQ_POOLED_CONNECTION_FACTORY_ID;
        
        String tibcoQueueId = CheetahConstants.TIBCO_QUEUE_ID;
        String tibcoTopicId = CheetahConstants.TIBCO_TOPIC_ID;
        String tibcoJndiInitialContextFactoryId = CheetahConstants.TIBCO_JNDI_INITIAL_CONTEXT_FACTORY_ID;
        String tibcoInitialConnectionFactory = CheetahConstants.TIBCO_INITIAL_CONNECTION_FACTORY_ID;
        String tibcoPooledConnectionFactoryId = CheetahConstants.TIBCO_POOLED_CONNECTION_FACTORY_ID;

        String type = element.getAttribute(typeAttributeName);
        ProtocolType protocolType = null;
        if (StringUtils.isNotEmpty(type)) {
            protocolType = ProtocolType.fromString(type);
        } else {
            protocolType = ProtocolType.NETTY;
        }

        LOG.info("Protocol type is {}", protocolType);

        ProtocolEntity protocolEntity = new ProtocolEntity();
        protocolEntity.setType(protocolType);

        switch (protocolType) {
            case NETTY:
                protocolEntity.setServerExecutorId(nettyServerExecutorId);
                protocolEntity.setClientExecutorId(nettyClientExecutorId);
                protocolEntity.setClientInterceptorId(nettyClientInterceptorId);
                break;
            case HESSIAN:
                protocolEntity.setServerExecutorId(hessianServerExecutorId);
                protocolEntity.setClientExecutorId(hessianClientExecutorId);
                protocolEntity.setClientInterceptorId(hessianClientInterceptorId);
                
                String path = properties.getString(pathAttributeName);
                if (StringUtils.isEmpty(path)) {
                    throw new FrameworkException("Web path is missing for " + protocolType);
                }
                WebServiceEntity webServiceEntity = new WebServiceEntity();
                webServiceEntity.setPath(path);
                cacheContainer.setWebServiceEntity(webServiceEntity);
                break;
            case KAFKA:
                protocolEntity.setServerExecutorId(kafkaServerExecutorId);
                protocolEntity.setClientExecutorId(kafkaClientExecutorId);
                protocolEntity.setClientInterceptorId(kafkaClientInterceptorId);
                
                MQEntity kafkaEntity = new MQEntity();
                kafkaEntity.extractProperties(properties, protocolType);
                cacheContainer.setMQEntity(kafkaEntity);
                break;
            case ACTIVE_MQ:
                protocolEntity.setServerExecutorId(mqServerExecutorId);
                protocolEntity.setClientExecutorId(mqClientExecutorId);
                protocolEntity.setClientInterceptorId(mqClientInterceptorId);
                
                MQEntity activeMQEntity = new MQEntity();
                activeMQEntity.setQueueId(activeMQQueueId);
                activeMQEntity.setTopicId(activeMQTopicId);
                activeMQEntity.setJndiInitialContextFactoryId(activeMQJndiInitialContextFactoryId);
                activeMQEntity.setInitialConnectionFactoryId(activeMQInitialConnectionFactory);
                activeMQEntity.setPooledConnectionFactoryId(activeMQPooledConnectionFactoryId);
                activeMQEntity.extractProperties(properties, protocolType);
                cacheContainer.setMQEntity(activeMQEntity);
                break;
            case TIBCO:
                protocolEntity.setServerExecutorId(mqServerExecutorId);
                protocolEntity.setClientExecutorId(mqClientExecutorId);
                protocolEntity.setClientInterceptorId(mqClientInterceptorId);
                
                MQEntity tibcoEntity = new MQEntity();
                tibcoEntity.setQueueId(tibcoQueueId);
                tibcoEntity.setTopicId(tibcoTopicId);
                tibcoEntity.setJndiInitialContextFactoryId(tibcoJndiInitialContextFactoryId);
                tibcoEntity.setInitialConnectionFactoryId(tibcoInitialConnectionFactory);
                tibcoEntity.setPooledConnectionFactoryId(tibcoPooledConnectionFactoryId);
                tibcoEntity.extractProperties(properties, protocolType);
                cacheContainer.setMQEntity(tibcoEntity);
                break;
        }

        cacheContainer.setProtocolEntity(protocolEntity);
        builder.addPropertyValue(createBeanName(ProtocolEntity.class), protocolEntity);
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        return ProtocolFactoryBean.class;
    }
}