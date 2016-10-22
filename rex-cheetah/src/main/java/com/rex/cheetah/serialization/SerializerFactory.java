package com.rex.cheetah.serialization;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.property.CheetahProperties;
import com.rex.cheetah.serialization.binary.FSTSerializerFactory;
import com.rex.cheetah.serialization.binary.KryoSerializerFactory;
import com.rex.cheetah.serialization.json.FSTJsonSerializerFactory;

public class SerializerFactory {
    private static final Logger LOG = LoggerFactory.getLogger(SerializerFactory.class);

    private static SerializerType binarySerializerType = SerializerType.FST_BINARY;
    private static SerializerType jsonSerializerType = SerializerType.JACKSON_JSON;
    private static boolean serializerLogPrint;

    public static void initialize(CheetahProperties properties) {
        String binarySerializer = properties.getString(CheetahConstants.BINARY_SERIALIZER_ATTRIBUTE_NAME);
        try {
            binarySerializerType = SerializerType.fromString(binarySerializer);
        } catch (Exception e) {
            LOG.warn("Invalid binary serializer={}, use default={}", binarySerializer, binarySerializerType);
        }
        LOG.info("Binary serializer is {}", binarySerializerType);
        if (binarySerializerType == SerializerType.FST_BINARY) {
            FSTSerializerFactory.initialize();
        } else if (binarySerializerType == SerializerType.KRYO_BINARY) {
            KryoSerializerFactory.initialize();
        }

        String jsonSerializer = properties.getString(CheetahConstants.JSON_SERIALIZER_ATTRIBUTE_NAME);
        try {
            jsonSerializerType = SerializerType.fromString(jsonSerializer);
        } catch (Exception e) {
            LOG.warn("Invalid json serializer={}, use default={}", jsonSerializer, jsonSerializerType);
        }
        LOG.info("Json serializer is {}", jsonSerializerType);
        if (jsonSerializerType == SerializerType.FST_JSON) {
            FSTJsonSerializerFactory.initialize();
        }
        
        serializerLogPrint = properties.getBoolean(CheetahConstants.SERIALIZER_LOG_PRINT_ATTRIBUTE_NAME);
    }

    public static SerializerType getBinarySerializerType() {
        return binarySerializerType;
    }

    public static void setBinarySerializerType(SerializerType binarySerializerType) {
        SerializerFactory.binarySerializerType = binarySerializerType;
    }

    public static SerializerType getJsonSerializerType() {
        return jsonSerializerType;
    }

    public static void setJsonSerializerType(SerializerType jsonSerializerType) {
        SerializerFactory.jsonSerializerType = jsonSerializerType;
    }
    
    public static boolean isSerializerLogPrint() {
        return serializerLogPrint;
    }
}