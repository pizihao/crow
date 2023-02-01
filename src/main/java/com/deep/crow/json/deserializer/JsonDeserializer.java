package com.deep.crow.json.deserializer;

import java.io.IOException;
import java.io.Reader;

/**
 * 反序列化器
 */
public interface JsonDeserializer<T> {

  /**
   * 反序列化操作
   *
   * @param reader 读取数据
   * @throws IOException 异常
   */
  T deserialize(Reader reader) throws IOException;
}
