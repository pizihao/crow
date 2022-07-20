package com.deep.crow.jackson.serializer;

/**
 * <h2>Short 类型序列化</h2>
 *
 * @author Create by liuwenhao on 2022/5/10 16:52
 */
public class ShortSerializer extends TypeSerializer<Short>{

    String str = "java.lang.Short";

    @Override
    public String getStr() {
        return str;
    }
}