package com.deep.crow.json.element;

import com.deep.crow.json.Mapper;
import com.deep.crow.type.ParameterizedTypeImpl;
import com.deep.crow.type.TypeBuilder;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类型元素，代表Json中可解析的元素类型
 */
public interface Element {

  /**
   * 是否是可解析的类型
   *
   * @param type 类型
   * @return 是否可以解析
   */
  boolean isSupport(Type type);

  /**
   * 序列化
   *
   * @param o   待序列化的对象
   * @param key key
   */
  Mapper serializer(Object o, String key, boolean isIndexKey);

  /**
   * 反序列化操作
   *
   * @param mapper 可反序列化的映射对象
   * @param type   反序列化的目标类型
   * @return 序列化后的对象
   */
  <T> T deserializer(Mapper mapper, Type type);

  default Class<?> getCls(Type type) {
    if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      Type ownerType = parameterizedType.getRawType();
      return (Class<?>) ownerType;
    }

    if (type instanceof TypeVariable) {
      TypeVariable<?> variable = (TypeVariable<?>) type;
      return (Class<?>) variable.getGenericDeclaration();
    }

    return (Class<?>) type;
  }

  /**
   * @param type  类的类型
   * @param field 类中字段的类型
   * @return 获取字段的实际类型
   */
  default Type getFieldType(Type type, Field field) {
    Type genericType = field.getGenericType();
    if (!(type instanceof ParameterizedType)) {
      return genericType;
    }

    // 获取类的泛型
    ParameterizedType parameterizedType = (ParameterizedType) type;
    Type[] arguments = parameterizedType.getActualTypeArguments();
    Class<?> cls = getCls(type);
    TypeVariable<? extends Class<?>>[] parameters = cls.getTypeParameters();
    List<String> typeNames = Arrays.stream(parameters).map(TypeVariable::getName).collect(Collectors.toList());
    // 获取字段的泛型
    // 如果字段本身是参数化类型
    if (genericType instanceof ParameterizedType) {
      List<Type> types = new LinkedList<>();
      ParameterizedType fieldParameterizedType = (ParameterizedType) genericType;
      Type[] fieldTypeArguments = fieldParameterizedType.getActualTypeArguments();
      Class<?> rawType = (Class<?>) fieldParameterizedType.getRawType();
      // fieldTypeArguments 的顺序就是字段中泛型的顺序
      for (Type typeArgument : fieldTypeArguments) {
        String typeName = typeArgument.getTypeName();
        if (!typeNames.contains(typeName)) {
          types.add(typeArgument);
        } else {
          int i = typeNames.indexOf(typeName);
          types.add(parameters[i]);
        }
      }
      return ParameterizedTypeImpl.make(rawType, types.toArray(new Type[0]));
    }
    // 如果字段本身就是泛型，但实际类型是class

    return null;
  }
}
