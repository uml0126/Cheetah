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

public class MQTopicDestinationContainer {
    
    // 缓存所有的异步响应Destination，对应为持久化容器(Topic)，key为interface
    private Map<String, Destination> asyncResponseDestinationMap = Maps.newConcurrentMap();

    public Map<String, Destination> getAsyncResponseDestinationMap() {
        return asyncResponseDestinationMap;
    }
}