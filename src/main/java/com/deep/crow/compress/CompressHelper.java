package com.deep.crow.compress;

import com.deep.crow.exception.CrowException;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类型压缩器辅助
 *
 * @author Create by liuwenhao on 2022/6/2 16:09
 */
@SuppressWarnings("unused")
public class CompressHelper {

  /*
   * 类型和压缩器的映射关系
   */
  private static final Map<Class<?>, Class<? extends Compress>> compressMap =
      new ConcurrentHashMap<>();

  static {
    compressMap.put(Iterable.class, IteratorCompress.class);
    compressMap.put(Map.class, MapCompress.class);
  }

  /**
   * 通过类型获取一组压缩器
   *
   * @param type 类型
   * @param o    需要判断的对象
   * @return com.deep.crow.compress.Compress
   * @author liuwenhao
   * @date 2022/6/2 16:48
   */
  public static Compress getType(Type type, Object o) {
    return (type instanceof ParameterizedType)
        ? get(o, type)
        : new SimpleCompress(o, type);
  }

  /**
   * 通过类型获取一组压缩器
   *
   * @param type 类型
   * @param o    需要判断的对象
   * @return com.deep.crow.compress.Compress
   * @author liuwenhao
   * @date 2022/6/2 17:26
   */
  private static Compress get(Object o, Type type) {
    for (Map.Entry<Class<?>, Class<? extends Compress>> entry : compressMap.entrySet()) {
      if (entry.getKey().isInstance(o)) {
        Class<? extends Compress> aClass = compressMap.get(entry.getKey());
        try {
          Constructor<? extends Compress> constructor =
              aClass.getConstructor(Object.class, Type.class);
          return constructor.newInstance(o, type);
        } catch (Exception e) {
          throw CrowException.exception(e);
        }
      }
    }
    return new DefaultCompress(o, type);
  }

  /**
   * 添加一组压缩器映射 高级别的抽象会覆盖低级别的抽象<br>
   * 所以注册的压缩器不一定会生效
   *
   * @param key   键
   * @param value 值
   * @author liuwenhao
   * @date 2022/6/2 16:17
   */
  public synchronized void register(Class<?> key, Class<? extends Compress> value) {
    int flag = -1;
    int removeFlag = -1;
    Class<?> aClass = null;
    for (Class<?> cls : compressMap.keySet()) {
      if (!cls.isAssignableFrom(key)) {
        flag = flag + 1;
        if (key.isAssignableFrom(cls)) {
          removeFlag = removeFlag + 1;
          aClass = cls;
        }
      }
    }
    if (flag == compressMap.size()) {
      compressMap.put(key, value);
    }
    if (removeFlag > 0) {
      compressMap.remove(aClass);
    }
  }

  /**
   * 顶级覆盖 指定一组件键值对，如果已存在则覆盖，不存在则新增<br>
   * 覆盖时会找到键类型的抽象级别最高的一组进行覆盖<br>
   * 如果新指定的键类型抽象级别更高则会删除抽象级别更低的键<br>
   * 例如指定了 List Compress，则会覆盖 Iterable IteratorCompress，最终的结果为：Iterable Compress
   *
   * @param key   键
   * @param value 值
   * @author liuwenhao
   * @date 2022/6/7 10:31
   */
  public synchronized void topLevelCover(Class<?> key, Class<? extends Compress> value) {
    Class<?> coverKey = key;
    Class<?> delKey = null;
    for (Class<?> cls : compressMap.keySet()) {
      // 是否存在低级别的抽象
      if (key.isAssignableFrom(cls)) {
        delKey = cls;
      }
      // 存在高级别的抽象
      if (cls.isAssignableFrom(key)) {
        coverKey = cls;
      }
    }
    if (Objects.nonNull(delKey)) {
      compressMap.remove(delKey);
    }
    compressMap.put(coverKey, value);
  }

  /**
   * 获取所有的值
   *
   * @return java.util.Map<java.lang.Class < ?>,java.lang.Class<? extends
   * com.deep.crow.compress.Compress>>
   * @author liuwenhao
   * @date 2022/6/2 18:43
   */
  public Map<Class<?>, Class<? extends Compress>> getMap() {
    return new HashMap<>(compressMap);
  }
}
