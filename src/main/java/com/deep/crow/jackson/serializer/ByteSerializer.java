package com.deep.crow.jackson.serializer;

/**
 * <h2>Byte 类型序列化</h2>
 *
 * @author Create by liuwenhao on 2022/5/10 16:52
 */
public class ByteSerializer extends TypeSerializer<Byte>{

    String str = "java.lang.Byte";

    @Override
    public String getStr() {
        return str;
    }
}