package com.deep.crow.type;

import com.deep.crow.exception.CrowException;
import com.deep.crow.multi.Multi;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 类型构建器 使用更简易的方式创建复杂的泛型 <br>
 * <a href="https://github.com/ikidou/TypeBuilder">TypeBuilder</a>
 *
 * @author Create by liuwenhao on 2022/4/24 14:28
 */
@SuppressWarnings("unused")
public class TypeBuilder {

  /** 维持嵌套关系 */
  private final TypeBuilder builder;

  private final Class<?> raw;
  private final List<Type> args = new ArrayList<>();
  private Class<?> owner;

  private TypeBuilder(Class<?> raw, TypeBuilder builder) {
    Objects.requireNonNull(raw);
    this.raw = raw;
    this.builder = builder;
  }

  public static TypeBuilder make(Class<?> raw) {
    return new TypeBuilder(raw, null);
  }

  private static TypeBuilder make(Class<?> raw, TypeBuilder builder) {
    return new TypeBuilder(raw, builder);
  }

  // =====================================FastBuilder=======================================

  /**
   * 快速构造一个List
   *
   * @param raw 泛型类型
   * @return java.lang.reflect.Type
   * @author liuwenhao
   * @date 2022/4/24 17:28
   */
  public static Type list(Type raw) {
    return TypeBuilder.make(List.class).addArgs(raw).build();
  }

  /**
   * 快速构造一个Queue
   *
   * @param raw 泛型类型
   * @return java.lang.reflect.Type
   * @author liuwenhao
   * @date 2022/4/24 17:28
   */
  public static Type queue(Type raw) {
    return TypeBuilder.make(Queue.class).addArgs(raw).build();
  }

  /**
   * 快速构造一个Set
   *
   * @param raw 泛型类型
   * @return java.lang.reflect.Type
   * @author liuwenhao
   * @date 2022/4/24 17:28
   */
  public static Type set(Type raw) {
    return TypeBuilder.make(Set.class).addArgs(raw).build();
  }

  /**
   * 快速构造一个ThreadLocal
   *
   * @param raw 泛型类型
   * @return java.lang.reflect.Type
   * @author liuwenhao
   * @date 2022/4/24 17:28
   */
  public static Type threadLocal(Type raw) {
    return TypeBuilder.make(ThreadLocal.class).addArgs(raw).build();
  }

  /**
   * 快速构造一个Iterator
   *
   * @param raw 泛型类型
   * @return java.lang.reflect.Type
   * @author liuwenhao
   * @date 2022/4/24 17:28
   */
  public static Type iterator(Type raw) {
    return TypeBuilder.make(Iterator.class).addArgs(raw).build();
  }

  /**
   * 快速构造一个Collection
   *
   * @param raw 泛型类型
   * @return java.lang.reflect.Type
   * @author liuwenhao
   * @date 2022/4/24 17:28
   */
  public static Type collection(Type raw) {
    return TypeBuilder.make(Collection.class).addArgs(raw).build();
  }

  /**
   * 快速构造一个Map
   *
   * @param key key类型
   * @param value value类型
   * @return java.lang.reflect.Type
   * @author liuwenhao
   * @date 2022/4/24 17:28
   */
  public static Type map(Type key, Type value) {
    return TypeBuilder.make(Map.class).addArgs(key).addArgs(value).build();
  }

  /**
   * 快速构造一个Multi
   *
   * @param raw 泛型类型
   * @return java.lang.reflect.Type
   * @author liuwenhao
   * @date 2022/4/24 17:28
   */
  public static Type multi(Type raw) {
    return TypeBuilder.make(Multi.class).addArgs(raw).build();
  }

  // =====================================impl=======================================

  public TypeBuilder nested(Class<?> raw) {
    return make(raw, this);
  }

  public TypeBuilder parent() {
    Objects.requireNonNull(builder);
    builder.addArgs(getType());
    return builder;
  }

  public TypeBuilder addArgs(Class<?> clazz) {
    return addArgs((Type) clazz);
  }

  public TypeBuilder addExtends(Class<?>... classes) {
    Objects.requireNonNull(classes);
    WildcardTypeImpl wildcardType = new WildcardTypeImpl(null, classes);
    return addArgs(wildcardType);
  }

  public TypeBuilder addSuper(Class<?>... classes) {
    Objects.requireNonNull(classes);
    WildcardTypeImpl wildcardType = new WildcardTypeImpl(classes, null);
    return addArgs(wildcardType);
  }

  public TypeBuilder addArgs(Type type) {
    Objects.requireNonNull(type);
    args.add(type);
    return this;
  }

  public TypeBuilder owner(Class<?> owner) {
    Objects.requireNonNull(owner);
    this.owner = owner;
    return this;
  }

  public Type build() {
    if (builder != null) {
      throw new CrowException("嵌套情况下请先调用 parent()");
    }
    return getType();
  }

  private Type getType() {
    if (args.isEmpty()) {
      return raw;
    }
    return ParameterizedTypeImpl.make(raw, args.toArray(new Type[0]), owner);
  }
}
