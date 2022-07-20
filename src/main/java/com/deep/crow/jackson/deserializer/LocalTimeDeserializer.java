package com.deep.crow.jackson.deserializer;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * <h2>Boolean 类型反序列化</h2>
 *
 * @author Create by liuwenhao on 2022/5/10 16:33
 */
public class LocalTimeDeserializer extends TypeDeserializer<LocalTime> {

    String type = "java.time.LocalTime";

    DateTimeFormatter localTimePattern;

    public LocalTimeDeserializer(DateTimeFormatter localTimePattern) {
        this.localTimePattern = localTimePattern;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public LocalTime getResult(String s) {
        return LocalTime.parse(s, localTimePattern);
    }
}