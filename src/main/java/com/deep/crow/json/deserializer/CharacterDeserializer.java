package com.deep.crow.json.deserializer;

/**
 * Character 类型反序列化
 *
 * @author Create by liuwenhao on 2022/5/10 16:33
 */
public class CharacterDeserializer extends TypeDeserializer<Character> {

  String type = "java.lang.Character";

  @Override
  public String getType() {
    return type;
  }

  @Override
  public Character getResult(String s) {
    return s.charAt(0);
  }
}
