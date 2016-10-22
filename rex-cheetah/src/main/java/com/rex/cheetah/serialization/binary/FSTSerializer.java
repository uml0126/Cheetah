package com.rex.cheetah.serialization.binary;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import org.apache.commons.lang3.ArrayUtils;
import org.nustaq.serialization.FSTConfiguration;

import com.rex.cheetah.serialization.SerializerException;

public class FSTSerializer {
    public static <T> byte[] serialize(T object) {
        FSTConfiguration fst = FSTSerializerFactory.getDefaultFST();

        return serialize(fst, object);
    }
    
    public static <T> byte[] serialize(FSTConfiguration fst, T object) {
        if (object == null) {
            throw new SerializerException("Object is null");
        }

        return fst.asByteArray(object);
    }

    public static <T> T deserialize(byte[] bytes) {
        FSTConfiguration fst = FSTSerializerFactory.getDefaultFST();

        return deserialize(fst, bytes);
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(FSTConfiguration fst, byte[] bytes) {
        if (ArrayUtils.isEmpty(bytes)) {
            throw new SerializerException("Bytes is null or empty");
        }

        return (T) fst.asObject(bytes);
    }
}