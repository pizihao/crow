package com.deep.crow.json.element;

import com.deep.crow.json.Mapper;
import com.deep.crow.json.symbol.Symbol;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MapElement implements Element {
  @Override
  public boolean isSupport(Type type) {
    Class<?> cls = getCls(type);
    return Map.class.isAssignableFrom(cls);
  }

  @Override
  public Mapper serializer(Object o, String key, boolean isIndexKey) {
    Map<?, ?> map = (Map<?, ?>) o;
    Mapper mapper = new Mapper(key, o, Symbol.LEFT_BRACES, Symbol.RIGHT_BRACES, isIndexKey);
    for (Map.Entry<?, ?> entry : map.entrySet()) {
      Object entryKey = entry.getKey();
      Object entryValue = entry.getValue();
      String s = String.valueOf(entryKey);
      Element element = Elements.getElement(entryValue.getClass());
      Mapper serializer = element.serializer(entryValue, s, false);
      mapper.put(s, serializer);
    }
    return mapper;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T deserializer(Mapper mapper, Type type) {
    ParameterizedType parameterizedType = (ParameterizedType) type;
    Type valueType = parameterizedType.getActualTypeArguments()[1];
    Map<Object, Object> map = new HashMap<>();
    for (Map.Entry<String, Mapper> entry : mapper.entrySet()) {
      String key = entry.getKey();
      Mapper value = entry.getValue();
      Element element = Elements.getElement(valueType);
      Object deserializer = element.deserializer(value, valueType);
      map.put(key, deserializer);
    }
    return (T) map;
  }
}
