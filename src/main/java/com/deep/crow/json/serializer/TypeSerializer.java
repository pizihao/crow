package com.deep.crow.json.serializer;

/**
 * 基本数据类型序列化
 *
 * @author Create by liuwenhao on 2022/5/10 16:48
 */
public abstract class TypeSerializer<T> implements JsonSerializer<T> {

  public abstract String getStr();

  @Override
  public void serialize(T value, StringBuilder builder) {
    builder.append(getStr()).append(value);
  }
}
