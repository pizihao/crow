package com.deep.crow.headbe;

import com.deep.crow.exception.CrowException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h2>类型压缩器辅助</h2>
 *
 * @author Create by liuwenhao on 2022/6/2 16:09
 */
@SuppressWarnings("unused")
public class NestedTypeHelper {

    /*
     * 类型和压缩器的映射关系
     */
    private static final Map<Class<?>, Class<? extends NestedType>> nestedTypeMap = new ConcurrentHashMap<>();

    static {
        nestedTypeMap.put(Iterable.class, IteratorType.class);
        nestedTypeMap.put(Map.class, MapType.class);
    }

    /**
     * <h2>通过类型获取一组压缩器</h2>
     *
     * @param type         类型
     * @param o            需要判断的对象
     * @param objectMapper objectMapper
     * @return com.deep.crow.headbe.NestedType
     * @author liuwenhao
     * @date 2022/6/2 16:48
     */
    public static NestedType getType(Type type, Object o, ObjectMapper objectMapper) {
        return (type instanceof ParameterizedType)
            ? get(o, type, objectMapper)
            : new SimpleType(o, type, objectMapper);
    }

    /**
     * <h2>通过类型获取一组压缩器</h2>
     *
     * @param type         类型
     * @param o            需要判断的对象
     * @param objectMapper objectMapper
     * @return com.deep.crow.headbe.NestedType
     * @author liuwenhao
     * @date 2022/6/2 17:26
     */
    private static NestedType get(Object o, Type type, ObjectMapper objectMapper) {
        for (Map.Entry<Class<?>, Class<? extends NestedType>> entry : nestedTypeMap.entrySet()) {
            if (entry.getKey().isInstance(o)) {
                Class<? extends NestedType> aClass = nestedTypeMap.get(entry.getKey());
                try {
                    Constructor<? extends NestedType> constructor = aClass.getConstructor(Object.class, Type.class, ObjectMapper.class);
                    return constructor.newInstance(o, type, objectMapper);
                } catch (Exception e) {
                    throw CrowException.exception(e);
                }
            }
        }
        return new DefaultType(o, type, objectMapper);
    }

    /**
     * <h2>添加一组压缩器映射</h2>
     * 高级别的抽象会覆盖低级别的抽象<br>
     * 所以注册的压缩器不一定会生效
     *
     * @param key   键
     * @param value 值
     * @author liuwenhao
     * @date 2022/6/2 16:17
     */
    public synchronized void register(Class<?> key, Class<? extends NestedType> value) {
        int flag = -1;
        int removeFlag = -1;
        Class<?> aClass = null;
        for (Class<?> cls : nestedTypeMap.keySet()) {
            if (!cls.isAssignableFrom(key)) {
                flag = flag + 1;
                if (key.isAssignableFrom(cls)) {
                    removeFlag = removeFlag + 1;
                    aClass = cls;
                }
            }
        }
        if (flag == nestedTypeMap.size()){
            nestedTypeMap.put(key, value);
        }
        if (removeFlag > 0){
            nestedTypeMap.remove(aClass);
        }

    }

    /**
     * <h2>获取所有的值</h2>
     *
     * @return java.util.Map<java.lang.Class<?>,java.lang.Class<? extends com.deep.crow.headbe.NestedType>>
     * @author liuwenhao
     * @date 2022/6/2 18:43
     */
    public Map<Class<?>, Class<? extends NestedType>> getMap(){
        return new HashMap<>(nestedTypeMap);
    }

}