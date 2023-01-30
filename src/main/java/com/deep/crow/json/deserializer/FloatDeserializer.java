package com.deep.crow.json.deserializer;

/**
 * Float 类型序列化
 *
 * @author Create by liuwenhao on 2022/5/10 16:52
 */
public class FloatDeserializer extends TypeDeserializer<Float> {

  String type = "java.lang.Float";

  @Override
  public String getType() {
    return type;
  }

  @Override
  public Float getResult(String s) {
    return Float.parseFloat(s);
  }
}
