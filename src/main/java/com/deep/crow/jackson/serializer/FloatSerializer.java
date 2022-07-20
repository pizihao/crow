package com.deep.crow.jackson.serializer;

/**
 * <h2>Float 类型序列化</h2>
 *
 * @author Create by liuwenhao on 2022/5/10 16:52
 */
public class FloatSerializer extends TypeSerializer<Float>{

    String str = "java.lang.Float";

    @Override
    public String getStr() {
        return str;
    }
}