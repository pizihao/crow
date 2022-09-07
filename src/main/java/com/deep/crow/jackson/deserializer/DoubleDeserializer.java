package com.deep.crow.jackson.deserializer;

/**
 * Integer 类型序列化
 *
 * @author Create by liuwenhao on 2022/5/10 16:52
 */
public class DoubleDeserializer extends TypeDeserializer<Double> {

  String type = "java.lang.Double";

  @Override
  public String getType() {
    return type;
  }

  @Override
  public Double getResult(String s) {
    return Double.parseDouble(s);
  }
}
