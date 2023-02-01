package com.deep.crow.json;


import com.deep.crow.json.data.Data;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonTest extends TestCase {

  public void testData() throws Exception {

    Integer a = 132;
    System.out.println(a.getClass().isPrimitive());

    List<String> strings = new ArrayList<>();
    strings.add("qwe");
    strings.add("wer");

    Map<String, Integer> map = new HashMap<>();
    map.put("456", 32);
    map.put("djkl", 6);
    map.put("ssaa", 789);

    IOSimpleObj simpleObj = new IOSimpleObj("Str", 1527, strings, map);

    Data data = new Data(simpleObj);
    System.out.println(data);
  }
}
