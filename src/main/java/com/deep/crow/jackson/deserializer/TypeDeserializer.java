package com.deep.crow.jackson.deserializer;

import com.deep.crow.exception.CrowException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.format.DateTimeParseException;

/**
 * <h2>基本数据类型反序列化</h2>
 *
 * @author Create by liuwenhao on 2022/5/10 16:36
 */
public abstract class TypeDeserializer<T> extends JsonDeserializer<T> {


    public abstract String getType();

    public abstract T getResult(String s);

    @Override
    public T deserialize(JsonParser p, DeserializationContext context) throws IOException {
        String text = p.getText();
        int length = getType().length();
        String type = text.substring(0, length);
        if (!getType().equals(type)) {
            throw CrowException.exception("类型映射错误 -- {}", getType());
        }
        String value = text.substring(type.length());
        try {
            return getResult(value);
        } catch (DateTimeParseException e) {
            throw CrowException.exception("类型映射错误 -- {}", getType());
        }
    }

}