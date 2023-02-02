package com.deep.crow.json.deserializer;

import java.io.IOException;
import java.io.Writer;

/**
 * 反序列化器
 */
public interface JsonDeserializer<T> {

  /**
   * 反序列化操作
   *
   * @param writer 读取数据
   */
  T deserialize(Writer writer);
}
