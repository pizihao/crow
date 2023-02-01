package com.deep.crow.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.io.Writer;

/**
 * 基本数据类型序列化
 *
 * @author Create by liuwenhao on 2022/5/10 16:48
 */
public abstract class TypeSerializer<T> implements JsonSerializer<T> {

  public abstract String getStr();

  @Override
  public void serialize(T value, Writer writer)
      throws IOException {
    writer.append(getStr()).append(String.valueOf(value));
  }
}
