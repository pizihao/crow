package com.deep.crow.json.element;

import com.deep.crow.exception.CrowException;
import com.deep.crow.json.Mapper;
import com.deep.crow.json.symbol.Symbol;
import com.deep.crow.util.ClassUtil;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public class ObjectElement implements Element {

  @Override
  public boolean isSupport(Type type) {
    if (Objects.isNull(type)) {
      return false;
    }
    Class<?> cls = (Class<?>) type;
    return !(ClassUtil.isPrimitive(cls) || CharSequence.class.isAssignableFrom(cls));
  }

  @Override
  public void serializer(Mapper m, Object o, String key) {
    Class<?> cls = o.getClass();
    List<Field> fields = ClassUtil.getFieldsByGetterAndSetter(cls);
    if (fields.isEmpty()) {
      Mapper mapper = new Mapper(key, o);
      mapper.setIndex(m.isIndex());
      m.setIndex(false);
      m.put(key, mapper);
      return;
    }
    Mapper mapper = new Mapper(key, o, Symbol.LEFT_BRACES, Symbol.RIGHT_BRACES);
    mapper.setIndex(m.isIndex());
    m.setIndex(false);
    try {
      for (Field f : fields) {
        String name = f.getName();
        PropertyDescriptor descriptor = new PropertyDescriptor(name, cls);
        Method readMethod = descriptor.getReadMethod();
        Object invoke = readMethod.invoke(o);
        Element element = Elements.getElement(invoke.getClass());
        Mapper invokeMapper = new Mapper(name, invoke);
        element.serializer(invokeMapper, invoke, null);
        mapper.put(name, invokeMapper);
      }
      m.put(key, mapper);
    } catch (Exception e) {
      throw new CrowException(e);
    }
  }

  @Override
  public <T> T deserializer(String context, Type type) {
    return null;
  }
}
