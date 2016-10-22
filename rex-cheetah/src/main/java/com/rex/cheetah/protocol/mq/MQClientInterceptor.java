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

import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.entity.ReferenceEntity;
import com.rex.cheetah.protocol.AbstractClientInterceptor;
import com.rex.cheetah.protocol.ClientInterceptorAdapter;
import com.rex.cheetah.protocol.ProtocolRequest;

public class MQClientInterceptor extends AbstractClientInterceptor {
    
    @Override
    public void invokeAsync(ProtocolRequest request) throws Exception {
        String interfaze = request.getInterface();
        boolean isAsync = request.isAsync();
        
        Destination queueResponseDestination = null;
        Destination queueRequestDestination = null;
        
        if (isAsync) {
            queueResponseDestination = MQCacheContainer.getMQQueueDestinationContainer().getAsyncResponseDestinationMap().get(interfaze);
            queueRequestDestination = MQCacheContainer.getMQQueueDestinationContainer().getAsyncRequestDestinationMap().get(interfaze);
        } else {
            queueResponseDestination = MQCacheContainer.getMQQueueDestinationContainer().getSyncResponseDestinationMap().get(interfaze);
            queueRequestDestination = MQCacheContainer.getMQQueueDestinationContainer().getSyncRequestDestinationMap().get(interfaze);
        }
        
        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();
        
        MQProducer mqProducer = getMQProducer(interfaze);
        mqProducer.produceRequest(queueResponseDestination, queueRequestDestination, applicationEntity, request);
    }

    @Override
    public Object invokeSync(ProtocolRequest request) throws Exception {
        ClientInterceptorAdapter clientInterceptorAdapter = executorContainer.getClientInterceptorAdapter();
        
        return clientInterceptorAdapter.invokeSync(this, request);
    }

    @Override
    public void invokeBroadcast(ProtocolRequest request) throws Exception {
        String interfaze = request.getInterface();
        
        Destination topicResponseDestination = MQCacheContainer.getMQTopicDestinationContainer().getAsyncResponseDestinationMap().get(interfaze);
        
        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();
        
        MQProducer mqProducer = getMQProducer(interfaze);
        mqProducer.produceRequest(topicResponseDestination, null, applicationEntity, request);
    }
    
    private MQProducer getMQProducer(String interfaze) {
        Map<String, ReferenceEntity> referenceEntityMap = cacheContainer.getReferenceEntityMap();
        ReferenceEntity referenceEntity = referenceEntityMap.get(interfaze);
        String server = referenceEntity.getServer();
        
        return MQCacheContainer.getReferenceContextMap().get(server).getMQProducer();
    }
}