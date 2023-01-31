package com.deep.crow.json.data;

import com.deep.crow.exception.CrowException;
import com.deep.crow.json.symbol.Symbol;

/**
 * 数据值，其本身可能是一个完成的json
 */
public class Value {

  /**
   * value对象
   */
  Object obj;

  String objRes;

  public Value(Object obj) {
    if (obj.getClass().isEnum()) {
      throw new CrowException("无法解析枚举类型");
    }
    if (obj.getClass().isArray()) {
      throw new CrowException("无法解析枚数组类型");
    }
    if (obj.getClass().isAnnotation()) {
      throw new CrowException("无法解析枚注解类型");
    }

    this.obj = obj;
    // 如果是基本数据类型
    if (obj.getClass().isPrimitive() || obj instanceof String) {
      this.objRes = Symbol.space + this.obj + Symbol.comma;
    }

  }

  /**
   * 当o是一个复杂的类型时，将其进行解析，除Iterable和Map类型外的对象，通过其getter和setter方式获取结果
   *
   * @param o
   * @return
   */
  private Value parseValue(Object o) {

  }


}
