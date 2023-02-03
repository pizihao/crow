package com.deep.crow.json.element;

import com.deep.crow.json.Mapper;
import com.deep.crow.json.deserializer.JsonDeserializer;
import com.deep.crow.json.serializer.JsonSerializer;
import com.deep.crow.util.ClassUtil;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * 针对基本数据类型和串类型的序列化操作
 */
public class SimpleElement implements Element {

  @Override
  public boolean isSupport(Type type) {
    if (Objects.isNull(type)) {
      return false;
    }
    Class<?> cls = getCls(type);
    return ClassUtil.isPrimitive(cls) || CharSequence.class.isAssignableFrom(cls);
  }

  @Override
  public Mapper serializer(Object o, String key, boolean isIndexKey) {
    Class<?> cls = o.getClass();
    JsonSerializer<Object> serializer = Mapper.getSerializer(cls);
    StringBuilder builder = new StringBuilder();
    serializer.serialize(o, builder);
    return new Mapper(key, builder.toString(), isIndexKey);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T deserializer(Mapper mapper, Type type) {
    Object value = mapper.getValue();
    String s = String.valueOf(value);
    Class<?> cls = getCls(type);
    JsonDeserializer<Object> deserializer = Mapper.getDeserializer(cls);
    return (T) deserializer.deserialize(s);
  }

}
