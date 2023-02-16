package com.deep.crow.util;

import com.deep.crow.json.Mapper;
import com.deep.crow.json.element.Element;
import com.deep.crow.json.element.Elements;

import java.lang.reflect.Type;

public class JsonUtil {

  private JsonUtil() {
  }

  /**
   * 将某个对象转化为json
   *
   * @param o    要转化的对象
   * @param type o的具体类型
   * @return json串
   */
  public static Mapper objToString(Object o, Type type) {
    Element element = Elements.getElement(type);
    return element.serializer(type, o, null, false);
  }

  /**
   * 反序列化操作
   *
   * @param mapper json串
   * @param type   反序列化的类型
   * @return 结果对象
   */
  public static <T> T jsonToObj(Mapper mapper, Type type) {
    Element element = Elements.getElement(type);
    return element.deserializer(mapper, type);
  }

}
