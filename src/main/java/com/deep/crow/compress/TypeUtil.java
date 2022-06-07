package com.deep.crow.compress;

import com.deep.crow.exception.CrowException;
import com.deep.crow.jackson.ObjectMapperFactory;
import com.deep.crow.type.CrowTypeReference;
import com.deep.crow.type.ParameterizedTypeImpl;
import com.deep.crow.util.ContainerUtil;
import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.FieldAccess;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

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
     * 无匹配选项则获得空集合
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
     * 无匹配选项则抛出异常
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
            CrowTypeReference<T> typeReference = CrowTypeReference.make(type);
            ObjectMapper objectMapper = ObjectMapperFactory.get();
            for (Object o : l) {
                boolean match = isMatch(typeReference, o, objectMapper);
                if (match) {
                    return (T) o;
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
     *
     * @param l       结果集
     * @param t       需要填充的类对象
     * @param isCover 是否覆盖
     * @author liuwenhao
     * @date 2022/4/26 9:02
     */
    public static <T> void fillInstance(Iterable<?> l, T t, boolean isCover) {
        FieldAccess fieldAccess = FieldAccess.get(t.getClass());
        List<Field> fields = new ArrayList<>(Arrays.asList(fieldAccess.getFields()));

        for (Object o : l) {
            TypeMatching typeMatching = new TypeMatching(fields, o, fieldAccess, isCover);
            Field matching = typeMatching.matching(t);
            if (matching != null) {
                fields.remove(matching);
            }
        }
    }

    /**
     * <h2>根据类型填充属性</h2>
     * 批量处理的形式，针对单个对象的处理和#{@link #fillInstance(Iterable, Object, boolean)}相同
     *
     * @param l       结果集
     * @param ts      需要填充的类对象集合
     * @param isCover 是否覆盖
     * @return java.util.Collection<T>
     * @author liuwenhao
     * @date 2022/6/7 13:26
     */
    public static <T> Collection<T> fillCollection(Iterable<?> l, Collection<T> ts, boolean isCover) {
        T element = ContainerUtil.getFirstElement(ts);
        if (Objects.isNull(element)) {
            return ts;
        }
        FieldAccess fieldAccess = FieldAccess.get(element.getClass());
        List<Field> fields = new ArrayList<>(Arrays.asList(fieldAccess.getFields()));

        for (Object o : l) {
            TypeMatching typeMatching = new TypeMatchingBatch<>(fields, o, fieldAccess, isCover, ts);
            Field matching = typeMatching.matching(element);
            if (matching != null) {
                fields.remove(matching);
            }
        }
        return ts;
    }

    /**
     * <h2>根据类型填充属性</h2>
     * 批量处理的形式，针对单个对象的处理和#{@link #fillInstance(Iterable, Object, boolean)}相同
     *
     * @param l  结果集
     * @param ts 需要填充的类对象集合
     * @return java.util.Collection<T>
     * @author liuwenhao
     * @date 2022/6/7 13:26
     */
    public static <T> Collection<T> fillCollection(Iterable<?> l, Collection<T> ts) {
        return fillCollection(l, ts, false);
    }


    /**
     * <h2>根据类型填充属性</h2>
     * 按照类型匹配的方式将结果集中的数据填充到实例对象中，对于相同类型的数据采用按顺序依次填充的策略<br>
     * 这需要 reflections 的支持
     *
     * @param l     结果集
     * @param clazz 需要填充的类
     * @author liuwenhao
     * @date 2022/4/26 9:02
     */
    public static <T> T fillClass(Iterable<?> l, Class<T> clazz) {
        return fillClass(l, clazz, false);
    }

    /**
     * <h2>根据类型填充属性</h2>
     * 按照类型匹配的方式将结果集中的数据填充到实例对象中，对于相同类型的数据采用按顺序依次填充的策略<br>
     * 这需要 reflections 的支持
     *
     * @param l       结果集
     * @param clazz   需要填充的类
     * @param isCover 是否覆盖
     * @author liuwenhao
     * @date 2022/4/26 9:02
     */
    public static <T> T fillClass(Iterable<?> l, Class<T> clazz, boolean isCover) {
        ConstructorAccess<T> constructorAccess = ConstructorAccess.get(clazz);
        T t = constructorAccess.newInstance();
        fillInstance(l, t, isCover);
        return t;
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
        List<Field> fields;

        /**
         * 实例对象
         */
        Object o;

        /**
         * 操作实例字段
         */
        FieldAccess fieldAccess;

        /**
         * 是否覆盖
         */
        boolean isCover;

        public TypeMatching(List<Field> fields, Object o,
                            FieldAccess fieldAccess, boolean isCover) {
            this.fields = fields;
            this.o = o;
            this.isCover = isCover;
            this.fieldAccess = fieldAccess;
        }

        public TypeMatching(List<Field> fields, Object o,
                            FieldAccess fieldAccess) {
            this(fields, o, fieldAccess, false);
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
        public Field matching(Object obj) {
            ObjectMapper objectMapper = ObjectMapperFactory.get();
            for (Field field : fields) {
                String property = field.getName();
                Object result = fieldAccess.get(obj, property);
                if ((isCover || Objects.isNull(result)) && isAccordWith(field, obj, objectMapper)) {
                    fieldAccess.set(obj, property, o);
                    return field;
                }
            }
            return null;
        }

    }


    /**
     * 对比字段类型和对象类型，批量处理
     *
     * @author Create by liuwenhao on 2022/4/22 19:25
     */
    static class TypeMatchingBatch<T> extends TypeMatching {

        /**
         * 需要填充的类对象集合
         */
        Collection<T> collection;

        public TypeMatchingBatch(List<Field> fields, Object o, FieldAccess fieldAccess, boolean isCover, Collection<T> collection) {
            super(fields, o, fieldAccess, isCover);
            this.collection = collection;
        }

        public TypeMatchingBatch(List<Field> fields, Object o, FieldAccess fieldAccess, Collection<T> collection) {
            super(fields, o, fieldAccess);
            this.collection = collection;
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
        @Override
        public Field matching(Object obj) {
            ObjectMapper objectMapper = ObjectMapperFactory.get();
            for (Field field : fields) {
                String property = field.getName();
                Object result = fieldAccess.get(obj, property);
                if ((isCover || Objects.isNull(result)) && isAccordWith(field, o, objectMapper)) {
                    long l = System.currentTimeMillis();
                    collection.forEach(t -> fieldAccess.set(t, property, o));
                    long t = System.currentTimeMillis();
                    System.out.println(t - l);
                    return field;
                }
            }
            return null;
        }


    }

    /**
     * <h2>验证字段与实例对象是否可以兼容</h2>
     *
     * @param field        字段属性
     * @param o            实例对象
     * @param objectMapper ObjectMapper
     * @return boolean
     * @author liuwenhao
     * @date 2022/4/27 9:38
     */
    private static boolean isAccordWith(Field field, Object o, ObjectMapper objectMapper) {
        Type type = field.getType();
        Compress compress = CompressHelper.getType(type, o, objectMapper);
        return compress.check();
    }

}