package com.deep.crow.jackson.serializer;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * <h2>基本数据类型序列化</h2>
 *
 * @author Create by liuwenhao on 2022/5/10 16:48
 */
public abstract class TypeSerializer<T> extends JsonSerializer<T> {

    public abstract String getStr();

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(getStr() + value);
    }

    @Override
    public void serializeWithType(T value, JsonGenerator g, SerializerProvider provider,
                                  com.fasterxml.jackson.databind.jsontype.TypeSerializer typeSer) throws IOException {
        serialize(value, g, provider);
    }
}