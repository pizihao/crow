package com.deep.crow.util;

import com.deep.crow.exception.CrowException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作类对象的相关工具类
 */
public class ClassUtil {

  static List<Class<?>> simple = new LinkedList<>();

  static {
    simple.add(Integer.class);
    simple.add(Float.class);
    simple.add(Character.class);
    simple.add(Long.class);
    simple.add(Double.class);
    simple.add(Short.class);
    simple.add(Boolean.class);
    simple.add(Byte.class);
  }

  private ClassUtil() {

  }

  /**
   * 判断类是否是基本数据类型或是其包装类型
   *
   * @param cls 要判断的类型
   * @return 是否是基本数据类型
   */
  public static boolean isPrimitive(Class<?> cls) {
    return cls.isPrimitive() || simple.contains(cls);
  }

  /**
   * 获取类中带有get方和set方法的属性
   *
   * @param cls 类型
   * @return 属性集合
   */
  public static List<Field> getFieldsByGetterAndSetter(Class<?> cls) {
    List<Field> fields = new LinkedList<>();
    List<Field> allField = getAllField(cls);
    for (Field f : allField) {
      try {
        new PropertyDescriptor(f.getName(), cls);
        fields.add(f);
      } catch (Exception e) {
        // non
      }
    }
    return fields;
  }

  /**
   * 获取类中所有的属性，包括自身的和从父类继承的，还有各种权限的，但不包括静态的
   *
   * @param cls 类
   * @return 属性集 合
   */
  public static List<Field> getAllField(Class<?> cls) {
    List<Field> allFields = new LinkedList<>();
    Class<?> searchType = cls;
    while (searchType != null) {
      Field[] fields = searchType.getDeclaredFields();
      allFields.addAll(Arrays.asList(fields));
      searchType = searchType.getSuperclass();
    }
    return allFields.stream()
        .filter(f -> !Modifier.isStatic(f.getModifiers()))
        .collect(Collectors.toList());
  }

  public static <T> T newInstance(Class<T> cls) {
    try {
      return cls.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw CrowException.exception(e);
    }
  }
}
