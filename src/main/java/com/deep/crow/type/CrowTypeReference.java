package com.deep.crow.type;

import com.fasterxml.jackson.core.type.TypeReference;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 协助泛型检索
 *
 * @author Create by liuwenhao on 2022/6/2 15:32
 */
public class CrowTypeReference<T> extends TypeReference<T> {
  Type type;

  private CrowTypeReference(Type type) {
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
