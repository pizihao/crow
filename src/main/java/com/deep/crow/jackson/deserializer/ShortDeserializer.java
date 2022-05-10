package com.deep.crow.jackson.deserializer;

/**
 * <h2>Short 类型序列化</h2>
 *
 * @author Create by liuwenhao on 2022/5/10 16:52
 */
public class ShortDeserializer extends TypeDeserializer<Short> {

    String type = "java.lang.Short";

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Short getResult(String s) {
        return Short.parseShort(s);
    }
}