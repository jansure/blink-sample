package com.glory.blink.server.utils;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import lombok.NonNull;

/**
 * jackson序列化工具
 *
 * @author zhangshuai
 * @since 13/03/2018
 */

public class JsonUtil {
    private static ObjectMapper mapper;

    /* static{
         //mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
         mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS,true);
     }*/
    public static String serialize(Object object) {
        if (mapper == null) {
            mapper = new ObjectMapper();
        }

        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

    public static <T> T deserialize(String jsonStr, Class<T> valueType) {
        if (mapper == null) {
            mapper = new ObjectMapper();
        }

        try {
            return mapper.readValue(jsonStr, valueType);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T deserialize(String jsonStr, TypeReference<T> valueTypeRef) {
        if (mapper == null) {
            mapper = new ObjectMapper();
        }

        try {
            return mapper.readValue(jsonStr, valueTypeRef);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> List<T> decodeList(@NonNull String array) {
        TypeReference<List<T>> typeReference = new TypeReference<List<T>>() {
        };
        List<T> list = deserialize(array, typeReference);
        return Optional.ofNullable(list).orElse(Lists.newArrayList());
    }
}