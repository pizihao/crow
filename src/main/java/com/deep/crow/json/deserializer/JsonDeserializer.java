package com.deep.crow.json.deserializer;

/**
 * 反序列化器
 */
public interface JsonDeserializer<T> {

  /**
   * 反序列化操作
   *
   * @param str 读取数据
   */
  T deserialize(String str);
}
