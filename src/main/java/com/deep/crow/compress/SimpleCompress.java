package com.deep.crow.compress;

import java.lang.reflect.Type;

/**
 * 简单类型，无泛型
 *
 * @author Create by liuwenhao on 2022/6/2 14:29
 */
@SuppressWarnings("unchecked")
public class SimpleCompress extends AbstractCompress {

  public SimpleCompress(Object o, Type type) {
    super(o, type);
  }

  @Override
  public <T> T compress() {
    return (T) o;
  }

  @Override
  public boolean check() {
    return ((Class<?>) type).isInstance(compress());
  }
}
