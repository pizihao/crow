package com.deep.crow.json.element;

import com.deep.crow.json.Mapper;
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
    Class<?> cls = (Class<?>) type;
    return ClassUtil.isPrimitive(cls) || CharSequence.class.isAssignableFrom(cls);
  }

  @Override
  public void serializer(Mapper m, Object o, String key) {
    Class<?> cls = o.getClass();
    JsonSerializer<Object> serializer = Mapper.getSerializer(cls);
    StringBuilder builder = new StringBuilder();
    serializer.serialize(o, builder);
    Mapper mapper = new Mapper(null, builder.toString());
    mapper.setIndex(m.isIndex());
    m.setIndex(false);
    m.put(key, mapper);
  }

  @Override
  public <T> T deserializer(String context, Type type) {
    return null;
  }

}
