package com.deep.crow.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Boolean 类型序列化
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
  public void serialize(LocalDate value, Writer writer)
      throws IOException {
    String format = value.format(localDatePattern);
    writer.append(getStr()).append(format);
  }
}
