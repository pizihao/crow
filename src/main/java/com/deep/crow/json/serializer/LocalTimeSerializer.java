package com.deep.crow.json.serializer;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Boolean 类型序列化
 *
 * @author Create by liuwenhao on 2022/5/10 16:52
 */
public class LocalTimeSerializer extends TypeSerializer<LocalTime> {

  String str = "java.time.LocalTime";

  DateTimeFormatter localTimePattern;

  public LocalTimeSerializer(DateTimeFormatter localTimePattern) {
    this.localTimePattern = localTimePattern;
  }

  @Override
  public String getStr() {
    return str;
  }

  @Override
  public void serialize(LocalTime value, StringBuilder builder) {
    String format = value.format(localTimePattern);
    builder.append(getStr()).append(format);
  }
}
