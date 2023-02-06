package com.deep.crow.json.element;

import com.deep.crow.exception.CrowException;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class Elements {

  static final List<Element> ELEMENT = new LinkedList<>();

  private Elements() {
  }

  static {
    ELEMENT.add(new SimpleElement());
    ELEMENT.add(new ArrayElement());
    ELEMENT.add(new IterableElement());
    ELEMENT.add(new MapElement());
    ELEMENT.add(new ObjectElement());
  }

  public static Element getElement(Type type) {
    for (Element e : ELEMENT) {
      boolean support = e.isSupport(type);
      if (support){
        return e;
      }
    }
    throw new CrowException("不支持的类型");
  }
}
