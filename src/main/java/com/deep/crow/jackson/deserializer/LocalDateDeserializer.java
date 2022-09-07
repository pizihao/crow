package com.deep.crow.jackson.deserializer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Boolean 类型反序列化
 *
 * @author Create by liuwenhao on 2022/5/10 16:33
 */
public class LocalDateDeserializer extends TypeDeserializer<LocalDate> {

  String type = "java.time.LocalDate";

  DateTimeFormatter localDatePattern;

  public LocalDateDeserializer(DateTimeFormatter localDatePattern) {
    this.localDatePattern = localDatePattern;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public LocalDate getResult(String s) {
    return LocalDate.parse(s, localDatePattern);
  }
}
