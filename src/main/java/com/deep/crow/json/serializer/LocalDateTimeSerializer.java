package com.deep.crow.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Boolean 类型序列化
 *
 * @author Create by liuwenhao on 2022/5/10 16:52
 */
public class LocalDateTimeSerializer extends TypeSerializer<LocalDateTime> {

  String str = "java.time.LocalDateTime";

  DateTimeFormatter localDateTimePattern;

  public LocalDateTimeSerializer(DateTimeFormatter localDateTimePattern) {
    this.localDateTimePattern = localDateTimePattern;
  }

  @Override
  public String getStr() {
    return str;
  }

  @Override
  public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    String format = value.format(localDateTimePattern);
    gen.writeString(getStr() + format);
  }
}
