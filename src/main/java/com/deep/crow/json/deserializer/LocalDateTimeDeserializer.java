package com.deep.crow.json.deserializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Boolean 类型反序列化
 *
 * @author Create by liuwenhao on 2022/5/10 16:33
 */
public class LocalDateTimeDeserializer extends TypeDeserializer<LocalDateTime> {

  String type = "java.time.LocalDateTime";

  DateTimeFormatter localDateTimePattern;

  public LocalDateTimeDeserializer(DateTimeFormatter localDateTimePattern) {
    this.localDateTimePattern = localDateTimePattern;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public LocalDateTime getResult(String s) {
    return LocalDateTime.parse(s, localDateTimePattern);
  }
}
