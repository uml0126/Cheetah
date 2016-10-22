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

import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.entity.DestinationType;
import com.rex.cheetah.common.entity.ReferenceEntity;
import com.rex.cheetah.protocol.AbstractClientInterceptor;
import com.rex.cheetah.protocol.ClientInterceptorAdapter;
import com.rex.cheetah.protocol.ProtocolRequest;

public class KafkaMQClientInterceptor extends AbstractClientInterceptor {

    @Override
    public void invokeAsync(ProtocolRequest request) throws Exception {
        String interfaze = request.getInterface();
        
        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();
        
        String topic = KafkaMQDestinationUtil.createDestinationEntity(DestinationType.RESPONSE_QUEUE, interfaze, applicationEntity).toString();
        
        KafkaMQProducer producer = getProducer(interfaze);
        producer.produceRequest(topic, applicationEntity, request);
    }

    @Override
    public Object invokeSync(ProtocolRequest request) throws Exception {
        ClientInterceptorAdapter clientInterceptorAdapter = executorContainer.getClientInterceptorAdapter();

        return clientInterceptorAdapter.invokeSync(this, request);
    }

    @Override
    public void invokeBroadcast(ProtocolRequest request) throws Exception {        
        String interfaze = request.getInterface();
        
        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();
        
        String topic = KafkaMQDestinationUtil.createDestinationEntity(DestinationType.RESPONSE_TOPIC, interfaze, applicationEntity).toString();
        
        KafkaMQProducer producer = getProducer(interfaze);
        producer.produceRequest(topic, applicationEntity, request);
    }

    private KafkaMQProducer getProducer(String interfaze) {
        Map<String, ReferenceEntity> referenceEntityMap = cacheContainer.getReferenceEntityMap();
        ReferenceEntity referenceEntity = referenceEntityMap.get(interfaze);
        String server = referenceEntity.getServer();

        return KafkaMQCacheContainer.getReferenceContextMap().get(server).getProducer();
    }
}