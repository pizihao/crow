package com.deep.crow.compress;

import com.deep.crow.type.CrowTypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;

/**
 * <h2>默认的检验器，存在泛型</h2>
 *
 * @author Create by liuwenhao on 2022/6/2 15:38
 */
@SuppressWarnings("unchecked")
public class DefaultCompress extends AbstractCompress {

    public DefaultCompress(Object o, Type type, ObjectMapper objectMapper) {
        super(o, type, objectMapper);
    }

    @Override
    public <T> T compress() {
        return (T) o;
    }

    @Override
    public boolean check() {
        try {
            CrowTypeReference<?> typeReference = CrowTypeReference.make(type);
            objectMapper.convertValue(compress(), typeReference);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}