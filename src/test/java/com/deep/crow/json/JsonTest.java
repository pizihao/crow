package com.deep.crow.json;

import com.deep.crow.json.element.Element;
import com.deep.crow.json.element.Elements;
import junit.framework.TestCase;

import java.util.*;

@SuppressWarnings("all")
public class JsonTest extends TestCase {

  public void testData() throws Exception {

    List<IOUser> strings = new ArrayList<>();
    strings.add(new IOUser("456"));
    strings.add(new IOUser("75469"));

    List<Integer> integers = new LinkedList<>();
    integers.add(5);
    integers.add(7);

    IOPerson<Integer> ioPerson = new IOPerson<>(13, "dhkf", integers);

    Map<String, Integer> map = new HashMap<>();
    map.put("456", 32);
    map.put("djkl", 6);
    map.put("ssaa", 789);

    IOSimpleObj simpleObj = new IOSimpleObj("Str", 1527, strings, ioPerson, map);
    Class<? extends IOSimpleObj> objCls = simpleObj.getClass();
    Element element = Elements.getElement(objCls);
    Mapper mapper = element.serializer(objCls, simpleObj, null, false);
    String s = mapper.toString();
    System.out.println(s);

    IOSimpleObj o = element.deserializer(mapper, objCls);
    System.out.println(o);

  }
}
