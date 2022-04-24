package com.deep.crow.type;


import com.deep.crow.exception.CrowException;
import com.deep.crow.multi.Multi;

import java.lang.reflect.Type;
import java.util.*;

/**
 * <h2>类型构建器</h2>
 * 使用更简易的方式创建复杂的泛型 <br>
 * <a href="https://github.com/ikidou/TypeBuilder">TypeBuilder</a>
 *
 * @author Create by liuwenhao on 2022/4/24 14:28
 */
@SuppressWarnings("unused")
public class TypeBuilder {

    /**
     * 维持嵌套关系
     */
    private final TypeBuilder builder;
    private final Class<?> raw;
    private final List<Type> args = new ArrayList<>();

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
     * <h2>快速构造一个List</h2>
     *
     * @param raw 泛型类型
     * @return java.lang.reflect.Type
     * @author liuwenhao
     * @date 2022/4/24 17:28
     */
    public static Type list(Class<?> raw) {
        return TypeBuilder.make(List.class)
            .add(raw)
            .build();
    }

    /**
     * <h2>快速构造一个Queue</h2>
     *
     * @param raw 泛型类型
     * @return java.lang.reflect.Type
     * @author liuwenhao
     * @date 2022/4/24 17:28
     */
    public static Type queue(Class<?> raw) {
        return TypeBuilder.make(Queue.class)
            .add(raw)
            .build();
    }

    /**
     * <h2>快速构造一个Set</h2>
     *
     * @param raw 泛型类型
     * @return java.lang.reflect.Type
     * @author liuwenhao
     * @date 2022/4/24 17:28
     */
    public static Type set(Class<?> raw) {
        return TypeBuilder.make(Set.class)
            .add(raw)
            .build();
    }

    /**
     * <h2>快速构造一个ThreadLocal</h2>
     *
     * @param raw 泛型类型
     * @return java.lang.reflect.Type
     * @author liuwenhao
     * @date 2022/4/24 17:28
     */
    public static Type threadLocal(Class<?> raw) {
        return TypeBuilder.make(ThreadLocal.class)
            .add(raw)
            .build();
    }

    /**
     * <h2>快速构造一个Iterator</h2>
     *
     * @param raw 泛型类型
     * @return java.lang.reflect.Type
     * @author liuwenhao
     * @date 2022/4/24 17:28
     */
    public static Type iterator(Class<?> raw) {
        return TypeBuilder.make(Iterator.class)
            .add(raw)
            .build();
    }

    /**
     * <h2>快速构造一个Collection</h2>
     *
     * @param raw 泛型类型
     * @return java.lang.reflect.Type
     * @author liuwenhao
     * @date 2022/4/24 17:28
     */
    public static Type collection(Class<?> raw) {
        return TypeBuilder.make(Collection.class)
            .add(raw)
            .build();
    }

    /**
     * <h2>快速构造一个Map</h2>
     *
     * @param key   key类型
     * @param value value类型
     * @return java.lang.reflect.Type
     * @author liuwenhao
     * @date 2022/4/24 17:28
     */
    public static Type map(Class<?> key, Class<?> value) {
        return TypeBuilder.make(Map.class)
            .add(key)
            .add(value)
            .build();
    }

    /**
     * <h2>快速构造一个Multi</h2>
     *
     * @param raw 泛型类型
     * @return java.lang.reflect.Type
     * @author liuwenhao
     * @date 2022/4/24 17:28
     */
    public static Type multi(Class<?> raw) {
        return TypeBuilder.make(Multi.class)
            .add(raw)
            .build();
    }

    // =====================================impl=======================================

    public TypeBuilder nested(Class<?> raw) {
        return make(raw, this);
    }

    public TypeBuilder parent() {
        Objects.requireNonNull(builder);
        builder.add(getType());
        return builder;
    }

    public TypeBuilder add(Class<?> clazz) {
        return add((Type) clazz);
    }

    public TypeBuilder addExtends(Class<?>... classes) {
        Objects.requireNonNull(classes);
        WildcardTypeImpl wildcardType = WildcardTypeImpl.make(null, classes);
        return add(wildcardType);
    }

    public TypeBuilder addSuper(Class<?>... classes) {
        Objects.requireNonNull(classes);
        WildcardTypeImpl wildcardType = WildcardTypeImpl.make(classes, null);
        return add(wildcardType);
    }

    public TypeBuilder add(Type type) {
        Objects.requireNonNull(type);
        args.add(type);
        return this;
    }

    public Type build() {
        if (builder != null) {
            throw new CrowException("嵌套情况下请先调用 endNested()");
        }
        return getType();
    }

    private Type getType() {
        if (args.isEmpty()) {
            return raw;
        }
        return ParameterizedTypeImpl.make(raw, args.toArray(new Type[0]), null);
    }
}