package com.deep.crow.json.serializer;

/**
 * Boolean 类型序列化
 *
 * @author Create by liuwenhao on 2022/5/10 16:52
 */
public class BooleanSerializer extends TypeSerializer<Boolean> {

  String str = "java.lang.Boolean";

  @Override
  public String getStr() {
    return str;
  }
}
