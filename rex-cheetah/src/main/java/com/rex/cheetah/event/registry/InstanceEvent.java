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
import com.rex.cheetah.event.eventbus.Event;

public abstract class InstanceEvent extends Event {
    private InstanceEventType eventType;
    private String interfaze;
    // 某个节点上下线事件触发后，回调返回该节点的服务/调用信息
    private ApplicationEntity applicationEntity;
    // 某个节点上下线事件触发后，回调返回跟该节点相同类型的服务/调用信息列表
    private List<ApplicationEntity> applicationEntityList;

    public InstanceEvent(InstanceEventType eventType, String interfaze, ApplicationEntity applicationEntity, List<ApplicationEntity> applicationEntityList) {
        super();

        this.eventType = eventType;
        this.interfaze = interfaze;
        this.applicationEntity = applicationEntity;
        this.applicationEntityList = applicationEntityList;
    }

    public InstanceEventType getEventType() {
        return eventType;
    }

    public String getInterface() {
        return interfaze;
    }

    public ApplicationEntity getApplicationEntity() {
        return applicationEntity;
    }
    
    public List<ApplicationEntity> getApplicationEntityList() {
        return applicationEntityList;
    }
}