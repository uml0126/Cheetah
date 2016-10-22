package com.rex.cheetah.common.delegate;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import com.rex.cheetah.common.container.CacheContainer;
import com.rex.cheetah.common.container.ExecutorContainer;
import com.rex.cheetah.common.property.CheetahProperties;
import com.rex.cheetah.common.util.ClassUtil;

public class CheetahDelegateImpl implements CheetahDelegate {
    protected CheetahProperties properties;
    protected CacheContainer cacheContainer;
    protected ExecutorContainer executorContainer;

    public CheetahDelegateImpl() {

    }
    
    @Override
    public CheetahProperties getProperties() {
        return properties;
    }

    @Override
    public void setProperties(CheetahProperties properties) {
        this.properties = properties;
    }

    @Override
    public CacheContainer getCacheContainer() {
        return cacheContainer;
    }

    @Override
    public void setCacheContainer(CacheContainer cacheContainer) {
        this.cacheContainer = cacheContainer;
    }

    @Override
    public ExecutorContainer getExecutorContainer() {
        return executorContainer;
    }

    @Override
    public void setExecutorContainer(ExecutorContainer executorContainer) {
        this.executorContainer = executorContainer;
    }
    
    @Override
    public <T> T createDelegate(String delegateClassId) throws Exception {
        String delegateClassName = properties.get(delegateClassId);

        T delegateInstance = ClassUtil.createInstance(delegateClassName);
        
        CheetahDelegate delegate = (CheetahDelegate) delegateInstance;
        delegate.setProperties(properties);
        delegate.setCacheContainer(cacheContainer);
        delegate.setExecutorContainer(executorContainer);
        
        return delegateInstance;
    }
}