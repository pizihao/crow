package com.deep.crow.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * <h2>Boolean 类型序列化</h2>
 *
 * @author Create by liuwenhao on 2022/5/10 16:52
 */
public class LocalDateSerializer extends TypeSerializer<LocalDate> {

    String str = "java.time.LocalDate";

    DateTimeFormatter localDatePattern;

    public LocalDateSerializer(DateTimeFormatter localDatePattern) {
        this.localDatePattern = localDatePattern;
    }

    @Override
    public String getStr() {
        return str;
    }

    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String format = value.format(localDatePattern);
        gen.writeString(getStr() + format);
    }
}