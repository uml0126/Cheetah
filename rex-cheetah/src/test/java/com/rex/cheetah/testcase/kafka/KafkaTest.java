package com.rex.cheetah.testcase.kafka;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.Utils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaTest {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaTest.class);

    @Test
    public void test() throws Exception {
        // Kafka哈希算法作为分区的漏洞，崩溃中...
        LOG.info("对应的分区={}", getPartitionIndex(10, "abc", "10.0.0.3:1000"));
        LOG.info("对应的分区={}", getPartitionIndex(10, "abc", "10.0.0.3:1001"));
    }
    
    @SuppressWarnings("resource")
    private int getPartitionIndex(int partitionNumber, String topic, String key) {
        StringSerializer keySerializer = new StringSerializer();
        byte[] serializedKey = keySerializer.serialize(topic, key);

        int positive = Utils.murmur2(serializedKey) & 0x7fffffff;

        return positive % partitionNumber;
    }
}