package com.deep.crow.jackson.deserializer;

/**
 * Long类型反序列化
 *
 * @author Create by liuwenhao on 2022/5/10 16:33
 */
public class LongDeserializer extends TypeDeserializer<Long> {

  String type = "java.lang.Long";

  @Override
  public String getType() {
    return type;
  }

  @Override
  public Long getResult(String s) {
    return Long.parseLong(s);
  }
}
