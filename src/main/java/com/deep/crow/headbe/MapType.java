package com.deep.crow.headbe;

import com.deep.crow.type.CrowTypeReference;
import com.deep.crow.util.ContainerUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * <h2>映射集合压缩器</h2>
 *
 * @author Create by liuwenhao on 2022/6/2 15:48
 */
@SuppressWarnings("unchecked")
public class MapType extends AbstractNestedType {

    public MapType(Object o, Type type, ObjectMapper objectMapper) {
        super(o, type, objectMapper);
    }

    @Override
    public <T> T split() {
        Map<?, ?> map = (Map<?, ?>) o;
        return (T) ContainerUtil.getFirstKeyValue(map);
    }

    @Override
    public boolean check() {
        Map.Entry<?, ?> firstKeyValue = split();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type keyArgument = parameterizedType.getActualTypeArguments()[0];
        Type valueArgument = parameterizedType.getActualTypeArguments()[1];
        CrowTypeReference<?> keyTypeReference = CrowTypeReference.make(keyArgument);
        CrowTypeReference<?> valueTypeReference = CrowTypeReference.make(valueArgument);
        objectMapper.convertValue(firstKeyValue.getKey(), keyTypeReference);
        objectMapper.convertValue(firstKeyValue.getValue(), valueTypeReference);
        return false;
    }
}