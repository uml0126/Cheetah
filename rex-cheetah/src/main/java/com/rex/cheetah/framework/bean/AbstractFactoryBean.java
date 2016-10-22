package com.rex.cheetah.framework.bean;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.rex.cheetah.common.container.CacheContainer;
import com.rex.cheetah.common.container.ExecutorContainer;
import com.rex.cheetah.common.delegate.CheetahDelegate;
import com.rex.cheetah.common.property.CheetahProperties;
import com.rex.cheetah.common.util.ClassUtil;

@SuppressWarnings("all")
public abstract class AbstractFactoryBean implements ApplicationContextAware, FactoryBean, InitializingBean {
    protected ApplicationContext applicationContext;
    
    protected CheetahDelegate delegate;
    protected CheetahProperties properties;
    protected CacheContainer cacheContainer;
    protected ExecutorContainer executorContainer;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public CheetahDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(CheetahDelegate delegate) {
        this.delegate = delegate;
        this.properties = delegate.getProperties();
        this.cacheContainer = delegate.getCacheContainer();
        this.executorContainer = delegate.getExecutorContainer();
    }
    
    public CheetahProperties getProperties() {
        return properties;
    }

    public CacheContainer getCacheContainer() {
        return cacheContainer;
    }

    public ExecutorContainer getExecutorContainer() {
        return executorContainer;
    }
    
    protected <T> T createDelegate(String delegateClassId) throws Exception {        
        return delegate.createDelegate(delegateClassId);
    }
}