package com.deep.crow.json.data;

import com.deep.crow.json.symbol.Symbol;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * json数据中的键，这是一个比较简单的数据
 */
public class Key {

  /**
   * 键的值
   */
  Object keyName;

  /**
   * 通过键组装的数据
   */
  Writer keyRes;

  public Key(Object key) throws IOException {
    this.keyName = key;
    this.keyRes = new StringWriter();
    keyRes.append(Symbol.QUOTES).append(keyName.toString()).append(Symbol.QUOTES).append(Symbol.COLON);
  }

  public String getKeyRes() {
    return keyRes.toString();
  }

  public Object getKey() {
    return keyName;
  }

  @Override
  public String toString() {
    return getKeyRes();
  }

}
