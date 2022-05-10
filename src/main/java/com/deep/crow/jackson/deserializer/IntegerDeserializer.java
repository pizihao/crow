package com.deep.crow.jackson.deserializer;

import com.deep.crow.jackson.serializer.TypeSerializer;

/**
 * <h2>Integer 类型序列化</h2>
 *
 * @author Create by liuwenhao on 2022/5/10 16:52
 */
public class IntegerDeserializer extends TypeDeserializer<Integer> {

    String type = "java.lang.Integer";

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Integer getResult(String s) {
        return Integer.parseInt(s);
    }
}