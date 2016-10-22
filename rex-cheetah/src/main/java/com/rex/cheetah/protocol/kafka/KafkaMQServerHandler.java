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

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.entity.MQPropertyEntity;
import com.rex.cheetah.common.thread.ThreadPoolFactory;
import com.rex.cheetah.protocol.ProtocolRequest;
import com.rex.cheetah.protocol.ProtocolResponse;
import com.rex.cheetah.protocol.ServerExecutorAdapter;
import com.rex.cheetah.serialization.SerializerExecutor;

public class KafkaMQServerHandler extends KafkaMQConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaMQServerHandler.class);

    private KafkaMQProducer producer;
    private int consumerServerPollTimeout = 500;
    private boolean transportLogPrint;

    public KafkaMQServerHandler(MQPropertyEntity mqPropertyEntity, String groupId) {
        super(mqPropertyEntity, groupId);

        try {
            consumerServerPollTimeout = mqPropertyEntity.getInteger(CheetahConstants.KAFKA_CONSUMER_SERVER_POLL_TIMEOUT_ATTRIBUTE_NAME);
            transportLogPrint = mqPropertyEntity.getBoolean(CheetahConstants.TRANSPORT_LOG_PRINT_ATTRIBUTE_NAME);
        } catch (Exception e) {
            LOG.error("Get properties failed", e);
        }

        producer = new KafkaMQProducer(mqPropertyEntity);
    }

    public void consume(final String responseTopic, final String requestTopic, final String interfaze) throws Exception {
        ThreadPoolFactory.createThreadPoolServerExecutor(interfaze).submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                consumer.subscribe(Arrays.asList(responseTopic));

                while (true) {
                    ConsumerRecords<String, byte[]> records = consumer.poll(consumerServerPollTimeout);
                    if (records != null && records.count() != 0) {
                        for (ConsumerRecord<String, byte[]> record : records) {
                            ProtocolRequest request = SerializerExecutor.deserialize(record.value());
                            String requestSource = request.getRequestSource().toString();
                            
                            if (transportLogPrint) {
                                LOG.info("Request from client={}, service={}", requestSource, interfaze);
                            }
                            
                            ProtocolResponse response = new ProtocolResponse();
                            try {
                                ServerExecutorAdapter serverExecutorAdapter = executorContainer.getServerExecutorAdapter();
                                serverExecutorAdapter.handle(request, response);
                            } catch (Exception e) {
                                LOG.error("Consume request failed", e);
                            }

                            boolean feedback = request.isFeedback();
                            if (feedback) {
                                ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();
                                try {
                                    producer.produceResponse(requestTopic, applicationEntity, response, requestSource);
                                } catch (Exception e) {
                                    LOG.error("Produce response failed", e);
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}