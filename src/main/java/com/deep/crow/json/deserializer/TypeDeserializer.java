package com.deep.crow.json.deserializer;

import com.deep.crow.exception.CrowException;

import java.time.format.DateTimeParseException;

/**
 * 基本数据类型反序列化
 *
 * @author Create by liuwenhao on 2022/5/10 16:36
 */
public abstract class TypeDeserializer<T> implements JsonDeserializer<T> {

  public abstract String getType();

  public abstract T getResult(String s);

  @Override
  public T deserialize(String str) {
    int length = getType().length();
    String type = str.substring(0, length);
    if (!getType().equals(type)) {
      throw CrowException.exception("类型映射错误 -- {}", getType());
    }
    String value = str.substring(type.length());
    try {
      return getResult(value);
    } catch (DateTimeParseException e) {
      throw CrowException.exception("类型映射错误 -- {}", getType());
    }
  }
}
