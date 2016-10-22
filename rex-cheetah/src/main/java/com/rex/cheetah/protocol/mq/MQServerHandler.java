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

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.entity.MQPropertyEntity;
import com.rex.cheetah.protocol.ProtocolRequest;
import com.rex.cheetah.protocol.ProtocolResponse;
import com.rex.cheetah.protocol.ServerExecutorAdapter;

public class MQServerHandler extends MQConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(MQServerHandler.class);
    
    private MQBytesMessageConverter mqMessageConverter = new MQBytesMessageConverter();
    private MQProducer mqProducer;
    private boolean transportLogPrint;
    
    public MQServerHandler(MQProducer mqProducer, MQPropertyEntity mqPropertyEntity) {
        this.mqProducer = mqProducer;
        
        try {
            transportLogPrint = mqPropertyEntity.getBoolean(CheetahConstants.TRANSPORT_LOG_PRINT_ATTRIBUTE_NAME);
        } catch (Exception e) {
            LOG.error("Get properties failed", e);
        }
    }

    @Override
    public void onMessage(final BytesMessage message, final Session session) throws JMSException {
        final ProtocolRequest request = (ProtocolRequest) mqMessageConverter.fromMessage(message);
        // 如果消费线程里面放子线程：
        // 好处是可以加快消费速度，减少MQ消息堆积，
        // 坏处是如果子线程消费速度跟不上，会造成消息在内存中的堆积，一旦服务器挂掉，消息全部丢失
        // 最终还是去掉子线程
        String interfaze = request.getInterface();
        /*ThreadPoolFactory.createThreadPoolServerExecutor(interfaze).submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {*/
                String requestSelector = MQSelectorUtil.getRequestSelector(message);
                
                if (transportLogPrint) {
                    LOG.info("Request from client={}, service={}", requestSelector, interfaze);
                }
                
                final ProtocolResponse response = new ProtocolResponse();
                try {
                    ServerExecutorAdapter serverExecutorAdapter = executorContainer.getServerExecutorAdapter();
                    serverExecutorAdapter.handle(request, response);
                } catch (Exception e) {
                    LOG.error("Consume request failed", e);
                }

                boolean feedback = request.isFeedback();
                if (feedback) {
                    try {
                        Destination requestDestination = message.getJMSReplyTo();                    
       
                        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();

                        mqProducer.produceResponse(requestDestination, applicationEntity, response, requestSelector);
                    } finally {
                        message.setJMSReplyTo(null);
                    }
                }
                
                /*return null;
            }
        });*/
    }
}