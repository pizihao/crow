package com.deep.crow.json;

import com.deep.crow.json.element.Element;
import com.deep.crow.json.element.Elements;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonTest extends TestCase {

  public void testData() throws Exception {

    List<IOUser> strings = new ArrayList<>();
    strings.add(new IOUser("456"));
    strings.add(new IOUser("75469"));

    IOPerson<Integer> ioPerson = new IOPerson<>(13, "dhkf");

    Map<String, Integer> map = new HashMap<>();
    map.put("456", 32);
    map.put("djkl", 6);
    map.put("ssaa", 789);

    IOSimpleObj simpleObj = new IOSimpleObj("Str", 1527, strings, ioPerson, map);
    Mapper mapper = new Mapper(simpleObj);
    Element element = Elements.getElement(simpleObj.getClass());
    element.serializer(mapper, simpleObj, null);
    System.out.println(mapper);

  }
}
