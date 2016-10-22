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

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.serialization.SerializerException;

public class JacksonSerializer {
    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat(CheetahConstants.DATE_FORMAT));
    }

    public static <T> String toJson(T object) {
        if (object == null) {
            throw new SerializerException("Object is null");
        }

        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new SerializerException(e.getMessage(), e);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            throw new SerializerException("Json is null or empty");
        }

        try {
            return mapper.readValue(json, clazz);
        } catch (JsonParseException e) {
            throw new SerializerException(e.getMessage(), e);
        } catch (JsonMappingException e) {
            throw new SerializerException(e.getMessage(), e);
        } catch (IOException e) {
            throw new SerializerException(e.getMessage(), e);
        }
    }
}