package com.deep.crow.jackson.serializer;

/**
 * <h2>Character 类型序列化</h2>
 *
 * @author Create by liuwenhao on 2022/5/10 16:52
 */
public class CharacterSerializer extends TypeSerializer<Character>{

    String str = "java.lang.Character";

    @Override
    public String getStr() {
        return str;
    }
}