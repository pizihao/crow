package com.deep.crow.json.element;

import com.deep.crow.json.Mapper;
import com.deep.crow.json.symbol.Symbol;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class IterableElement implements Element {

  @Override
  public boolean isSupport(Type type) {
    if (Objects.isNull(type)) {
      return false;
    }
    Class<?> cls = getCls(type);
    return Iterable.class.isAssignableFrom(cls);
  }

  @Override
  public Mapper serializer(Object o, String key, boolean isIndexKey) {
    Iterable<?> iterable = (Iterable<?>) o;
    Mapper mapper = new Mapper(key, o, Symbol.LEFT_BRACKET, Symbol.RIGHT_BRACKET, isIndexKey);
    int i = 0;
    for (Object obj : iterable) {
      Element element = Elements.getElement(obj.getClass());
      String s = String.valueOf(i);
      Mapper serializer = element.serializer(obj, s, true);
      mapper.put(s, serializer);
      i++;
    }
    return mapper;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T deserializer(Mapper mapper, Type type) {
    List<Object> list = new ArrayList<>();
    ParameterizedType parameterizedType = (ParameterizedType) type;
    Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
    Element element = Elements.getElement(actualTypeArgument);
    for (Mapper m : mapper.values()) {
      Object deserializer = element.deserializer(m, actualTypeArgument);
      list.add(deserializer);
    }
    return (T) list;
  }
}
