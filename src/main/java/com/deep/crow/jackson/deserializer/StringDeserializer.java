package com.deep.crow.jackson.deserializer;

/**
 * String 类型反序列化
 *
 * @author Create by liuwenhao on 2022/5/10 16:33
 */
public class StringDeserializer extends TypeDeserializer<String> {

  String type = "java.lang.String";

  @Override
  public String getType() {
    return type;
  }

  @Override
  public String getResult(String s) {
    return s;
  }
}
