package com.rex.cheetah.protocol.kafka;

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

import com.google.common.collect.Maps;

public class KafkaMQCacheContainer {
    // 缓存Service所有MQ的服务器配置，key为server(MQ配置名)，例如kafka-1
    private static Map<String, KafkaMQContext> serviceContextMap = Maps.newConcurrentMap();

    // 缓存Reference所有MQ的服务器配置，key为server(MQ配置名)，例如kafka-1
    private static Map<String, KafkaMQContext> referenceContextMap = Maps.newConcurrentMap();
    
    public static Map<String, KafkaMQContext> getServiceContextMap() {
        return serviceContextMap;
    }

    public static Map<String, KafkaMQContext> getReferenceContextMap() {
        return referenceContextMap;
    }
}