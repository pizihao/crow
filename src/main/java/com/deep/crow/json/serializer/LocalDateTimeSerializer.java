package com.deep.crow.json.serializer;

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
  public void serialize(LocalDateTime value, StringBuilder builder) {
    String format = value.format(localDateTimePattern);
    builder.append(getStr()).append(format);
  }
}
