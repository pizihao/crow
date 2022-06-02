package com.deep.crow.headbe;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;

/**
 * <h2>简单类型，无泛型</h2>
 *
 * @author Create by liuwenhao on 2022/6/2 14:29
 */
@SuppressWarnings("unchecked")
public class SimpleType extends AbstractNestedType {

    public SimpleType(Object o, Type type, ObjectMapper objectMapper) {
        super(o, type, objectMapper);
    }

    @Override
    public <T> T split() {
        return (T) o;
    }

    @Override
    public boolean check() {
        return ((Class<?>) type).isInstance(split());
    }

}