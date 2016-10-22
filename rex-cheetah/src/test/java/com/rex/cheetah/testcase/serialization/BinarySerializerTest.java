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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.nustaq.serialization.FSTConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.kryo.Kryo;
import com.rex.cheetah.common.object.ObjectPoolFactory;
import com.rex.cheetah.common.property.CheetahProperties;
import com.rex.cheetah.common.property.CheetahPropertiesManager;
import com.rex.cheetah.serialization.binary.FSTSerializer;
import com.rex.cheetah.serialization.binary.FSTSerializerFactory;
import com.rex.cheetah.serialization.binary.JDKSerializer;
import com.rex.cheetah.serialization.binary.KryoSerializer;
import com.rex.cheetah.serialization.binary.KryoSerializerFactory;
import com.rex.cheetah.serialization.binary.ProtoStuffSerializer;
import com.rex.cheetah.testcase.entity.EntityFactory;
import com.rex.cheetah.testcase.entity.User;

public class BinarySerializerTest {
    private static final Logger LOG = LoggerFactory.getLogger(BinarySerializerTest.class);

    @Test
    public void testFSTFunction1() throws Exception {
        User user1 = EntityFactory.createUser1();

        FSTConfiguration fst = FSTSerializerFactory.createFST();
        
        byte[] bytes = FSTSerializer.serialize(fst, user1);
        LOG.info(bytes.toString());

        User user2 = FSTSerializer.deserialize(fst, bytes);
        LOG.info(user2.toString());
    }

    @Test
    public void testFSTFunction2() throws Exception {
        Map<String, String> m1 = new HashMap<String, String>();
        m1.put("a", "1");
        m1.put("b", "2");

        Map<String, String> m2 = Collections.unmodifiableMap(m1);

        FSTConfiguration fst = FSTSerializerFactory.createFST();
        
        byte[] bytes = FSTSerializer.serialize(fst, (Serializable) m2);
        LOG.info(bytes.toString());

        Map<String, String> m3 = FSTSerializer.deserialize(fst, bytes);
        LOG.info(m3.toString());
    }

    @Test
    public void testKyroFunction1() throws Exception {
        User user1 = EntityFactory.createUser1();

        Kryo kryo = KryoSerializerFactory.createKryo();
        
        byte[] bytes = KryoSerializer.serialize(kryo, user1);
        LOG.info(bytes.toString());

        User user2 = KryoSerializer.deserialize(kryo, bytes);
        LOG.info(user2.toString());
    }
    
    @Test
    public void testKyroFunction2() throws Exception {
        // 不能序列化不可变类
        Map<String, String> m1 = new HashMap<String, String>();
        m1.put("a", "1");
        m1.put("b", "2");

        Map<String, String> m2 = Collections.unmodifiableMap(m1);

        Kryo kryo = KryoSerializerFactory.createKryo();
        
        byte[] bytes = KryoSerializer.serialize(kryo, (Serializable) m2);
        LOG.info(bytes.toString());

        Map<String, String> m3 = KryoSerializer.deserialize(kryo, bytes);
        LOG.info(m3.toString());
    }
    
    @Test
    public void testProtoStuffFunction1() throws Exception {
        User user1 = EntityFactory.createUser1();
        
        byte[] bytes = ProtoStuffSerializer.serialize(user1);
        LOG.info(bytes.toString());

        User user2 = ProtoStuffSerializer.deserialize(bytes, User.class);
        LOG.info(user2.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testProtoStuffFunction2() throws Exception {
        Map<String, String> m1 = new HashMap<String, String>();
        m1.put("a", "1");
        m1.put("b", "2");

        Map<String, String> m2 = Collections.unmodifiableMap(m1);

        byte[] bytes = ProtoStuffSerializer.serialize((Serializable) m2);
        LOG.info(bytes.toString());

        Map<String, String> m3 = ProtoStuffSerializer.deserialize(bytes, Map.class);
        LOG.info(m3.toString());
    }
    
    @Test
    public void testJDKFunction1() throws Exception {
        User user1 = EntityFactory.createUser1();

        byte[] bytes = JDKSerializer.serialize(user1);
        LOG.info(bytes.toString());

        User user2 = JDKSerializer.deserialize(bytes);
        LOG.info(user2.toString());
    }
    
    @Test
    public void testJDKFunction2() throws Exception {
        Map<String, String> m1 = new HashMap<String, String>();
        m1.put("a", "1");
        m1.put("b", "2");

        Map<String, String> m2 = Collections.unmodifiableMap(m1);

        byte[] bytes = JDKSerializer.serialize((Serializable) m2);
        LOG.info(bytes.toString());

        Map<String, String> m3 = JDKSerializer.deserialize(bytes);
        LOG.info(m3.toString());
    }

    @Test
    public void testPerformance() throws Exception {
        User user1 = EntityFactory.createUser1();
        
        CheetahProperties properties = CheetahPropertiesManager.getProperties();
        ObjectPoolFactory.initialize(properties);
        FSTSerializerFactory.initialize();
        KryoSerializerFactory.initialize();
        
        FSTConfiguration fst = FSTSerializerFactory.createFST();
        
        long value1 = System.currentTimeMillis();
        for (int i = 0; i < 5000000; i++) {
            byte[] bytes = FSTSerializer.serialize(fst, user1);

            FSTSerializer.deserialize(fst, bytes);
        }
        LOG.info("FSTSerializer time spent : {}", System.currentTimeMillis() - value1);
        
        long value2 = System.currentTimeMillis();
        for (int i = 0; i < 5000000; i++) {
            byte[] bytes = FSTSerializer.serialize(user1);

            FSTSerializer.deserialize(bytes);
        }
        LOG.info("FSTSerializer (pool) time spent : {}", System.currentTimeMillis() - value2);

        Kryo kryo = KryoSerializerFactory.createKryo();
        
        long value3 = System.currentTimeMillis();
        for (int i = 0; i < 5000000; i++) {
            byte[] bytes = KryoSerializer.serialize(kryo, user1);

            KryoSerializer.deserialize(kryo, bytes);
        }
        LOG.info("KryoSerializer time spent : {}", System.currentTimeMillis() - value3);
        
        long value4 = System.currentTimeMillis();
        for (int i = 0; i < 5000000; i++) {
            byte[] bytes = KryoSerializer.serialize(user1);

            KryoSerializer.deserialize(bytes);
        }
        LOG.info("KryoSerializer (pool) time spent : {}", System.currentTimeMillis() - value4);

        long value5 = System.currentTimeMillis();
        for (int i = 0; i < 5000000; i++) {
            byte[] bytes = ProtoStuffSerializer.serialize(user1);

            ProtoStuffSerializer.deserialize(bytes, User.class);
        }
        LOG.info("ProtoStuffSerializer time spent : {}", System.currentTimeMillis() - value5);

        long value6 = System.currentTimeMillis();
        for (int i = 0; i < 5000000; i++) {
            byte[] bytes = JDKSerializer.serialize(user1);

            JDKSerializer.deserialize(bytes);
        }
        LOG.info("JDKSerializer time spent : {}", System.currentTimeMillis() - value6);
    }
}