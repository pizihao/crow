package com.deep.crow.jackson.serializer;

/**
 * <h2>Long 类型序列化</h2>
 *
 * @author Create by liuwenhao on 2022/5/10 16:33
 */
public class LongSerializer extends TypeSerializer<Long> {

    String str = "java.lang.Long";

    @Override
    public String getStr() {
        return str;
    }

}