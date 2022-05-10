package com.deep.crow.jackson.deserializer;

/**
 * <h2>Character 类型反序列化</h2>
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