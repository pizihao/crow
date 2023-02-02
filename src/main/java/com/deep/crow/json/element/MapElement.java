package com.deep.crow.json.element;

import com.deep.crow.json.Mapper;
import com.deep.crow.json.symbol.Symbol;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class MapElement implements Element {
  @Override
  public boolean isSupport(Type type) {
    if (type instanceof ParameterizedType) {
      Class<?> cls = (Class<?>) ((ParameterizedType) type).getRawType();
      return Map.class.isAssignableFrom(cls);
    }

    Class<?> cls = (Class<?>) type;
    return Map.class.isAssignableFrom(cls);
  }

  @Override
  public void serializer(Mapper m, Object o, String key) {
    Map<?, ?> map = (Map<?, ?>) o;
    Mapper mapper = new Mapper(null, o, Symbol.LEFT_BRACES, Symbol.RIGHT_BRACES);
    mapper.setIndex(m.isIndex());
    m.setIndex(false);
    for (Map.Entry<?, ?> entry : map.entrySet()) {
      Object entryKey = entry.getKey();
      Object entryValue = entry.getValue();
      Mapper entryMapper = new Mapper(String.valueOf(entryKey), entryValue);
      Element element = Elements.getElement(entryValue.getClass());
      element.serializer(entryMapper, entryValue, null);
      mapper.put(String.valueOf(entryKey), entryMapper);
    }
    m.put(key, mapper);
  }

  @Override
  public <T> T deserializer(String context, Type type) {
    return null;
  }
}
