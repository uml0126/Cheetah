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

public interface CheetahDelegate {
    // 获取属性句柄容器
    CheetahProperties getProperties();
    
    void setProperties(CheetahProperties properties);
    
    // 获取缓存容器
    CacheContainer getCacheContainer();

    void setCacheContainer(CacheContainer cacheContainer);
    
    // 获取执行器句柄容器
    ExecutorContainer getExecutorContainer();
    
    void setExecutorContainer(ExecutorContainer executorContainer);
    
    // 反射创建Delegate类
    <T> T createDelegate(String delegateClassId) throws Exception;
}