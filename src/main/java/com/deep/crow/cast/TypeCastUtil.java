package com.deep.crow.cast;

import com.deep.crow.exception.CrowException;
import com.deep.crow.json.ObjectMapperFactory;
import com.deep.crow.type.CrowTypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 通过类型强转的方式进行数据填充
 */
public class TypeCastUtil {

  private TypeCastUtil() {
  }

  /**
   * 筛选结果类型 在一个未知的结果集中匹配指定类型的项<br>
   * 无匹配选项则抛出异常
   *
   * @param l    结果集
   * @param type 检索的类型
   * @return T
   * @author liuwenhao
   * @date 2022/4/24 16:27
   */
  public static <T> T screenType(Iterable<?> l, Type type) {



    throw CrowException.exception("无可匹配类型，{}", type.getTypeName());
  }
}
