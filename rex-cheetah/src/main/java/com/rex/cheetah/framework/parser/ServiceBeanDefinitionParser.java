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
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.delegate.CheetahDelegate;
import com.rex.cheetah.common.entity.ProtocolEntity;
import com.rex.cheetah.common.entity.ServiceEntity;
import com.rex.cheetah.framework.bean.ServiceFactoryBean;
import com.rex.cheetah.framework.exception.FrameworkException;
import com.rex.cheetah.framework.exception.FrameworkExceptionFactory;
import com.rex.cheetah.protocol.ServerExecutor;
import com.rex.cheetah.protocol.ServerExecutorAdapter;
import com.rex.cheetah.security.SecurityExecutor;

public class ServiceBeanDefinitionParser extends AbstractExtensionBeanDefinitionParser {
    
    public ServiceBeanDefinitionParser(CheetahDelegate delegate) {
        super(delegate);
    }
    
    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        super.doParse(element, parserContext, builder);
        
        String namespaceElementName = properties.getString(CheetahConstants.NAMESPACE_ELEMENT_NAME);
        String serviceElementName = CheetahConstants.SERVICE_ELEMENT_NAME;
        String interfaceAttributeName = CheetahConstants.INTERFACE_ATTRIBUTE_NAME;
        String serverAttributeName = CheetahConstants.SERVER_ATTRIBUTE_NAME;
        String refAttributeName = CheetahConstants.REF_ATTRIBUTE_NAME;

        String interfaze = element.getAttribute(interfaceAttributeName);
        String server = element.getAttribute(serverAttributeName);
        String ref = element.getAttribute(refAttributeName);
        
        if (StringUtils.isEmpty(interfaze)) {
            throw FrameworkExceptionFactory.createAttributeMissingException(namespaceElementName, serviceElementName, interfaceAttributeName);
        }

        if (StringUtils.isEmpty(ref)) {
            throw FrameworkExceptionFactory.createAttributeMissingException(namespaceElementName, serviceElementName, refAttributeName);
        }
        
        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setInterface(interfaze);
        serviceEntity.setServer(server);
        
        builder.addPropertyValue(interfaceAttributeName, interfaze);
		builder.addPropertyValue(serviceElementName, new RuntimeBeanReference(ref));
        builder.addPropertyValue(createBeanName(ServiceEntity.class), serviceEntity);
        
        ServerExecutor serverExecutor = createServerExecutor();
        builder.addPropertyValue(createBeanName(ServerExecutor.class), serverExecutor);
        
        ServerExecutorAdapter serverExecutorAdapter = createServerExecutorAdapter();
        builder.addPropertyValue(createBeanName(ServerExecutorAdapter.class), serverExecutorAdapter);
        
        SecurityExecutor securityExecutor = createSecurityExecutor();
        builder.addPropertyValue(createBeanName(SecurityExecutor.class), securityExecutor);
    }
    
    protected ServerExecutor createServerExecutor() {
        ServerExecutor serverExecutor = executorContainer.getServerExecutor();
        if (serverExecutor == null) {
            ProtocolEntity protocolEntity = cacheContainer.getProtocolEntity();
            String serverExecutorId = protocolEntity.getServerExecutorId();
            
            try {
                serverExecutor = createDelegate(serverExecutorId);
            } catch (Exception e) {
                throw new FrameworkException("Creat ServerExecutor failed", e);
            }
            
            executorContainer.setServerExecutor(serverExecutor);
        }
        
        return serverExecutor;
    }
    
    protected ServerExecutorAdapter createServerExecutorAdapter() {
        ServerExecutorAdapter serverExecutorAdapter = executorContainer.getServerExecutorAdapter();
        if (serverExecutorAdapter == null) {
            String serverExecutorAdapterId = CheetahConstants.SERVER_EXECUTOR_ADAPTER_ID;
            try {
                serverExecutorAdapter = createDelegate(serverExecutorAdapterId);
            } catch (Exception e) {
                throw new FrameworkException("Creat ServerExecutorAdapter failed", e);
            }
            
            executorContainer.setServerExecutorAdapter(serverExecutorAdapter);
        }
        
        return serverExecutorAdapter;
    }
    
    protected SecurityExecutor createSecurityExecutor() {
        SecurityExecutor securityExecutor = executorContainer.getSecurityExecutor();
        if (securityExecutor == null) {
            String securityExecutorId = CheetahConstants.SECURITY_EXECUTOR_ID;
            try {
                securityExecutor = createDelegate(securityExecutorId);
            } catch (Exception e) {
                throw new FrameworkException("Creat SecurityExecutor failed", e);
            }
            
            executorContainer.setSecurityExecutor(securityExecutor);
        }

        return securityExecutor;
    }
    
    @Override
    protected Class<?> getBeanClass(Element element) {
        return ServiceFactoryBean.class;
    }
}