package com.deep.crow.jackson.deserializer;

/**
 * <h2>Boolean 类型反序列化</h2>
 *
 * @author Create by liuwenhao on 2022/5/10 16:33
 */
public class BooleanDeserializer extends TypeDeserializer<Boolean> {

    String type = "java.lang.Boolean";


    @Override
    public String getType() {
        return type;
    }

    @Override
    public Boolean getResult(String s) {
        return Boolean.parseBoolean(s);
    }
}