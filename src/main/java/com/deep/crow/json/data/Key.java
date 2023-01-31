package com.deep.crow.json.data;

import com.deep.crow.json.symbol.Symbol;

/**
 * json数据中的键，这是一个比较简单的数据
 */
public class Key {

  /**
   * 键的值
   */
  String keyName;

  /**
   * 通过键组装的数据
   */
  String keyRes;

  public Key(String key) {
    this.keyName = key;
    this.keyRes = Symbol.quotes + keyName + Symbol.quotes + Symbol.space + Symbol.colon;
  }

  public String getKeyRes() {
    return keyRes;
  }

  public String getKey() {
    return keyName;
  }

  @Override
  public String toString() {
    return getKeyRes();
  }

}
