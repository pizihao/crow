package com.deep.crow.json.serializer;

import java.io.IOException;
import java.io.Writer;

/**
 * 序列化器
 */
public interface JsonSerializer<T> {

  /**
   * 序列化操作
   *
   * @param value  值
   * @param writer 存储序列化值的对象
   * @throws IOException 异常
   */
  void serialize(T value, Writer writer) throws IOException;
}
