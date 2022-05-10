package com.deep.crow.jackson.serializer;

/**
 * <h2>Integer 类型序列化</h2>
 *
 * @author Create by liuwenhao on 2022/5/10 16:52
 */
public class IntegerSerializer extends TypeSerializer<Integer>{

    String str = "java.lang.Integer";

    @Override
    public String getStr() {
        return str;
    }
}