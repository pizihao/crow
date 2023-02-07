package com.deep.crow.json.deserializer;

/**
 * String 类型反序列化
 *
 * @author Create by liuwenhao on 2022/5/10 16:33
 */
public class CharSequenceDeserializer extends TypeDeserializer<CharSequence> {

  String type = "java.lang.CharSequence";

  @Override
  public String getType() {
    return type;
  }

  @Override
  public String getResult(String s) {
    return s;
  }

  @Override
  public CharSequence deserialize(String str) {
    String text = str.substring(1, str.length() - 1);
    return super.deserialize(text);
  }
}
