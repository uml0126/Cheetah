package com.rex.cheetah.common.property;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import com.rex.cheetah.common.entity.ApplicationEntity;

// 从外部接口获取配置文件
public interface CheetahPropertiesExecutor {
    
    // 获取Property文本配置信息
    String retrieveProperty(ApplicationEntity applicationEntity) throws Exception;
    
    // 持久化Property文本配置信息
    void persistProperty(String property, ApplicationEntity applicationEntity) throws Exception;
}