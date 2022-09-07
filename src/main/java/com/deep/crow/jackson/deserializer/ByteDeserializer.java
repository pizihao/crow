package com.deep.crow.jackson.deserializer;

/**
 * Byte 类型反序列化
 *
 * @author Create by liuwenhao on 2022/5/10 16:33
 */
public class ByteDeserializer extends TypeDeserializer<Byte> {

  String type = "java.lang.Byte";

  @Override
  public String getType() {
    return type;
  }

  @Override
  public Byte getResult(String s) {
    return Byte.parseByte(s);
  }
}
