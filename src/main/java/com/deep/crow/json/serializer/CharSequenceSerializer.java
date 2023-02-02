package com.deep.crow.json.serializer;

import com.deep.crow.json.symbol.Symbol;

/**
 * Boolean 类型序列化
 *
 * @author Create by liuwenhao on 2022/5/10 16:52
 */
public class CharSequenceSerializer extends TypeSerializer<CharSequence> {

  String str = "java.lang.CharSequence";

  @Override
  public String getStr() {
    return str;
  }

  @Override
  public void serialize(CharSequence value, StringBuilder builder) {
    builder.append(Symbol.QUOTES);
    super.serialize(value, builder);
    builder.append(Symbol.QUOTES);
  }
}
