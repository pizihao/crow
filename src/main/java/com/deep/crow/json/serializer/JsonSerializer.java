package com.deep.crow.json.serializer;

import java.io.IOException;

/**
 * 序列化器
 */
public interface JsonSerializer<T> {

  /**
   * 序列化操作
   *
   * @param value   值
   * @param builder 存储序列化值的对象
   */
  void serialize(T value, StringBuilder builder);
}
