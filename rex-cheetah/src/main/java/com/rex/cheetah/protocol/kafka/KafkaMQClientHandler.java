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

import java.util.Arrays;
import java.util.concurrent.Callable;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.entity.MQPropertyEntity;
import com.rex.cheetah.common.thread.ThreadPoolFactory;
import com.rex.cheetah.protocol.ClientExecutorAdapter;
import com.rex.cheetah.protocol.ProtocolResponse;
import com.rex.cheetah.serialization.SerializerExecutor;

public class KafkaMQClientHandler extends KafkaMQConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaMQClientHandler.class);
    
    private int consumerClientPollTimeout = 500;
    private boolean transportLogPrint;
    
    public KafkaMQClientHandler(MQPropertyEntity mqPropertyEntity, String groupId) {
        super(mqPropertyEntity, groupId);
        
        try {
            consumerClientPollTimeout = mqPropertyEntity.getInteger(CheetahConstants.KAFKA_CONSUMER_CLIENT_POLL_TIMEOUT_ATTRIBUTE_NAME);
            transportLogPrint = mqPropertyEntity.getBoolean(CheetahConstants.TRANSPORT_LOG_PRINT_ATTRIBUTE_NAME);
        } catch (Exception e) {
            LOG.error("Get properties failed", e);
        }
    }

    public void consume(final String topic, final String interfaze, final ApplicationEntity applicationEntity) throws Exception {     
        ThreadPoolFactory.createThreadPoolClientExecutor(interfaze).submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                String requestUrl = applicationEntity.toUrl();
                
                int partitionIndex = getPartitionIndex(consumer, topic, requestUrl);
                TopicPartition partition = new TopicPartition(topic, partitionIndex);
                consumer.assign(Arrays.asList(partition));
                
                while (true) {
                    ConsumerRecords<String, byte[]> records = consumer.poll(consumerClientPollTimeout);
                    if (records != null && records.count() != 0) {
                        for (ConsumerRecord<String, byte[]> record : records) {
                            ProtocolResponse response = SerializerExecutor.deserialize(record.value());
                            try {
                                String responseSource = response.getResponseSource().toString();
                                // String requestSource = response.getRequestSource().toString();
                                
                                if (transportLogPrint) {
                                    LOG.info("Response from server={}, service={}", responseSource, interfaze);
                                }
                                
                                ClientExecutorAdapter clientExecutorAdapter = executorContainer.getClientExecutorAdapter();
                                clientExecutorAdapter.handle(response);
                            } catch (Exception e) {
                                LOG.error("Consume request failed", e);
                            }
                        }
                    }
                }
            }
        });
    }
    
    @SuppressWarnings("resource")
    private int getPartitionIndex(Consumer<String, byte[]> consumer, String topic, String key) {
        int partitionNumber = consumer.partitionsFor(topic).size();
        
        StringSerializer keySerializer = new StringSerializer();
        byte[] serializedKey = keySerializer.serialize(topic, key);

        int positive = Utils.murmur2(serializedKey) & 0x7fffffff;

        return positive % partitionNumber;
    }
}