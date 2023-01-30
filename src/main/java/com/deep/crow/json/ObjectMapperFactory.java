package com.deep.crow.json;

import com.deep.crow.json.deserializer.*;
import com.deep.crow.json.serializer.*;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 获取{@link ObjectMapper}
 *
 * @author Create by liuwenhao on 2022/4/24 13:28
 */
public class ObjectMapperFactory {

  static ObjectMapper objectMapper;

  static {
    objectMapper = new ObjectMapper();
    objectMapper.activateDefaultTyping(
        LaissezFaireSubTypeValidator.instance,
        ObjectMapper.DefaultTyping.NON_FINAL,
        JsonTypeInfo.As.PROPERTY);
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    DateTimeFormatter localDateTimePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    DateTimeFormatter localDatePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter localTime = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

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

    simpleModule.addSerializer(
        LocalDateTime.class, new LocalDateTimeSerializer(localDateTimePattern));
    simpleModule.addDeserializer(
        LocalDateTime.class, new LocalDateTimeDeserializer(localDateTimePattern));
    simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer(localDatePattern));
    simpleModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(localDatePattern));
    simpleModule.addSerializer(LocalTime.class, new LocalTimeSerializer(localTime));
    simpleModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(localTime));
    objectMapper.registerModule(simpleModule);
  }

  private ObjectMapperFactory() {}

  public static void setObjectMapper(ObjectMapper objectMapper) {
    ObjectMapperFactory.objectMapper = objectMapper;
  }

  public static ObjectMapper get() {
    return objectMapper;
  }
}
