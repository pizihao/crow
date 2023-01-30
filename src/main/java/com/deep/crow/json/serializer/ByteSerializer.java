package com.deep.crow.json.serializer;

/**
 * Byte 类型序列化
 *
 * @author Create by liuwenhao on 2022/5/10 16:52
 */
public class ByteSerializer extends TypeSerializer<Byte> {

  String str = "java.lang.Byte";

  @Override
  public String getStr() {
    return str;
  }
}
