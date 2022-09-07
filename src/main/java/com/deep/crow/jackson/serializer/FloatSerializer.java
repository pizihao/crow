package com.deep.crow.jackson.serializer;

/**
 * Float 类型序列化
 *
 * @author Create by liuwenhao on 2022/5/10 16:52
 */
public class FloatSerializer extends TypeSerializer<Float> {

  String str = "java.lang.Float";

  @Override
  public String getStr() {
    return str;
  }
}
