package com.rex.cheetah.registry.zookeeper;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.util.RandomUtil;
import com.rex.cheetah.serialization.SerializerExecutor;

public class ZookeeperApplicationEntityFactory {

    public static ApplicationEntity fromJson(String applicationJson) {
        ApplicationEntity applicationEntity = SerializerExecutor.fromJson(applicationJson, ApplicationEntity.class);

        return applicationEntity;
    }

    public static List<ApplicationEntity> fromJson(List<String> applicationJsonList) {
        List<ApplicationEntity> applicationEntityList = new ArrayList<ApplicationEntity>();
        if (CollectionUtils.isNotEmpty(applicationJsonList)) {
            for (String applicationJson : applicationJsonList) {
                ApplicationEntity applicationEntity = fromJson(applicationJson);
                
                // 在某个时刻，可能存在老节点未下线，而新节点又上线，并存的情况
                ApplicationEntity entity = getApplicationEntity(applicationEntityList, applicationEntity);
                if (entity != null) {
                    // 通过最新时间来控制缓存，保证缓存里的ApplicationEntity是最新且有效的
                    if (applicationEntity.getTime() > entity.getTime()) {
                        applicationEntityList.remove(entity);
                        applicationEntityList.add(applicationEntity);
                    }
                } else {
                    applicationEntityList.add(applicationEntity);
                }
            }
        }

        return applicationEntityList;
    }
    
    private static ApplicationEntity getApplicationEntity(List<ApplicationEntity> applicationEntityList, ApplicationEntity applicationEntity) {
        for (ApplicationEntity entity : applicationEntityList) {
            if (entity.equals(applicationEntity)) {
                return entity;
            }
        }
        
        return null;
    }
    
    public static String toJson(ApplicationEntity applicationEntity) {
        applicationEntity.setId(RandomUtil.uuidRandom());
        applicationEntity.setTime(System.currentTimeMillis());
        
        return SerializerExecutor.toJson(applicationEntity);
    }
}