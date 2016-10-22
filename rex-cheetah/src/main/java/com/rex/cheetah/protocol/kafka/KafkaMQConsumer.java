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

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.delegate.CheetahDelegateImpl;
import com.rex.cheetah.common.entity.MQPropertyEntity;

public class KafkaMQConsumer extends CheetahDelegateImpl {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaMQConsumer.class);
    
    protected MQPropertyEntity mqPropertyEntity;
    protected Consumer<String, byte[]> consumer;
    
    public KafkaMQConsumer(MQPropertyEntity mqPropertyEntity, String groupId) {
        Map<String, Object> map = null;
        try {
            map = mqPropertyEntity.summarizeProperties(CheetahConstants.KAFKA_CONSUMER_ATTRIBUTE_NAME);
        } catch (Exception e) {
            LOG.error("Extract properties failed", e);
        }
        map.put("group.id", groupId);

        this.mqPropertyEntity = mqPropertyEntity;
        this.consumer = new KafkaConsumer<String, byte[]>(map, new StringDeserializer(), new ByteArrayDeserializer());
    }

    public MQPropertyEntity getMQPropertyEntity() {
        return mqPropertyEntity;
    }
    
    public Consumer<String, byte[]> getConsumer() {
        return consumer;
    }
}