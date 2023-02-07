package com.deep.crow.json.serializer;

/**
 * Long 类型序列化
 *
 * @author Create by liuwenhao on 2022/5/10 16:33
 */
public class LongSerializer extends TypeSerializer<Long> {

  String str = "java.lang.Long";

  @Override
  public String getStr() {
    return str;
  }
}
