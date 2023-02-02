package com.deep.crow.json.element;

import com.deep.crow.json.Mapper;
import com.deep.crow.json.symbol.Symbol;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * 解析数组类型
 */
public class ArrayElement implements Element {

  @Override
  public boolean isSupport(Type type) {
    if (Objects.isNull(type) || type instanceof ParameterizedType) {
      return false;
    }
    return ((Class<?>) type).isArray();
  }

  @Override
  public void serializer(Mapper m, Object o, String key) {
    Object[] arr = (Object[]) o;
    Mapper mapper = new Mapper(null, o, Symbol.LEFT_BRACKET, Symbol.RIGHT_BRACKET);
    for (int i = 0; i < arr.length; i++) {
      Object obj = arr[i];
      Element element = Elements.getElement(obj.getClass());
      mapper.setIndex(true);
      element.serializer(mapper, obj, String.valueOf(i));
    }
    m.put(key, mapper);
  }

  @Override
  public <T> T deserializer(String context, Type type) {
    return null;
  }
}
