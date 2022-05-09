package com.deep.crow.util;

import com.deep.crow.exception.CrowException;
import com.deep.crow.jackson.ObjectMapperFactory;
import com.deep.crow.type.ParameterizedTypeImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reflections.ReflectionUtils;

import javax.annotation.Nullable;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * <h2>类型填充工具类</h2>
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
    public static <T> T screenClass(Iterable<?> l, Class<T> clazz) {
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
     * 无匹配选项则抛出异常，在填充时会忽略带有单个String泛型的类型
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
         *      其真实类型是未知的，所以并不支持这种获取方式。
         * 2，通过序列化的方式，先通过参数指明类型进行序列化
         *      如果成功，则说明类型兼容，反之不兼容
         *      这需要 Jackson 的支持
         */

        if (type instanceof ParameterizedType) {
            Type[] arguments = ((ParameterizedType) type).getActualTypeArguments();
            boolean sign = false;
            for (Type argument : arguments) {
                sign = argument != String.class.getGenericSuperclass();
            }
            if (sign) {
                // 不希望带有全是String类型的泛型
                CrowTypeReference<T> typeReference = CrowTypeReference.make(type);
                ObjectMapper objectMapper = ObjectMapperFactory.get();
                for (Object o : l) {
                    boolean match = isMatch(typeReference, o, objectMapper);
                    if (match) {
                        return (T) o;
                    }
                }

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
            boolean match = isMatch(typeReference, o, objectMapper);
            if (match) {
                tList.add((T) o);
            }
        }
        return tList;
    }

    /**
     * <h2>根据类型填充属性</h2>
     * 按照类型匹配的方式将结果集中的数据填充到实例对象中，对于相同类型的数据采用按顺序依次填充的策略<br>
     * 按对象的填充方式，在填充时不会影响到已有的数据在填充之前需要使用到反射获取属性类型<br>
     * 这需要 reflections 的支持
     *
     * @param l 结果集
     * @param t 需要填充的类对象
     * @author liuwenhao
     * @date 2022/4/26 9:02
     */
    public static <T> void fillInstance(Iterable<?> l, T t) {
        fillInstance(l, t, false);
    }

    /**
     * <h2>根据类型填充属性</h2>
     * 按照类型匹配的方式将结果集中的数据填充到实例对象中，对于相同类型的数据采用按顺序依次填充的策略<br>
     * 按对象的填充方式，在填充时不会影响到已有的数据在填充之前需要使用到反射获取属性类型<br>
     * 这需要 reflections 的支持，并且在填充时会忽略带有单个String泛型的类型
     *
     * @param l       结果集
     * @param t       需要填充的类对象
     * @param isCover 是否覆盖
     * @author liuwenhao
     * @date 2022/4/26 9:02
     */
    public static <T> void fillInstance(Iterable<?> l, T t, boolean isCover) {
        Set<Field> fields = ReflectionUtils.getAllFields(t.getClass());
        try {
            for (Object o : l) {
                TypeMatching typeMatching = new TypeMatching(fields, o, isCover);
                Field matching = typeMatching.matching(t);
                if (matching != null) {
                    fields.remove(matching);
                }
            }
        } catch (IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            throw new CrowException(e);
        }
    }

    /**
     * <h2>根据类型填充属性</h2>
     * 按照类型匹配的方式将结果集中的数据填充到实例对象中，对于相同类型的数据采用按顺序依次填充的策略<br>
     * 这需要 reflections 的支持，并且在填充时会忽略带有单个String泛型的类型
     *
     * @param l     结果集
     * @param clazz 需要填充的类
     * @author liuwenhao
     * @date 2022/4/26 9:02
     */
    public static <T> T fillClass(Iterable<?> l, Class<T> clazz) throws InstantiationException, IllegalAccessException {
        return fillClass(l, clazz, false);
    }

    /**
     * <h2>根据类型填充属性</h2>
     * 按照类型匹配的方式将结果集中的数据填充到实例对象中，对于相同类型的数据采用按顺序依次填充的策略<br>
     * 这需要 reflections 的支持，并且在填充时会忽略带有单个String泛型的类型
     *
     * @param l       结果集
     * @param clazz   需要填充的类
     * @param isCover 是否覆盖
     * @author liuwenhao
     * @date 2022/4/26 9:02
     */
    public static <T> T fillClass(Iterable<?> l, Class<T> clazz, boolean isCover) throws InstantiationException, IllegalAccessException {
        T instance = clazz.newInstance();
        fillInstance(l, instance, isCover);
        return instance;
    }

    /**
     * <h2>验证类型是否匹配</h2>
     *
     * @param typeReference 类型
     * @param o             对象
     * @param objectMapper  objectMapper
     * @return boolean
     * @author liuwenhao
     * @date 2022/4/26 11:24
     */
    private static boolean isMatch(CrowTypeReference<?> typeReference, Object o, ObjectMapper objectMapper) {
        try {
            objectMapper.convertValue(o, typeReference);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 对比字段类型和对象类型
     *
     * @author Create by liuwenhao on 2022/4/22 19:25
     */
    static class TypeMatching {

        /**
         * 字段集
         */
        Set<Field> fields;

        /**
         * 实例对象
         */
        Object o;

        /**
         * 是否覆盖
         */
        boolean isCover;

        public TypeMatching(Set<Field> fields, Object o, boolean isCover) {
            this.fields = fields;
            this.o = o;
            this.isCover = isCover;
        }

        public TypeMatching(Set<Field> fields, Object o) {
            this(fields, o, false);
        }

        /**
         * <h2>在fields中寻找和object相对应的类型</h2>
         * 匹配成功后将数据加入到obj中<br>
         * 通过PropertyDescriptor完成匹配和填充操作
         *
         * @param obj 需要填充的对象
         * @return 返回匹配到的字段，如果未匹配则返回null
         * @author liuwenhao
         * @date 2022/4/26 11:00
         */
        @Nullable
        public Field matching(Object obj) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
            ObjectMapper objectMapper = ObjectMapperFactory.get();
            for (Field field : fields) {
                if (!isString(field)) {
                    continue;
                }
                String property = field.getName();
                PropertyDescriptor descriptor = new PropertyDescriptor(property, obj.getClass());
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(obj);
                if (isCover || Objects.isNull(result)) {
                    boolean sign = isAccordWith(field, objectMapper);
                    if (sign) {
                        Method writeMethod = descriptor.getWriteMethod();
                        writeMethod.invoke(obj, o);
                        return field;
                    }
                }
                // 如果obj中本来就存在数据，那么不予处理
            }
            return null;
        }

        /**
         * <h2>验证字段与实例对象是否可以兼容</h2>
         *
         * @param field        字段属性
         * @param objectMapper ObjectMapper
         * @return boolean
         * @author liuwenhao
         * @date 2022/4/27 9:38
         */
        private boolean isAccordWith(Field field, ObjectMapper objectMapper) {
            Type type = field.getGenericType();
            if (type instanceof ParameterizedType) {
                // 参数化类型的情况
                ParameterizedType parameterizedType = (ParameterizedType) type;
                CrowTypeReference<?> typeReference = CrowTypeReference.make(parameterizedType);
                return isMatch(typeReference, o, objectMapper);
            } else {
                // 普通类型的情况
                return field.getType().isInstance(o);
            }
        }

        /**
         * <h2>验证是否存在String类型</h2>
         *
         * @param field 字段属性
         * @return boolean
         * @author liuwenhao
         * @date 2022/4/28 10:46
         */
        private boolean isString(Field field) {
            Objects.requireNonNull(field);
            Type type = field.getGenericType();
            if (type instanceof ParameterizedType) {
                Type[] arguments = ((ParameterizedType) type).getActualTypeArguments();
                for (Type argument : arguments) {
                    if (argument != String.class.getGenericSuperclass()) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }

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