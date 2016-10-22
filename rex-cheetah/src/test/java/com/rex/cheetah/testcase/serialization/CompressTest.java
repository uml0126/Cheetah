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

import java.util.HashMap;

import org.junit.Test;

import com.rex.cheetah.common.property.CheetahProperties;
import com.rex.cheetah.common.property.CheetahPropertiesManager;
import com.rex.cheetah.serialization.SerializerExecutor;
import com.rex.cheetah.serialization.SerializerFactory;
import com.rex.cheetah.serialization.SerializerType;
import com.rex.cheetah.serialization.binary.FSTSerializerFactory;
import com.rex.cheetah.serialization.binary.KryoSerializerFactory;
import com.rex.cheetah.serialization.compression.CompressorType;

public class CompressTest {
    @Test
    public void test() throws Exception {
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        for (int i = 0; i < 10000; i++) {
            map.put(i, "abcdefghijklmnopqrstuvwxyz");
        }

        CheetahProperties properties = CheetahPropertiesManager.getProperties();
        SerializerFactory.initialize(properties);
        
        FSTSerializerFactory.initialize();
        SerializerExecutor.serialize(map, SerializerType.FST_BINARY, CompressorType.QUICK_LZ_COMPRESSOR, true, true);
        
        KryoSerializerFactory.initialize();
        SerializerExecutor.serialize(map, SerializerType.KRYO_BINARY, CompressorType.QUICK_LZ_COMPRESSOR, true, true);
        
        SerializerExecutor.serialize(map, SerializerType.JDK_BINARY, CompressorType.QUICK_LZ_COMPRESSOR, true, true);
    }
}