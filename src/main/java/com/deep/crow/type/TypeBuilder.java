package com.deep.crow.type;


import com.deep.crow.exception.CrowException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <h2>类型构建器</h2>
 * 使用更简易的方式创建复杂的泛型 <br>
 * <a href="https://github.com/ikidou/TypeBuilder">TypeBuilder</a>
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