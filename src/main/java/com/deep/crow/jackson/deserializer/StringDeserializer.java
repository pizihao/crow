package com.deep.crow.jackson.deserializer;

/**
 * <h2>String 类型反序列化</h2>
 *
 * @author Create by liuwenhao on 2022/5/10 16:33
 */
public class StringDeserializer extends TypeDeserializer<String> {

    String type = "java.lang.String";


    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getResult(String s) {
        return s;
    }
}