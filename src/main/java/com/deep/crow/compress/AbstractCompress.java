package com.deep.crow.compress;

import java.lang.reflect.Type;

/**
 * 抽象压缩器 提供了必须的属性，从而可以在隐藏接口的情况下对压缩器进行扩展
 *
 * @author Create by liuwenhao on 2022/6/2 16:19
 */
public abstract class AbstractCompress implements Compress {
  Object o;
  Type type;

  AbstractCompress(Object o, Type type) {
    this.o = o;
    this.type = type;
  }
}
