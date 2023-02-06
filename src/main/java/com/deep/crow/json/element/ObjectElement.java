package com.deep.crow.json.element;

import com.deep.crow.exception.CrowException;
import com.deep.crow.json.Mapper;
import com.deep.crow.json.symbol.Symbol;
import com.deep.crow.util.ClassUtil;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.List;
import java.util.Objects;

public class ObjectElement implements Element {

  @Override
  public boolean isSupport(Type type) {
    if (Objects.isNull(type)) {
      return false;
    }
    Class<?> cls = getCls(type);
    return !(ClassUtil.isPrimitive(cls) || CharSequence.class.isAssignableFrom(cls));
  }

  @Override
  public Mapper serializer(Type type, Object o, String key, boolean isIndexKey) {
    Class<?> cls = o.getClass();
    String typeName = type.getTypeName();
    if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      typeName = parameterizedType.getRawType().getTypeName();
    }
    if (!typeName.equals(cls.getTypeName())) {
      throw CrowException.exception("类型不匹配");
    }
    List<Field> fields = ClassUtil.getFieldsByGetterAndSetter(cls);
    if (fields.isEmpty()) {
      return new Mapper(key, o, isIndexKey);
    }
    Mapper mapper = new Mapper(key, o, Symbol.LEFT_BRACES, Symbol.RIGHT_BRACES, isIndexKey);
    try {
      for (Field f : fields) {
        String name = f.getName();
        PropertyDescriptor descriptor = new PropertyDescriptor(name, cls);
        Method readMethod = descriptor.getReadMethod();
        Object invoke = readMethod.invoke(o);
        Element element = Elements.getElement(invoke.getClass());
        Mapper serializer = element.serializer(getFieldType(type, f), invoke, name, false);
        mapper.put(name, serializer);
      }
      return mapper;
    } catch (Exception e) {
      throw CrowException.exception(e);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T deserializer(Mapper mapper, Type type) {
    Class<?> cls = getCls(type);
    List<Field> fields = ClassUtil.getFieldsByGetterAndSetter(cls);
    T t;
    try {
      t = (T) cls.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw CrowException.exception(e);
    }
    if (!fields.isEmpty()) {
      try {
        for (Field f : fields) {
          Mapper entryMapper = mapper.get(f.getName());
          Type fieldType = getFieldType(type, f);
          Element element = Elements.getElement(fieldType);
          Object o = element.deserializer(entryMapper, fieldType);
          PropertyDescriptor descriptor = new PropertyDescriptor(f.getName(), cls);
          Method writeMethod = descriptor.getWriteMethod();
          writeMethod.invoke(t, o);
        }
      } catch (Exception e) {
        throw CrowException.exception(e);
      }
    }
    return t;
  }

}
