package com.deep.crow.json.data;

import com.deep.crow.json.Mapper;
import com.deep.crow.json.serializer.JsonSerializer;
import com.deep.crow.json.symbol.Symbol;
import com.deep.crow.util.ClassUtil;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 一个json数据段，包含一个对象中的所有属性，有若干个key和value组成
 */
public class Data {

  Writer writer;

  public Data(Object o) throws Exception {
    this.writer = new StringWriter();
    if (o == null) {
      writer.append(null);
      return;
    }
    Class<?> cls = o.getClass();
    if (cls.isArray() || o instanceof Iterable || o instanceof Map) {
      Value value = new Value(o);
      writer.append(value.toString());
      return;
    }

    if (cls.isEnum()) {
      writer.append(o.toString());
      return;
    }

    if (ClassUtil.isPrimitive(cls) || o instanceof CharSequence) {
      JsonSerializer<Object> serializer = Mapper.getSerializer(cls);
      serializer.serialize(o, writer);
      return;
    }
    writer.append(Symbol.LEFT_BRACES);
    parse(o);
    writer.append(Symbol.RIGHT_BRACES);
  }

  /**
   * 解析
   *
   * @param o 对象
   * @throws Exception 异常
   */
  private void parse(Object o) throws Exception {
    Class<?> cls = o.getClass();
    List<Field> fields = ClassUtil.getFieldsByGetterAndSetter(cls);
    if (fields.isEmpty()) {
      writer.append(o.toString());
      return;
    }
    for (Field f : fields) {
      PropertyDescriptor descriptor = new PropertyDescriptor(f.getName(), cls);
      Method readMethod = descriptor.getReadMethod();
      Key key = new Key(f.getName());
      Value value = new Value(readMethod.invoke(o));
      writer.append(key.toString()).append(value.toString()).append(Symbol.COMMA);
    }
    eliminate();
  }

  private void eliminate() throws IOException {
    Writer w = new StringWriter();
    String s = this.writer.toString();
    w.write(s, 0, s.length() - 1);
    this.writer = w;
  }

  @Override
  public String toString() {
    return writer.toString();
  }
}
