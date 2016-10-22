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

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.delegate.CheetahDelegate;
import com.rex.cheetah.common.entity.MethodEntity;
import com.rex.cheetah.common.entity.MethodKey;
import com.rex.cheetah.common.entity.ProtocolEntity;
import com.rex.cheetah.common.entity.ReferenceEntity;
import com.rex.cheetah.framework.bean.ReferenceFactoryBean;
import com.rex.cheetah.framework.exception.FrameworkException;
import com.rex.cheetah.framework.exception.FrameworkExceptionFactory;
import com.rex.cheetah.protocol.ClientExecutor;
import com.rex.cheetah.protocol.ClientExecutorAdapter;
import com.rex.cheetah.protocol.ClientInterceptorAdapter;

@SuppressWarnings("all")
public class ReferenceBeanDefinitionParser extends AbstractExtensionBeanDefinitionParser {

    public ReferenceBeanDefinitionParser(CheetahDelegate delegate) {
        super(delegate);
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        super.doParse(element, parserContext, builder);

        String namespaceElementName = properties.getString(CheetahConstants.NAMESPACE_ELEMENT_NAME);
        String referenceElementName = CheetahConstants.REFERENCE_ELEMENT_NAME;
        String methodElementName = CheetahConstants.METHOD_ELEMENT_NAME;
        String interfaceAttributeName = CheetahConstants.INTERFACE_ATTRIBUTE_NAME;
        String serverAttributeName = CheetahConstants.SERVER_ATTRIBUTE_NAME;
        
        String interfaze = element.getAttribute(interfaceAttributeName);
        String server = element.getAttribute(serverAttributeName);
        
        if (StringUtils.isEmpty(interfaze)) {
            throw FrameworkExceptionFactory.createAttributeMissingException(namespaceElementName, referenceElementName, interfaceAttributeName);
        }
        
        MethodEntity methodEntity = new MethodEntity();
        MethodBeanDefinitionParser methodBeanDefinitionParser = new MethodBeanDefinitionParser(delegate, methodEntity);
        methodBeanDefinitionParser.parseMethod(element, null, null);

        Map methodMap = parseMethodElements(element, parserContext, builder);
        
        ReferenceEntity referenceEntity = new ReferenceEntity();
        referenceEntity.setInterface(interfaze);
        referenceEntity.setServer(server);

        builder.addPropertyValue(interfaceAttributeName, interfaze);
        builder.addPropertyValue(createBeanName(MethodEntity.class), methodEntity);
        builder.addPropertyValue(methodElementName, methodMap);
        builder.addPropertyValue(createBeanName(ReferenceEntity.class), referenceEntity);
        
        ClientExecutor clientExecutor = createClientExecutor();
        builder.addPropertyValue(createBeanName(ClientExecutor.class), clientExecutor);
        
        ClientExecutorAdapter clientExecutorAdapter = createClientExecutorAdapter();
        builder.addPropertyValue(createBeanName(ClientExecutorAdapter.class), clientExecutorAdapter);
        
        ClientInterceptorAdapter clientInterceptorAdapter = createClientInterceptorAdapter();
        builder.addPropertyValue(createBeanName(ClientInterceptorAdapter.class), clientInterceptorAdapter);
    }

    private Map parseMethodElements(Element referenceElement, ParserContext parserContext, BeanDefinitionBuilder builder) {
        String methodElementName = CheetahConstants.METHOD_ELEMENT_NAME;
        String methodAttributeName = CheetahConstants.METHOD_ATTRIBUTE_NAME;
        String parameterTypesAttributeName = CheetahConstants.PARAMETER_TYPES_ATTRIBUTE_NAME;

        List<Element> methodElements = DomUtils.getChildElementsByTagName(referenceElement, methodElementName);

        ManagedMap methodMap = new ManagedMap(methodElements.size());
        methodMap.setMergeEnabled(true);
        methodMap.setSource(parserContext.getReaderContext().extractSource(referenceElement));

        for (Element methodElement : methodElements) {
            String method = methodElement.getAttribute(methodAttributeName);
            String parameterTypes = methodElement.getAttribute(parameterTypesAttributeName);
            
            MethodKey methodKey = new MethodKey();
            methodKey.setMethod(method);
            methodKey.setParameterTypes(parameterTypes);
            if (methodMap.containsKey(methodKey)) {
                throw FrameworkExceptionFactory.createMethodDuplicatedException(methodElementName, methodKey);
            }
            
            methodMap.put(methodKey, parserContext.getDelegate().parseCustomElement(methodElement, builder.getRawBeanDefinition()));
        }

        return methodMap;
    }
    
    protected ClientExecutor createClientExecutor() {
        ClientExecutor clientExecutor = executorContainer.getClientExecutor();
        if (clientExecutor == null) {
            ProtocolEntity protocolEntity = cacheContainer.getProtocolEntity();
            String clientExecutorId = protocolEntity.getClientExecutorId();

            try {
                clientExecutor = createDelegate(clientExecutorId);
            } catch (Exception e) {
                throw new FrameworkException("Creat ClientExecutor failed", e);
            }
            
            executorContainer.setClientExecutor(clientExecutor);
        }
        
        return clientExecutor;
    }
    
    protected ClientExecutorAdapter createClientExecutorAdapter() {
        ClientExecutorAdapter clientExecutorAdapter = executorContainer.getClientExecutorAdapter();
        if (clientExecutorAdapter == null) {
            String clientExecutorAdapterId = CheetahConstants.CLIENT_EXECUTOR_ADAPTER_ID;
            try {
                clientExecutorAdapter = createDelegate(clientExecutorAdapterId);
            } catch (Exception e) {
                throw new FrameworkException("Creat ClientExecutorAdapter failed", e);
            }
            
            executorContainer.setClientExecutorAdapter(clientExecutorAdapter);
        }
        
        return clientExecutorAdapter;
    }
    
    protected ClientInterceptorAdapter createClientInterceptorAdapter() {
        ClientInterceptorAdapter clientInterceptorAdapter = executorContainer.getClientInterceptorAdapter();
        if (clientInterceptorAdapter == null) {
            String clientInterceptorAdapterId = CheetahConstants.CLIENT_INTERCEPTOR_ADAPTER_ID;
            try {
                clientInterceptorAdapter = createDelegate(clientInterceptorAdapterId);
            } catch (Exception e) {
                throw new FrameworkException("Creat ClientInterceptorAdapter failed", e);
            }
            
            executorContainer.setClientInterceptorAdapter(clientInterceptorAdapter);
        }
        
        return clientInterceptorAdapter;
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        return ReferenceFactoryBean.class;
    }
}