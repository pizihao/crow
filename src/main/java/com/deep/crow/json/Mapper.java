package com.deep.crow.json;

import com.deep.crow.json.deserializer.*;
import com.deep.crow.json.serializer.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Mapper {

  static Map<Class<?>, JsonSerializer<?>> mapSerializer = new HashMap<>();

  static Map<Class<?>, JsonDeserializer<?>> mapDeserializer = new HashMap<>();

  static {
    mapSerializer.put(Boolean.class, new BooleanSerializer());
    mapSerializer.put(Byte.class, new ByteSerializer());
    mapSerializer.put(Character.class, new CharacterSerializer());
    mapSerializer.put(CharSequence.class, new CharSequenceSerializer());
    mapSerializer.put(Double.class, new DoubleSerializer());
    mapSerializer.put(Float.class, new FloatSerializer());
    mapSerializer.put(Integer.class, new IntegerSerializer());
    mapSerializer.put(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    mapSerializer.put(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
    mapSerializer.put(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")));
    mapSerializer.put(Long.class, new LongSerializer());
    mapSerializer.put(Short.class, new ShortSerializer());
    mapDeserializer.put(Boolean.class, new BooleanDeserializer());
    mapDeserializer.put(Byte.class, new ByteDeserializer());
    mapDeserializer.put(Character.class, new CharacterDeserializer());
    mapDeserializer.put(CharSequence.class, new CharSequenceDeserializer());
    mapDeserializer.put(Double.class, new DoubleDeserializer());
    mapDeserializer.put(Float.class, new FloatDeserializer());
    mapDeserializer.put(Integer.class, new IntegerDeserializer());
    mapDeserializer.put(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    mapDeserializer.put(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
    mapDeserializer.put(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")));
    mapDeserializer.put(Long.class, new LongDeserializer());
    mapDeserializer.put(Short.class, new ShortDeserializer());
  }

  @SuppressWarnings("unchecked")
  public static <T> JsonSerializer<T> getSerializer(Class<?> cls) {
    return (JsonSerializer<T>) get(cls, mapSerializer);
  }

  @SuppressWarnings("unchecked")
  public static <T> JsonDeserializer<T> getDeserializer(Class<?> cls) {
    return (JsonDeserializer<T>) get(cls, mapDeserializer);
  }

  private static Object get(Class<?> cls, Map<?, ?> map) {
    Object o = map.get(cls);
    Class<?> superclass = cls.getSuperclass();
    while (o == null && superclass != null && superclass.getSuperclass() != null) {
      superclass = superclass.getSuperclass();
      o = map.get(superclass);
      if (o != null) {
        break;
      }
    }

    Class<?>[] interfaces = cls.getInterfaces();
    while (o == null && interfaces.length > 0) {
      for (Class<?> i : interfaces) {
        o = get(i, map);
        if (o != null) {
          break;
        }
      }
      if (o != null) {
        break;
      }
    }
    return o;
  }
}
