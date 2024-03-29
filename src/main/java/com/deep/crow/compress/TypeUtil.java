package com.deep.crow.compress;

import com.deep.crow.exception.CrowException;
import com.deep.crow.type.ParameterizedTypeImpl;
import com.deep.crow.util.ClassUtil;
import com.deep.crow.util.ContainerUtil;
import com.deep.crow.util.JsonUtil;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.*;

/**
 * 类型填充工具类
 *
 * @author Create by liuwenhao on 2022/4/22 19:25
 */
@SuppressWarnings("unchecked,unused")
public class TypeUtil {

  private TypeUtil() {
  }

  /**
   * 筛选结果类型 在一个未知的结果集中匹配指定类型的项，匹配成功则直接返回<br>
   * 无匹配选项则抛出异常
   *
   * @param l     结果集
   * @param clazz 目标类
   * @return T
   * @author liuwenhao
   * @date 2022/4/22 19:33
   */
  public static <T> T screenClass(Iterable<?> l, Class<T> clazz) {
    for (Object o : l) {
      boolean instance = clazz.isInstance(o);
      if (instance) {
        return (T) o;
      }
    }
    throw CrowException.exception("无可匹配类型，{}", clazz.getName());
  }

  /**
   * 筛选结果类型 在一个未知的结果集中匹配指定类型的项，得到所有可能匹配的结果<br>
   * 无匹配选项则获得空集合
   *
   * @param l     结果集
   * @param clazz 目标类
   * @return T
   * @author liuwenhao
   * @date 2022/4/22 19:33
   */
  public static <T> List<T> screenClasses(Iterable<?> l, Class<T> clazz) {
    List<T> list = new ArrayList<>();
    for (Object o : l) {
      boolean instance = clazz.isInstance(o);
      if (instance) {
        list.add((T) o);
      }
    }
    return list;
  }

  /**
   * 筛选结果类型 在一个未知的结果集中匹配指定类型的项<br>
   * 无匹配选项则抛出异常
   *
   * @param l     结果集
   * @param clazz 目标类
   * @param types 泛型
   * @return T
   * @author liuwenhao
   * @date 2022/4/22 19:33
   */
  public static <T> T screenType(Iterable<?> l, Class<T> clazz, Type... types) {
    if (Objects.isNull(types)) {
      return screenClass(l, clazz);
    }
    // 需要获取的类型
    ParameterizedType parameterizedType = ParameterizedTypeImpl.make(clazz, types);
    return screenType(l, parameterizedType);
  }

  /**
   * 筛选结果类型 在一个未知的结果集中匹配指定类型的项，得到所有可能匹配的结果<br>
   * 无匹配选项则返回空集合
   *
   * @param l     结果集
   * @param clazz 目标类
   * @param types 泛型
   * @return T
   * @author liuwenhao
   * @date 2022/4/22 19:33
   */
  public static <T> List<T> screenTypes(Iterable<?> l, Class<?> clazz, Type... types) {
    // 需要获取的类型
    ParameterizedType parameterizedType = ParameterizedTypeImpl.make(clazz, types);
    return screenTypes(l, parameterizedType);
  }

  /**
   * 筛选结果类型 在一个未知的结果集中匹配指定类型的项<br>
   * 无匹配选项则抛出异常
   *
   * @param l    结果集
   * @param type 检索的类型
   * @return T
   * @author liuwenhao
   * @date 2022/4/24 16:27
   */
  public static <T> T screenType(Iterable<?> l, Type type) {
    // 如果type的类型直接是class，则可以通过比较简单的方式去转化
    if (type instanceof Class) {
      Class<?> cls = (Class<?>) type;
      return (T) screenClass(l, cls);
    }

    /*
     * o及其泛型是否与parameterizedType兼容，
     * 1，通过API获取o被擦除的泛型类型，和传入的类型进行对比，相同则返回
     *      但是并没有找到对应的API，因为o的类型是Object，o如果存在泛型，那么也是Object，
     *      其真实类型是未知的，所以并不支持这种获取方式。
     * 2，通过序列化的方式，先通过参数指明类型进行序列化
     *      如果成功，则说明类型兼容，反之不兼容
     */

    if (type instanceof ParameterizedType) {
      for (Object o : l) {
        boolean match = isMatch(type, o);
        if (match) {
          return (T) o;
        }
      }
    }
    throw CrowException.exception("无可匹配类型，{}", type.getTypeName());
  }

  /**
   * 筛选结果类型 在一个未知的结果集中匹配指定类型的项，得到所有可能匹配的结果<br>
   * 无匹配选项则返回空集合
   *
   * @param l    结果集
   * @param type 检索的类型
   * @return T
   * @author liuwenhao
   * @date 2022/4/22 19:33
   */
  public static <T> List<T> screenTypes(Iterable<?> l, Type type) {
    List<T> tList = new ArrayList<>();
    for (Object o : l) {
      boolean match = isMatch(type, o);
      if (match) {
        tList.add((T) o);
      }
    }
    return tList;
  }

  /**
   * 根据类型填充属性 按照类型匹配的方式将结果集中的数据填充到实例对象中，对于相同类型的数据采用按顺序依次填充的策略<br>
   * 按对象的填充方式，在填充时不会影响到已有的数据在填充之前需要使用到反射获取属性类型<br>
   *
   * @param l 结果集
   * @param t 需要填充的类对象
   * @author liuwenhao
   * @date 2022/4/26 9:02
   */
  public static <T> void fillInstance(Iterable<?> l, T t) {
    fillInstance(l, t, false);
  }

  /**
   * 根据类型填充属性 按照类型匹配的方式将结果集中的数据填充到实例对象中，对于相同类型的数据采用按顺序依次填充的策略<br>
   * 按对象的填充方式，在填充时不会影响到已有的数据在填充之前需要使用到反射获取属性类型<br>
   *
   * @param l       结果集
   * @param t       需要填充的类对象
   * @param isCover 是否覆盖
   * @author liuwenhao
   * @date 2022/4/26 9:02
   */
  public static <T> void fillInstance(Iterable<?> l, T t, boolean isCover) {
    Class<?> cls = t.getClass();
    List<Field> fields = ClassUtil.getFieldsByGetterAndSetter(cls);

    for (Object o : l) {
      TypeMatching typeMatching = new TypeMatching(fields, o, cls, isCover);
      Field matching = typeMatching.matching(t);
      if (matching != null) {
        fields.remove(matching);
      }
    }
  }

  /**
   * 根据类型填充属性 批量处理的形式，针对单个对象的处理和{@link #fillInstance(Iterable, Object, boolean)}相同
   *
   * @param l       结果集
   * @param ts      需要填充的类对象集合
   * @param isCover 是否覆盖
   * @return java.util.Collection<T>
   * @author liuwenhao
   * @date 2022/6/7 13:26
   */
  public static <T> Collection<T> fillCollection(Iterable<?> l, Collection<T> ts, boolean isCover) {
    T element = ContainerUtil.getFirstElement(ts);
    if (Objects.isNull(element)) {
      return ts;
    }
    Class<?> cls = element.getClass();
    List<Field> fields = ClassUtil.getFieldsByGetterAndSetter(cls);

    for (Object o : l) {
      TypeMatching typeMatching = new TypeMatchingBatch<>(fields, o, cls, isCover, ts);
      Field matching = typeMatching.matching(element);
      if (matching != null) {
        fields.remove(matching);
      }
    }
    return ts;
  }

  /**
   * 根据类型填充属性 批量处理的形式，针对单个对象的处理和{@link #fillInstance(Iterable, Object, boolean)}相同
   *
   * @param l  结果集
   * @param ts 需要填充的类对象集合
   * @return java.util.Collection<T>
   * @author liuwenhao
   * @date 2022/6/7 13:26
   */
  public static <T> Collection<T> fillCollection(Iterable<?> l, Collection<T> ts) {
    return fillCollection(l, ts, false);
  }

  /**
   * 根据类型填充属性 按照类型匹配的方式将结果集中的数据填充到实例对象中，对于相同类型的数据采用按顺序依次填充的策略<br>
   * 这需要 reflections 的支持
   *
   * @param l     结果集
   * @param clazz 需要填充的类
   * @author liuwenhao
   * @date 2022/4/26 9:02
   */
  public static <T> T fillClass(Iterable<?> l, Class<T> clazz) {
    return fillClass(l, clazz, false);
  }

  /**
   * 根据类型填充属性 按照类型匹配的方式将结果集中的数据填充到实例对象中，对于相同类型的数据采用按顺序依次填充的策略<br>
   * 这需要 reflections 的支持
   *
   * @param l       结果集
   * @param clazz   需要填充的类
   * @param isCover 是否覆盖
   * @author liuwenhao
   * @date 2022/4/26 9:02
   */
  public static <T> T fillClass(Iterable<?> l, Class<T> clazz, boolean isCover) {
    T t = ClassUtil.newInstance(clazz);
    fillInstance(l, t, isCover);
    return t;
  }

  /**
   * 验证类型是否匹配
   *
   * @param type 类型
   * @param o    对象
   * @return boolean
   * @author liuwenhao
   * @date 2022/4/26 11:24
   */
  private static boolean isMatch(Type type, Object o) {
    try {
      JsonUtil.objToString(o, type);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 对比字段类型和对象类型
   *
   * @author Create by liuwenhao on 2022/4/22 19:25
   */
  static class TypeMatching {

    /**
     * 字段集
     */
    List<Field> fields;

    /**
     * 实例对象
     */
    Object o;

    /**
     * 实例类型
     */
    Class<?> cls;

    /**
     * 是否覆盖
     */
    boolean isCover;

    public TypeMatching(List<Field> fields, Object o, Class<?> cls, boolean isCover) {
      this.fields = fields;
      this.o = o;
      this.cls = cls;
      this.isCover = isCover;
    }

    public TypeMatching(List<Field> fields, Object o, Class<?> cls) {
      this(fields, o, cls, false);
    }

    /**
     * 在fields中寻找和object相对应的类型 匹配成功后将数据加入到obj中<br>
     * 通过PropertyDescriptor完成匹配和填充操作
     *
     * @param obj 需要填充的对象
     * @return 返回匹配到的字段，如果未匹配则返回null
     * @author liuwenhao
     * @date 2022/4/26 11:00
     */
    public Field matching(Object obj) {
      try {
        for (Field field : fields) {
          String property = field.getName();
          PropertyDescriptor descriptor = new PropertyDescriptor(property, cls);
          Method readMethod = descriptor.getReadMethod();
          Object result = readMethod.invoke(obj);
          if ((isCover || Objects.isNull(result)) && isAccordWith(field, o)) {
            Method writeMethod = descriptor.getWriteMethod();
            writeMethod.invoke(obj, o);
            return field;
          }
        }
        return null;
      } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
        throw CrowException.exception(e);
      }
    }
  }

  /**
   * 对比字段类型和对象类型，批量处理
   *
   * @author Create by liuwenhao on 2022/4/22 19:25
   */
  static class TypeMatchingBatch<T> extends TypeMatching {

    /**
     * 需要填充的类对象集合
     */
    Collection<T> collection;

    public TypeMatchingBatch(
        List<Field> fields,
        Object o,
        Class<?> cls,
        boolean isCover,
        Collection<T> collection) {
      super(fields, o, cls, isCover);
      this.collection = collection;
    }

    public TypeMatchingBatch(
        List<Field> fields, Object o, Class<?> cls, Collection<T> collection) {
      super(fields, o, cls);
      this.collection = collection;
    }

    /**
     * 在fields中寻找和object相对应的类型 匹配成功后将数据加入到obj中<br>
     * 通过PropertyDescriptor完成匹配和填充操作
     *
     * @param obj 需要填充的对象
     * @return 返回匹配到的字段，如果未匹配则返回null
     * @author liuwenhao
     * @date 2022/4/26 11:00
     */
    @Override
    public Field matching(Object obj) {
      try {
        for (Field field : fields) {
          String property = field.getName();
          PropertyDescriptor descriptor = new PropertyDescriptor(property, cls);
          Method readMethod = descriptor.getReadMethod();
          Object result = readMethod.invoke(obj);
          if ((isCover || Objects.isNull(result)) && isAccordWith(field, o)) {
            for (T t : collection) {
              Method writeMethod = descriptor.getWriteMethod();
              writeMethod.invoke(t, o);
            }
            return field;
          }
        }
        return null;
      } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
        throw CrowException.exception(e);
      }
    }
  }

  /**
   * 验证字段与实例对象是否可以兼容
   *
   * @param field 字段属性
   * @param o     实例对象
   * @return boolean
   * @author liuwenhao
   * @date 2022/4/27 9:38
   */
  private static boolean isAccordWith(Field field, Object o) {
    Type type = field.getGenericType();
    Compress compress = CompressHelper.getType(type, o);
    return compress.check();
  }
}
