package com.deep.crow.type;

import com.deep.crow.exception.CrowException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Objects;

/**
 * 参数化类型 例如：<br>
 * {@code List<T> }<br>
 * 仿照{@link sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl}
 *
 * @author Create by liuwenhao on 2022/4/24 15:13
 */
@SuppressWarnings("unused")
public class ParameterizedTypeImpl implements ParameterizedType {
  private final Type[] actualTypeArguments;
  private final Class<?> rawType;
  private final Type ownerType;

  private ParameterizedTypeImpl(Class<?> rawType, Type[] actualTypeArguments, Type ownerType) {
    this.actualTypeArguments = actualTypeArguments;
    this.rawType = rawType;
    this.ownerType = (ownerType != null ? ownerType : rawType.getDeclaringClass());
    this.validateConstructorArguments();
  }

  public static ParameterizedTypeImpl make(
      Class<?> rawType, Type[] actualTypeArguments, Type ownerType) {
    return new ParameterizedTypeImpl(rawType, actualTypeArguments, ownerType);
  }

  public static ParameterizedTypeImpl make(Class<?> rawType, Type[] actualTypeArguments) {
    return new ParameterizedTypeImpl(rawType, actualTypeArguments, null);
  }

  private void validateConstructorArguments() {
    TypeVariable<?>[] typeParameters = this.rawType.getTypeParameters();
    int need = typeParameters.length;
    int actual = this.actualTypeArguments.length;
    if (need != actual) {
      throw CrowException.exception("泛型个数不对等：--需要的：{}个，--实际的：{}个", need, actual);
    }
  }

  public Type[] getActualTypeArguments() {
    return this.actualTypeArguments.clone();
  }

  public Class<?> getRawType() {
    return this.rawType;
  }

  public Type getOwnerType() {
    return this.ownerType;
  }

  public boolean equals(Object obj) {
    if (obj instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) obj;
      if (this == parameterizedType) {
        return true;
      } else {
        Type typeOwnerType = parameterizedType.getOwnerType();
        Type typeRawType = parameterizedType.getRawType();
        return Objects.equals(this.ownerType, typeOwnerType)
            && Objects.equals(this.rawType, typeRawType)
            && Arrays.equals(this.actualTypeArguments, parameterizedType.getActualTypeArguments());
      }
    } else {
      return false;
    }
  }

  public int hashCode() {
    return Arrays.hashCode(this.actualTypeArguments)
        ^ Objects.hashCode(this.ownerType)
        ^ Objects.hashCode(this.rawType);
  }

  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (this.ownerType != null) {
      if (this.ownerType instanceof Class) {
        builder.append(((Class<?>) this.ownerType).getName());
      } else {
        builder.append(this.ownerType);
      }

      builder.append(".");
      if (this.ownerType instanceof ParameterizedTypeImpl) {
        builder.append(
            this.rawType
                .getName()
                .replace(((ParameterizedTypeImpl) this.ownerType).rawType.getName() + "$", ""));
      } else {
        builder.append(this.rawType.getName());
      }
    } else {
      builder.append(this.rawType.getName());
    }
    if (this.actualTypeArguments != null && this.actualTypeArguments.length > 0) {
      builder.append("<");
      boolean b = true;
      for (Type type : this.actualTypeArguments) {
        if (!b) {
          builder.append(", ");
        }
        builder.append(type.getTypeName());
        b = false;
      }
      builder.append(">");
    }
    return builder.toString();
  }
}
