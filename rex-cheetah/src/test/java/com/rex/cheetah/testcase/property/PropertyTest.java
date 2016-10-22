package com.rex.cheetah.testcase.property;

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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.entity.MQEntity;
import com.rex.cheetah.common.entity.MQPropertyEntity;
import com.rex.cheetah.common.entity.ProtocolType;
import com.rex.cheetah.common.property.CheetahProperties;
import com.rex.cheetah.common.property.CheetahPropertiesManager;

public class PropertyTest {
    private static final Logger LOG = LoggerFactory.getLogger(PropertyTest.class);

    @Test
    public void testMQEntity() throws Exception {
        CheetahProperties properties = CheetahPropertiesManager.getProperties();

        MQEntity entity = new MQEntity();
        entity.extractProperties(properties, ProtocolType.TIBCO);
        Map<String, CheetahProperties> propertiesMap = entity.getPropertiesMap();
        for (Map.Entry<String, CheetahProperties> entry : propertiesMap.entrySet()) {
            String key = entry.getKey();
            CheetahProperties value = entry.getValue();
            LOG.info("{} : {}", key, value.getMap());
        }
    }

    @Test
    public void testMQPropertyEntity() throws Exception {
        CheetahProperties properties = CheetahPropertiesManager.getProperties();
        
        MQEntity entity = new MQEntity();
        entity.extractProperties(properties, ProtocolType.TIBCO);
        
        MQPropertyEntity propertyEntity = new MQPropertyEntity("com.rex.test", "tibco-1", entity);
        LOG.info("mqUrl : {}", propertyEntity.getString("mqUrl"));
        
        LOG.info("summarizeProperties : {}", propertyEntity.summarizeProperties(CheetahConstants.KAFKA_CONSUMER_ATTRIBUTE_NAME));
    }
}