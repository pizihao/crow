package com.deep.crow.headbe;


import com.deep.crow.type.CrowTypeReference;
import com.deep.crow.util.ContainerUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <h2>迭代器类型</h2>
 *
 * @author Create by liuwenhao on 2022/6/2 15:28
 */
@SuppressWarnings("unchecked")
public class IteratorType extends AbstractNestedType {

    public IteratorType(Object o, Type type, ObjectMapper objectMapper) {
        super(o, type, objectMapper);
    }

    @Override
    public <T> T split() {
        return type instanceof ParameterizedType ? (T) ContainerUtil.getFirstElement((Iterable<?>) o) : null;
    }

    @Override
    public boolean check() {
        try {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type argument = parameterizedType.getActualTypeArguments()[0];
            Type rawType = parameterizedType.getRawType();
            CrowTypeReference<?> typeReference = CrowTypeReference.make(argument);
            objectMapper.convertValue(split(), typeReference);
            return o.getClass().isAssignableFrom((Class<?>) rawType);
        } catch (Exception e) {
            return false;
        }
    }
}