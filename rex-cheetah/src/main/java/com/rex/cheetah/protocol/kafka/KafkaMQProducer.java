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
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.entity.MQPropertyEntity;
import com.rex.cheetah.common.entity.ProtocolType;
import com.rex.cheetah.common.thread.ThreadPoolFactory;
import com.rex.cheetah.event.protocol.ProtocolEventFactory;
import com.rex.cheetah.protocol.ProtocolRequest;
import com.rex.cheetah.protocol.ProtocolResponse;
import com.rex.cheetah.serialization.SerializerExecutor;

public class KafkaMQProducer {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaMQProducer.class);

    protected MQPropertyEntity mqPropertyEntity;
    protected Producer<String, byte[]> producer;
    
    public KafkaMQProducer(MQPropertyEntity mqPropertyEntity) {
        Map<String, Object> map = null;
        try {
            map = mqPropertyEntity.summarizeProperties(CheetahConstants.KAFKA_PRODUCER_ATTRIBUTE_NAME);
        } catch (Exception e) {
            LOG.error("Extract properties failed", e);
        }
        
        this.mqPropertyEntity = mqPropertyEntity;
        this.producer = new KafkaProducer<String, byte[]>(map, new StringSerializer(), new ByteArraySerializer());
    }

    public MQPropertyEntity getMQPropertyEntity() {
        return mqPropertyEntity;
    }
    
    public Producer<String, byte[]> getProducer() {
        return producer;
    }

    /**
     * The default partitioning strategy:
     * <ul>
     * <li>If a partition is specified in the record, use it
     * <li>If no partition is specified but a key is present choose a partition based on a hash of the key
     * <li>If no partition or key is present choose a partition in a round-robin fashion
     */
    public void produceRequest(final String topic, final ApplicationEntity applicationEntity, final ProtocolRequest request) throws Exception {        
        String interfaze = request.getInterface();
        ThreadPoolFactory.createThreadPoolClientExecutor(interfaze).submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                if (StringUtils.isEmpty(topic)) {
                    LOG.error("Topic can't be null");

                    return null;
                }
                
                String requestSource = applicationEntity.toUrl();
                request.setRequestSource(requestSource);
                
                final ProducerRecord<String, byte[]> record = new ProducerRecord<String, byte[]>(topic, SerializerExecutor.serialize(request));
                producer.send(record, new Callback() {
                    public void onCompletion(RecordMetadata metadata, Exception e) {
                        if (e == null) {
                            return;
                        }
                        
                        LOG.error("Produce request failed", e);

                        ProtocolEventFactory.postClientProducerEvent(ProtocolType.KAFKA, request);
                    }
                });

                return null;
            }
        });
    }

    public void produceResponse(String topic, ApplicationEntity applicationEntity, final ProtocolResponse response, String requestSource) throws Exception {
        if (StringUtils.isEmpty(topic)) {
            LOG.error("Topic can't be null");

            return;
        }
        
        if (StringUtils.isEmpty(requestSource)) {
            LOG.error("Request source can't be null");

            return;
        }
        
        String responseSource = applicationEntity.toUrl();
        response.setRequestSource(requestSource);
        response.setResponseSource(responseSource);

        final ProducerRecord<String, byte[]> record = new ProducerRecord<String, byte[]>(topic, requestSource, SerializerExecutor.serialize(response));
        producer.send(record, new Callback() {
            public void onCompletion(RecordMetadata metadata, Exception e) {
                if (e == null) {
                    return;
                }
                
                LOG.error("Produce response failed", e);

                ProtocolEventFactory.postServerProducerEvent(ProtocolType.KAFKA, response);
            }
        });
    }
}