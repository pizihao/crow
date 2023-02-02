package com.deep.crow.json;

import com.deep.crow.json.deserializer.*;
import com.deep.crow.json.serializer.*;
import com.deep.crow.json.symbol.Symbol;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Mapper implements Map<String, Mapper> {

  /**
   * 自身的key
   */
  String key;

  /**
   * 自身的value
   */
  Object value;

  String prefix;

  String suffix;

  boolean index;

  Map<String, Mapper> map = new HashMap<>();

  public Mapper(String key, Object value, String prefix, String suffix) {
    this.key = key;
    this.value = value;
    this.prefix = prefix;
    this.suffix = suffix;
  }

  public Mapper(String key, Object value) {
    this.key = key;
    this.value = value;
  }

  public Mapper(Object value) {
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Object getValue() {
    return value;
  }

  public Object getParseValue() {
    if (isEmpty()) {
      return value;
    }
    StringBuilder builder = new StringBuilder();
    for (Mapper mapper : map.values()) {
      builder.append(mapper.toString()).append(Symbol.COMMA);
    }
    builder.deleteCharAt(builder.length() - 1);
    return builder.toString();
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public boolean isIndex() {
    return index;
  }

  public void setIndex(boolean index) {
    this.index = index;
  }

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
    Class<?> s = cls.getSuperclass();
    while (o == null && s != null && s.getSuperclass() != null) {
      s = s.getSuperclass();
      o = map.get(s);
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

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return map.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return map.containsValue(value);
  }

  @Override
  public Mapper get(Object key) {
    return map.get(key);
  }

  @Override
  public Mapper put(String key, Mapper value) {
    return map.put(key, value);
  }

  @Override
  public Mapper remove(Object key) {
    return map.remove(key);
  }

  @Override
  public void putAll(Map<? extends String, ? extends Mapper> m) {
    map.putAll(m);
  }

  @Override
  public void clear() {
    map.clear();
  }

  @Override
  public Set<String> keySet() {
    return map.keySet();
  }

  @Override
  public Collection<Mapper> values() {
    return map.values();
  }

  @Override
  public Set<Entry<String, Mapper>> entrySet() {
    return map.entrySet();
  }

  @Override
  public String toString() {
    String s;
    if (key == null || index) {
      s = getParseValue().toString();
    } else {
      s = Symbol.QUOTES + getKey() + Symbol.QUOTES + Symbol.COLON + getParseValue();
    }

    if (suffix == null || prefix == null) {
      return s;
    }
    return prefix + s + suffix;
  }
}
