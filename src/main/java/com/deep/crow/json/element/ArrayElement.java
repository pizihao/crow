package com.deep.crow.json.element;

import com.deep.crow.exception.CrowException;
import com.deep.crow.json.Mapper;
import com.deep.crow.json.symbol.Symbol;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 解析数组类型
 */
public class ArrayElement implements Element {

  @Override
  public boolean isSupport(Type type) {
    if (Objects.isNull(type) || type instanceof ParameterizedType) {
      return false;
    }
    return getCls(type).isArray();
  }

  @Override
  public Mapper serializer(Type type, Object o, String key, boolean isIndexKey) {
    Object[] arr = (Object[]) o;
    if (!(type instanceof GenericArrayType)) {
      throw CrowException.exception("类型不匹配");
    }
    GenericArrayType genericArrayType = (GenericArrayType) type;
    Type componentType = genericArrayType.getGenericComponentType();
    Mapper mapper = new Mapper(key, o, Symbol.LEFT_BRACKET, Symbol.RIGHT_BRACKET, isIndexKey);
    for (int i = 0; i < arr.length; i++) {
      Object obj = arr[i];
      String s = String.valueOf(i);
      Element element = Elements.getElement(obj.getClass());
      Mapper serializer = element.serializer(componentType, obj, s, true);
      mapper.put(s, serializer);
    }
    return mapper;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T deserializer(Mapper mapper, Type type) {
    ParameterizedType parameterizedType = (ParameterizedType) type;
    Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
    Element element = Elements.getElement(actualTypeArgument);
    List<Mapper> values = new ArrayList<>(mapper.values());
    Object[] o = new Object[values.size()];
    for (int i = 0; i < values.size(); i++) {
      Mapper m = values.get(i);
      Object deserializer = element.deserializer(m, actualTypeArgument);
      o[i] = deserializer;
    }
    return (T) o;
  }
}
