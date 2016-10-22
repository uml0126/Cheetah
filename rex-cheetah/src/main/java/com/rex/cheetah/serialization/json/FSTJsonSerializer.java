package com.rex.cheetah.serialization.json;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import org.apache.commons.lang3.StringUtils;
import org.nustaq.serialization.FSTConfiguration;

import com.rex.cheetah.serialization.SerializerException;

public class FSTJsonSerializer {    
    public static <T> String toJson(T object) {
        FSTConfiguration fst = FSTJsonSerializerFactory.getDefaultFST();
        
        return toJson(fst, object);
    }
    
    public static <T> String toJson(FSTConfiguration fst, T object) {
        if (object == null) {
            throw new SerializerException("Object is null");
        }

        return fst.asJsonString(object);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        FSTConfiguration fst = FSTJsonSerializerFactory.getDefaultFST();
        
        return fromJson(fst, json, clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromJson(FSTConfiguration fst, String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            throw new SerializerException("Json is null or empty");
        }

        return (T) fst.asObject(json.getBytes());
    }
}