package com.deep.crow.json.data;

import com.deep.crow.exception.CrowException;
import com.deep.crow.json.Mapper;
import com.deep.crow.json.serializer.JsonSerializer;
import com.deep.crow.json.symbol.Symbol;
import com.deep.crow.util.ClassUtil;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * 数据值，其本身可能是一个完成的json
 */
public class Value {

  /**
   * value对象
   */
  Object obj;

  Writer objRes;

  public Value(Object obj) throws Exception {
    this.obj = obj;
    this.objRes = new StringWriter();
    if (obj == null) {
      objRes.append(null);
      return;
    }

    Class<?> cls = obj.getClass();
    if (cls.isAnnotation()) {
      throw new CrowException("无法解析枚注解类型");
    }
    // 如果是基本数据类型,CharSequence类型和枚举类型，则可以直接拼接
    if (cls.isEnum()) {
      objRes.append(String.valueOf(obj));
    }
    if (ClassUtil.isPrimitive(cls)) {
      JsonSerializer<Object> serializer = Mapper.getSerializer(cls);
      serializer.serialize(obj, objRes);
    } else if (obj instanceof CharSequence) {
      this.objRes.append(Symbol.QUOTES);
      JsonSerializer<Object> serializer = Mapper.getSerializer(cls);
      serializer.serialize(obj, objRes);
      this.objRes.append(Symbol.QUOTES);
    } else {
      parseValue(obj);
    }

  }

  /**
   * 当o是一个复杂的类型时，将其进行解析<br>
   * <b>注意：</b>
   * 对于一些特殊的类需要进行特殊的处理：<br>
   * <ul>
   *   <li>Array，数组类型，以[]的形式进行解析</li>
   *   <li>Iterable，集合类型，等同于数组类型</li>
   *   <li>实体类，以{}的形式进行解析</li>
   *   <li>Map，等同于实体类</li>
   * </ul>
   *
   * @param o 要序列化的对象
   */
  private void parseValue(Object o) throws Exception {
    if (o.getClass().isArray()) {
      parseValueArray(o);
    } else if (o instanceof Iterable) {
      parseValueIterable(o);
    } else if (o instanceof Map) {
      parseValueMap(o);
    } else {
      parseValueObject(o);
    }
  }

  /**
   * 转化数组
   *
   * @param arr 数组对象
   * @throws IOException 字符追加失败则出现IO异常
   */
  private void parseValueArray(Object arr) throws Exception {
    Object[] arrays = (Object[]) arr;
    this.objRes.append(Symbol.LEFT_BRACKET);
    for (Object o : arrays) {
      Value value = new Value(o);
      this.objRes.append(value.toString()).append(Symbol.COMMA);
    }
    eliminate();
    this.objRes.append(Symbol.RIGHT_BRACKET);
  }

  /**
   * 转化集合
   *
   * @param iter 集合对象
   * @throws IOException 字符追加失败则出现IO异常
   */
  private void parseValueIterable(Object iter) throws Exception {
    Iterable<?> iterable = (Iterable<?>) iter;
    this.objRes.append(Symbol.LEFT_BRACKET);
    for (Object o : iterable) {
      Value value = new Value(o);
      this.objRes.append(value.toString()).append(Symbol.COMMA);
    }
    eliminate();
    this.objRes.append(Symbol.RIGHT_BRACKET);
  }

  /**
   * 转化实体类
   *
   * @param o 对象
   * @throws IOException 字符追加失败则出现IO异常
   */
  private void parseValueObject(Object o) throws Exception {
    Data data = new Data(o);
    this.objRes.append(data.toString());
  }

  /**
   * 转化Map
   *
   * @param m Map对象
   * @throws IOException 字符追加失败则出现IO异常
   */
  private void parseValueMap(Object m) throws Exception {
    Map<?, ?> map = (Map<?, ?>) m;
    this.objRes.append(Symbol.LEFT_BRACES);
    for (Map.Entry<?, ?> entry : map.entrySet()) {
      Key key = new Key(entry.getKey());
      Value value = new Value(entry.getValue());
      this.objRes.append(key.toString()).append(value.toString()).append(Symbol.COMMA);
    }
    eliminate();
    this.objRes.append(Symbol.RIGHT_BRACES);
  }

  private void eliminate() throws IOException {
    Writer writer = new StringWriter();
    String s = this.objRes.toString();
    writer.write(s, 0, s.length() - 1);
    this.objRes = writer;
  }

  @Override
  public String toString() {
    return objRes.toString();
  }

}
