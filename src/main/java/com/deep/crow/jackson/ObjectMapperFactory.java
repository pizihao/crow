package com.deep.crow.jackson;

import com.deep.crow.jackson.deserializer.*;
import com.deep.crow.jackson.serializer.*;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


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

        DateTimeFormatter localDateTimePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        DateTimeFormatter localDatePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter localTime = DateTimeFormatter.ofPattern("HH-mm-ss");

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, new LongSerializer());
        simpleModule.addDeserializer(Long.class, new LongDeserializer());
        simpleModule.addSerializer(Integer.class, new IntegerSerializer());
        simpleModule.addDeserializer(Integer.class, new IntegerDeserializer());
        simpleModule.addSerializer(Double.class, new DoubleSerializer());
        simpleModule.addDeserializer(Double.class, new DoubleDeserializer());
        simpleModule.addSerializer(Float.class, new FloatSerializer());
        simpleModule.addDeserializer(Float.class, new FloatDeserializer());
        simpleModule.addSerializer(Short.class, new ShortSerializer());
        simpleModule.addDeserializer(Short.class, new ShortDeserializer());
        simpleModule.addSerializer(Byte.class, new ByteSerializer());
        simpleModule.addDeserializer(Byte.class, new ByteDeserializer());
        simpleModule.addSerializer(Character.class, new CharacterSerializer());
        simpleModule.addDeserializer(Character.class, new CharacterDeserializer());
        simpleModule.addSerializer(Boolean.class, new BooleanSerializer());
        simpleModule.addDeserializer(Boolean.class, new BooleanDeserializer());
        simpleModule.addSerializer(String.class, new StringSerializer());
        simpleModule.addDeserializer(String.class, new StringDeserializer());

        simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(localDateTimePattern));
        simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(localDateTimePattern));

        simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer(localDatePattern));
        simpleModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(localDatePattern));
        simpleModule.addSerializer(Date.class, new DateSerializer());
        simpleModule.addDeserializer(Date.class, new DateDeserializers.DateDeserializer());
        simpleModule.addSerializer(LocalTime.class, new LocalTimeSerializer(localTime));
        simpleModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(localTime));
        objectMapper.registerModule(simpleModule);

    }

    private ObjectMapperFactory() {

    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        ObjectMapperFactory.objectMapper = objectMapper;
    }

    public static ObjectMapper get() {
        return objectMapper;
    }
}