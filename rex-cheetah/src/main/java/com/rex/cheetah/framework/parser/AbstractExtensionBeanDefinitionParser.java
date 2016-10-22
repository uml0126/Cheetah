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

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.rex.cheetah.common.container.CacheContainer;
import com.rex.cheetah.common.container.ExecutorContainer;
import com.rex.cheetah.common.delegate.CheetahDelegate;
import com.rex.cheetah.common.property.CheetahProperties;
import com.rex.cheetah.common.util.ClassUtil;

public abstract class AbstractExtensionBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    private static final String DELEGATE = "delegate";
    
    protected CheetahDelegate delegate;
    protected CheetahProperties properties;
    protected CacheContainer cacheContainer;
    protected ExecutorContainer executorContainer;
    
    public AbstractExtensionBeanDefinitionParser(CheetahDelegate delegate) {
        this.delegate = delegate;
        this.properties = delegate.getProperties();
        this.cacheContainer = delegate.getCacheContainer();
        this.executorContainer = delegate.getExecutorContainer();
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) { 
        builder.addPropertyValue(DELEGATE, delegate);
    }
    
    protected <T> T createDelegate(String delegateClassId) throws Exception {        
        return delegate.createDelegate(delegateClassId);
    }
    
    protected String createBeanName(Class<?> clazz) {
        return ClassUtil.convertBeanName(clazz);
    }
}