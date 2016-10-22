package com.rex.cheetah.event.registry;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Rex
 * @email uml0126@126.com
 * @version 1.0
 */

import java.util.List;

import com.rex.cheetah.common.entity.ApplicationEntity;

public class ServiceInstanceEvent extends InstanceEvent {
    public ServiceInstanceEvent(InstanceEventType eventType, String interfaze, ApplicationEntity applicationEntity, List<ApplicationEntity> applicationEntityList) {
        super(eventType, interfaze, applicationEntity, applicationEntityList);
    }
    
    @Override
    public String toString() {
        return getEventName();
    }
    
    public static String getEventName() {
        return ServiceInstanceEvent.class.getName(); 
    }
}