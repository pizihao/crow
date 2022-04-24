package com.deep.crow.jackson;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

/**
 * <h2>获取{@link ObjectMapper}</h2>
 *
 * @author Create by liuwenhao on 2022/4/24 13:28
 */
public class ObjectMapperFactory {

    static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private ObjectMapperFactory(){

    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        ObjectMapperFactory.objectMapper = objectMapper;
    }

    public static ObjectMapper get() {
        return objectMapper;
    }

}