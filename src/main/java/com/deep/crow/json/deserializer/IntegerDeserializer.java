package com.deep.crow.json.deserializer;

/**
 * Integer 类型序列化
 *
 * @author Create by liuwenhao on 2022/5/10 16:52
 */
public class IntegerDeserializer extends TypeDeserializer<Integer> {

  String type = "java.lang.Integer";

  @Override
  public String getType() {
    return type;
  }

  @Override
  public Integer getResult(String s) {
    return Integer.parseInt(s);
  }
}
