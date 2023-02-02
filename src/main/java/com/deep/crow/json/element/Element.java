package com.deep.crow.json.element;

import com.deep.crow.json.Mapper;

import java.lang.reflect.Type;

/**
 * 类型元素，代表Json中可解析的元素类型
 */
public interface Element {

  /**
   * 是否是可解析的类型
   *
   * @param type 类型
   * @return 是否可以解析
   */
  boolean isSupport(Type type);

  /**
   * 序列化
   *
   * @param o   待序列化的对象
   * @param key key
   */
  void serializer(Mapper m, Object o, String key);

  /**
   * 反序列化操作
   *
   * @param context 可反序列化的文本
   * @param type    反序列化的目标类型
   * @return 序列化后的对象
   */
  <T> T deserializer(String context, Type type);
}
