package com.rex.cheetah.protocol.mq;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.util.Map;

import javax.jms.Destination;

import com.google.common.collect.Maps;

public class MQQueueDestinationContainer extends MQTopicDestinationContainer {
    // 缓存所有的同步请求Destination，对应为非持久化容器(Queue)，key为interface
    private Map<String, Destination> syncRequestDestinationMap = Maps.newConcurrentMap();

    // 缓存所有的同步响应Destination，对应为非持久化容器(Queue)，key为interface
    private Map<String, Destination> syncResponseDestinationMap = Maps.newConcurrentMap();
    
    // 缓存所有的异步请求Destination，对应为持久化容器(Queue)，key为interface
    private Map<String, Destination> asyncRequestDestinationMap = Maps.newConcurrentMap();
    
    // 缓存所有的异步响应Destination，对应为持久化容器(Queue)，key为interface
    private Map<String, Destination> asyncResponseDestinationMap = Maps.newConcurrentMap();

    public Map<String, Destination> getSyncRequestDestinationMap() {
        return syncRequestDestinationMap;
    }

    public Map<String, Destination> getSyncResponseDestinationMap() {
        return syncResponseDestinationMap;
    }

    public Map<String, Destination> getAsyncRequestDestinationMap() {
        return asyncRequestDestinationMap;
    }

    public Map<String, Destination> getAsyncResponseDestinationMap() {
        return asyncResponseDestinationMap;
    }
}