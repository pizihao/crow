package com.deep.crow.util;

import java.util.*;

/**
 * 涉及某些容器的操作工具类 如java提供的容器工具：{@link Collection},{@link Queue}等
 *
 * @author Create by liuwenhao on 2022/6/2 11:46
 */
public class ContainerUtil {

  private ContainerUtil() {}

  /**
   * 获取一个可迭代对象的首个元素
   *
   * @param iterable 可迭代的对象
   * @return T
   * @author liuwenhao
   * @date 2022/6/2 12:26
   */
  public static <T> T getFirstElement(Iterable<T> iterable) {
    if (Objects.isNull(iterable)) {
      return null;
    }
    Iterator<T> iterator = iterable.iterator();
    if (iterator.hasNext()) {
      return iterator.next();
    }
    return null;
  }

  /**
   * 获取一个Map对象的首个键值对
   *
   * @param map Map对象
   * @return Map.Entry<K, V>
   * @author liuwenhao
   * @date 2022/6/2 12:26
   */
  public static <K, V> Map.Entry<K, V> getFirstKeyValue(Map<K, V> map) {
    if (Objects.isNull(map)) {
      return null;
    }
    Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
    if (iterator.hasNext()) {
      return iterator.next();
    }
    return null;
  }
}
