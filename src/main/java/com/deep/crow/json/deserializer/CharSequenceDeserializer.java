package com.deep.crow.json.deserializer;

import com.deep.crow.exception.CrowException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * String 类型反序列化
 *
 * @author Create by liuwenhao on 2022/5/10 16:33
 */
public class CharSequenceDeserializer extends TypeDeserializer<CharSequence> {

  String type = "java.lang.CharSequence";

  @Override
  public String getType() {
    return type;
  }

  @Override
  public String getResult(String s) {
    return s;
  }

  @Override
  public CharSequence deserialize(Writer writer) {
    Writer w = new StringWriter();
    String s = w.toString();
    try {
      writer.write(s, 1, s.length() - 1);
    } catch (IOException e) {
      throw new CrowException(e);
    }
    return super.deserialize(w);
  }
}
