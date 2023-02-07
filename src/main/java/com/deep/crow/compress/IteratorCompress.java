package com.deep.crow.compress;

import com.deep.crow.util.ContainerUtil;
import com.deep.crow.util.JsonUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 迭代器类型
 *
 * @author Create by liuwenhao on 2022/6/2 15:28
 */
@SuppressWarnings("unchecked")
public class IteratorCompress extends AbstractCompress {

  public IteratorCompress(Object o, Type type) {
    super(o, type);
  }

  @Override
  public <T> T compress() {
    return type instanceof ParameterizedType
        ? (T) ContainerUtil.getFirstElement((Iterable<?>) o)
        : null;
  }

  @Override
  public boolean check() {
    try {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      Type argument = parameterizedType.getActualTypeArguments()[0];
      Type rawType = parameterizedType.getRawType();
      Object compress = compress();
      JsonUtil.objToString(compress, argument);
      return ((Class<?>) argument).isAssignableFrom(compress.getClass())
          && ((Class<?>) rawType).isAssignableFrom(o.getClass());
    } catch (Exception e) {
      return false;
    }
  }
}
