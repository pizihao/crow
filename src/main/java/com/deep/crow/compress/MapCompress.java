package com.deep.crow.compress;

import com.deep.crow.util.ContainerUtil;
import com.deep.crow.util.JsonUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 映射集合压缩器
 *
 * @author Create by liuwenhao on 2022/6/2 15:48
 */
@SuppressWarnings("unchecked")
public class MapCompress extends AbstractCompress {

  public MapCompress(Object o, Type type) {
    super(o, type);
  }

  @Override
  public <T> T compress() {
    Map<?, ?> map = (Map<?, ?>) o;
    return (T) ContainerUtil.getFirstKeyValue(map);
  }

  @Override
  public boolean check() {
    try {
      Map.Entry<?, ?> firstKeyValue = compress();
      ParameterizedType parameterizedType = (ParameterizedType) type;
      Type keyArgument = parameterizedType.getActualTypeArguments()[0];
      Type valueArgument = parameterizedType.getActualTypeArguments()[1];
      Type rawType = parameterizedType.getRawType();
      JsonUtil.objToString(firstKeyValue.getKey(), keyArgument);
      JsonUtil.objToString(firstKeyValue.getValue(), valueArgument);
      return ((Class<?>) keyArgument).isAssignableFrom(firstKeyValue.getKey().getClass())
          && ((Class<?>) valueArgument).isAssignableFrom(firstKeyValue.getValue().getClass())
          && ((Class<?>) rawType).isAssignableFrom(o.getClass());
    } catch (Exception e) {
      return false;
    }
  }
}
