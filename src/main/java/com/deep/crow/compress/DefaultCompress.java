package com.deep.crow.compress;

import com.deep.crow.util.JsonUtil;

import java.lang.reflect.Type;

/**
 * 默认的检验器，存在泛型
 *
 * @author Create by liuwenhao on 2022/6/2 15:38
 */
@SuppressWarnings("unchecked")
public class DefaultCompress extends AbstractCompress {

  public DefaultCompress(Object o, Type type) {
    super(o, type);
  }

  @Override
  public <T> T compress() {
    return (T) o;
  }

  @Override
  public boolean check() {
    try {
      JsonUtil.objToString(compress(), type);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
