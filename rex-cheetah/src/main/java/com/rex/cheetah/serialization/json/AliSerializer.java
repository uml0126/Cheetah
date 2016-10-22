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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.serialization.SerializerException;

public class AliSerializer {
    public static <T> String toJson(T object) {
        if (object == null) {
            throw new SerializerException("Object is null");
        }

        return JSON.toJSONStringWithDateFormat(object, CheetahConstants.DATE_FORMAT, SerializerFeature.WriteClassName);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            throw new SerializerException("Json is null or empty");
        }

        return JSON.parseObject(json, clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String json) {
        if (StringUtils.isEmpty(json)) {
            throw new SerializerException("Json is null or empty");
        }

        return (T) JSON.parse(json);
    }

    public static JSONObject parseJson(String json) {
        if (StringUtils.isEmpty(json)) {
            throw new SerializerException("Json is null or empty");
        }

        return JSON.parseObject(json);
    }
}