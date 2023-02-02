package com.deep.crow.json.element;

import com.deep.crow.json.Mapper;
import com.deep.crow.json.symbol.Symbol;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Objects;

public class IterableElement implements Element {

  @Override
  public boolean isSupport(Type type) {
    if (Objects.isNull(type)) {
      return false;
    }
    Class<?> cls;
    if (type instanceof ParameterizedType) {
      cls = (Class<?>) ((ParameterizedType) type).getRawType();
    } else {
      cls = (Class<?>) type;
    }
    return Iterable.class.isAssignableFrom(cls);
  }

  @Override
  public void serializer(Mapper m, Object o, String key) {
    Iterable<?> iterable = (Iterable<?>) o;
    Mapper mapper = new Mapper(null, o, Symbol.LEFT_BRACKET, Symbol.RIGHT_BRACKET);
    int i = 0;
    for (Object obj : iterable) {
      Element element = Elements.getElement(obj.getClass());
      mapper.setIndex(true);
      element.serializer(mapper, obj, String.valueOf(i));
      i++;
    }
    m.put(key, mapper);
  }

  @Override
  public <T> T deserializer(String context, Type type) {
    return null;
  }
}
