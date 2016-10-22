package com.rex.cheetah.testcase.serialization;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import org.junit.Test;
import org.nustaq.serialization.FSTConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.rex.cheetah.common.object.ObjectPoolFactory;
import com.rex.cheetah.common.property.CheetahProperties;
import com.rex.cheetah.common.property.CheetahPropertiesManager;
import com.rex.cheetah.serialization.json.AliSerializer;
import com.rex.cheetah.serialization.json.FSTJsonSerializer;
import com.rex.cheetah.serialization.json.FSTJsonSerializerFactory;
import com.rex.cheetah.serialization.json.JacksonSerializer;
import com.rex.cheetah.testcase.entity.EntityFactory;
import com.rex.cheetah.testcase.entity.User;

public class JsonSerializerTest {
    private static final Logger LOG = LoggerFactory.getLogger(JsonSerializerTest.class);
    
    @Test
    public void testJacksonFunction() throws Exception {
        User user1 = EntityFactory.createUser1();

        String json = JacksonSerializer.toJson(user1);
        LOG.info(json);

        User user2 = JacksonSerializer.fromJson(json, User.class);
        LOG.info(user2.toString());
    }
    
    @Test
    public void testAliFunction() throws Exception {
        User user1 = EntityFactory.createUser1();

        String json = AliSerializer.toJson(user1);
        LOG.info(json);

        User user2 = AliSerializer.fromJson(json, User.class);
        LOG.info(user2.toString());
        
        User user3 = AliSerializer.fromJson(json);
        LOG.info(user3.toString());
        
        JSONObject jsonObject = AliSerializer.parseJson(json);
        LOG.info(jsonObject.getString("name"));
    }
    
    @Test
    public void testFSTFunction() throws Exception {
        User user1 = EntityFactory.createUser1();

        FSTConfiguration fst = FSTJsonSerializerFactory.createFST();
                
        String json = FSTJsonSerializer.toJson(fst, user1);
        LOG.info(json);

        User user2 = FSTJsonSerializer.fromJson(fst, json, User.class);
        LOG.info(user2.toString());
    }
    
    @Test
    public void testPerformance() throws Exception {
        User user1 = EntityFactory.createUser1();
        
        CheetahProperties properties = CheetahPropertiesManager.getProperties();
        ObjectPoolFactory.initialize(properties);
        FSTJsonSerializerFactory.initialize();
        
        long value1 = System.currentTimeMillis();
        for (int i = 0; i < 5000000; i++) {
            String json = JacksonSerializer.toJson(user1);

            JacksonSerializer.fromJson(json, User.class);
        }
        LOG.info("JacksonSerializer time spent : {}", System.currentTimeMillis() - value1);
        
        long value2 = System.currentTimeMillis();
        for (int i = 0; i < 5000000; i++) {
            String json = AliSerializer.toJson(user1);

            AliSerializer.fromJson(json, User.class);
        }
        LOG.info("AliSerializer time spent : {}", System.currentTimeMillis() - value2);
        
        FSTConfiguration fst = FSTJsonSerializerFactory.createFST();
        
        long value3 = System.currentTimeMillis();
        for (int i = 0; i < 5000000; i++) {
            String json = FSTJsonSerializer.toJson(fst, user1);

            FSTJsonSerializer.fromJson(fst, json, User.class);
        }
        LOG.info("FSTJsonSerializer time spent : {}", System.currentTimeMillis() - value3);
        
        long value4 = System.currentTimeMillis();
        for (int i = 0; i < 5000000; i++) {
            String json = FSTJsonSerializer.toJson(user1);

            FSTJsonSerializer.fromJson(json, User.class);
        }
        LOG.info("FSTJsonSerializer (pool) time spent : {}", System.currentTimeMillis() - value4);
    }
}