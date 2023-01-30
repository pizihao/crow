package com.deep.crow.json.serializer;

/**
 * Double 类型序列化
 *
 * @author Create by liuwenhao on 2022/5/10 16:52
 */
public class DoubleSerializer extends TypeSerializer<Double> {

  String str = "java.lang.Double";

  @Override
  public String getStr() {
    return str;
  }
}
