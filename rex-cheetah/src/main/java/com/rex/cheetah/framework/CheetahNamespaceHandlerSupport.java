package com.rex.cheetah.framework;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.container.CacheContainer;
import com.rex.cheetah.common.container.ExecutorContainer;
import com.rex.cheetah.common.delegate.CheetahDelegate;
import com.rex.cheetah.common.delegate.CheetahDelegateImpl;
import com.rex.cheetah.common.property.CheetahProperties;
import com.rex.cheetah.common.property.CheetahPropertiesManager;
import com.rex.cheetah.framework.parser.ApplicationBeanDefinitionParser;
import com.rex.cheetah.framework.parser.MethodBeanDefinitionParser;
import com.rex.cheetah.framework.parser.MonitorBeanDefinitionParser;
import com.rex.cheetah.framework.parser.ProtocolBeanDefinitionParser;
import com.rex.cheetah.framework.parser.ReferenceBeanDefinitionParser;
import com.rex.cheetah.framework.parser.RegistryBeanDefinitionParser;
import com.rex.cheetah.framework.parser.ServiceBeanDefinitionParser;
import com.rex.cheetah.framework.parser.StrategyBeanDefinitionParser;

public class CheetahNamespaceHandlerSupport extends NamespaceHandlerSupport {
    @Override
    public void init() {
        CheetahProperties properties = CheetahPropertiesManager.getProperties();
                
        CacheContainer cacheContainer = new CacheContainer();
        ExecutorContainer executorContainer = new ExecutorContainer();
        
        CheetahDelegate delegate = new CheetahDelegateImpl();
        delegate.setProperties(properties);
        delegate.setCacheContainer(cacheContainer);
        delegate.setExecutorContainer(executorContainer);

        registerBeanDefinitionParser(CheetahConstants.APPLICATION_ELEMENT_NAME, new ApplicationBeanDefinitionParser(delegate));
        registerBeanDefinitionParser(CheetahConstants.REGISTRY_ELEMENT_NAME, new RegistryBeanDefinitionParser(delegate));
        registerBeanDefinitionParser(CheetahConstants.PROTOCOL_ELEMENT_NAME, new ProtocolBeanDefinitionParser(delegate));
        registerBeanDefinitionParser(CheetahConstants.STRATEGY_ELEMENT_NAME, new StrategyBeanDefinitionParser(delegate));
        registerBeanDefinitionParser(CheetahConstants.MONITOR_ELEMENT_NAME, new MonitorBeanDefinitionParser(delegate));
        registerBeanDefinitionParser(CheetahConstants.SERVICE_ELEMENT_NAME, new ServiceBeanDefinitionParser(delegate));
        registerBeanDefinitionParser(CheetahConstants.REFERENCE_ELEMENT_NAME, new ReferenceBeanDefinitionParser(delegate));
        registerBeanDefinitionParser(CheetahConstants.METHOD_ELEMENT_NAME, new MethodBeanDefinitionParser(delegate));
    }
}