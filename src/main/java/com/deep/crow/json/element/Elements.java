package com.deep.crow.json.element;

import com.deep.crow.exception.CrowException;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class Elements {

  static List<Element> elements = new LinkedList<>();

  private Elements() {
  }

  static {
    elements.add(new SimpleElement());
//    elements.add(new EnumElement());
    elements.add(new ArrayElement());
    elements.add(new IterableElement());
    elements.add(new MapElement());
    elements.add(new ObjectElement());
  }

  public static Element getElement(Type type) {
    for (Element e : elements) {
      boolean support = e.isSupport(type);
      if (support){
        return e;
      }
    }
    throw new CrowException("不支持的类型");
  }
}
