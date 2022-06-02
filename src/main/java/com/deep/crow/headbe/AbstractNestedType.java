package com.deep.crow.headbe;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/6/2 16:19
 */
public abstract class AbstractNestedType implements NestedType {
    Object o;
    Type type;
    ObjectMapper objectMapper;

    public AbstractNestedType(Object o, Type type, ObjectMapper objectMapper) {
        this.o = o;
        this.type = type;
        this.objectMapper = objectMapper;
    }
}