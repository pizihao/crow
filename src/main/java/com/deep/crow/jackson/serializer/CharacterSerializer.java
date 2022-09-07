package com.deep.crow.jackson.serializer;

/**
 * Character 类型序列化
 *
 * @author Create by liuwenhao on 2022/5/10 16:52
 */
public class CharacterSerializer extends TypeSerializer<Character> {

  String str = "java.lang.Character";

  @Override
  public String getStr() {
    return str;
  }
}
