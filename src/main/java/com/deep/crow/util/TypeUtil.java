package com.deep.crow.util;

import com.deep.crow.exception.CrowException;
import com.deep.crow.jackson.ObjectMapperFactory;
import com.deep.crow.type.ParameterizedTypeImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <h2>反射工具类</h2>
 *
 * @author Create by liuwenhao on 2022/4/22 19:25
 */
@SuppressWarnings("unchecked,unused")
public class TypeUtil {

    private TypeUtil() {
    }

    /**
     * <h2>筛选结果类型</h2>
     * 在一个未知的结果集中匹配指定类型的项，匹配成功则直接返回<br>
     * 无匹配选项则抛出异常
     *
     * @param l     结果集
     * @param clazz 目标类
     * @return T
     * @author liuwenhao
     * @date 2022/4/22 19:33
     */
    public static <T> T screenClass(Iterable<?> l, Class<?> clazz) {
        for (Object o : l) {
            boolean instance = clazz.isInstance(o);
            if (instance) {
                return (T) o;
            }
        }
        throw CrowException.exception("无可匹配类型，{}", clazz.getName());
    }

    /**
     * <h2>筛选结果类型</h2>
     * 在一个未知的结果集中匹配指定类型的项，得到所有可能匹配的结果<br>
     * 无匹配选项则抛出异常
     *
     * @param l     结果集
     * @param clazz 目标类
     * @return T
     * @author liuwenhao
     * @date 2022/4/22 19:33
     */
    public static <T> List<T> screenClasses(Iterable<?> l, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        for (Object o : l) {
            boolean instance = clazz.isInstance(o);
            if (instance) {
                list.add((T) o);
            }
        }
        return list;
    }

    /**
     * <h2>筛选结果类型</h2>
     * 在一个未知的结果集中匹配指定类型的项<br>
     * 无匹配选项则抛出异常
     *
     * @param l     结果集
     * @param clazz 目标类
     * @param types 泛型
     * @return T
     * @author liuwenhao
     * @date 2022/4/22 19:33
     */
    public static <T> T screenType(Iterable<?> l, Class<T> clazz, Type... types) {
        if (Objects.isNull(types)) {
            return screenClass(l, clazz);
        }
        // 需要获取的类型
        ParameterizedType parameterizedType = ParameterizedTypeImpl.make(clazz, types);
        return screenType(l, parameterizedType);
    }


    /**
     * <h2>筛选结果类型</h2>
     * 在一个未知的结果集中匹配指定类型的项，得到所有可能匹配的结果<br>
     * 无匹配选项则返回空集合
     *
     * @param l     结果集
     * @param clazz 目标类
     * @param types 泛型
     * @return T
     * @author liuwenhao
     * @date 2022/4/22 19:33
     */
    public static <T> List<T> screenTypes(Iterable<?> l, Class<?> clazz, Type... types) {
        // 需要获取的类型
        ParameterizedType parameterizedType = ParameterizedTypeImpl.make(clazz, types);
        return screenTypes(l, parameterizedType);
    }

    /**
     * <h2>筛选结果类型</h2>
     * 在一个未知的结果集中匹配指定类型的项<br>
     * 无匹配选项择抛出异常
     *
     * @param l    结果集
     * @param type 检索的类型
     * @return T
     * @author liuwenhao
     * @date 2022/4/24 16:27
     */
    public static <T> T screenType(Iterable<?> l, Type type) {
        /*
         * o及其泛型是否与parameterizedType兼容，
         * 1，通过API获取o被擦除的泛型类型，和传入的类型进行对比，相同则返回
         *      但是并没有找到对应的API，因为o的类型是Object，o如果存在泛型，那么也是Object，
         *      会被直接擦除，所以并不支持这种获取方式。
         * 2，通过序列化的方式，先通过参数指明类型进行序列化
         *      如果成功，则说明类型兼容，反之不兼容
         *      这需要借助 Jackson 的支持
         */
        CrowTypeReference<T> typeReference = CrowTypeReference.make(type);
        ObjectMapper objectMapper = ObjectMapperFactory.get();
        for (Object o : l) {
            try {
                String valueAsString = objectMapper.writeValueAsString(o);
                objectMapper.readValue(valueAsString, typeReference);
                return (T) o;
            } catch (IOException ignored) {
                // 不符合的数据
            }
        }
        throw CrowException.exception("无可匹配类型，{}", type.getTypeName());
    }

    /**
     * <h2>筛选结果类型</h2>
     * 在一个未知的结果集中匹配指定类型的项，得到所有可能匹配的结果<br>
     * 无匹配选项则返回空集合
     *
     * @param l    结果集
     * @param type 检索的类型
     * @return T
     * @author liuwenhao
     * @date 2022/4/22 19:33
     */
    public static <T> List<T> screenTypes(Iterable<?> l, Type type) {
        List<T> tList = new ArrayList<>();
        CrowTypeReference<T> typeReference = CrowTypeReference.make(type);
        ObjectMapper objectMapper = ObjectMapperFactory.get();
        for (Object o : l) {
            try {
                String valueAsString = objectMapper.writeValueAsString(o);
                objectMapper.readValue(valueAsString, typeReference);
                tList.add((T) o);
            } catch (IOException ignored) {
                // 不符合的数据
            }
        }
        return tList;
    }

    /**
     * <h2>推断结果类型</h2>
     * 在一个未知的结果集中匹配指定类型的项，匹配成功则直接返回<br>
     * 通过调用方接收的类型进行推断<br>
     * 无匹配选项则抛出异常
     *
     * @param l 结果集
     * @return T
     * @author liuwenhao
     * @date 2022/4/24 17:56
     */
    public static <T> T inferClass(Iterable<?> l) throws NoSuchMethodException {
        Method method = TypeUtil.class.getMethod("inferClass", Iterable.class);
        Type returnType = method.getGenericReturnType();
        System.out.println(returnType);
        return screenType(l, returnType);
    }

    /**
     * 协助泛型检索
     *
     * @author Create by liuwenhao on 2022/4/22 19:25
     */
    static class CrowTypeReference<T> extends TypeReference<T> {

        Type type;

        private CrowTypeReference(Type type) {
            super();
            this.type = type;
        }

        public static <T> CrowTypeReference<T> make(Type type) {
            return new CrowTypeReference<>(type);
        }

        @Override
        public Type getType() {
            return type;
        }
    }
}