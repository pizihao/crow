package com.deep.crow.util;

import com.deep.crow.jackson.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;

/** @author Create by liuwenhao on 2022/5/9 11:43 */
public class SerializeTest extends TestCase {
  public void testSerialize() throws JsonProcessingException {

    Map<String, Long> map = new HashMap<>();
    map.put("liu", 123L);
    map.put("45", 54878L);
    ObjectMapper objectMapper = ObjectMapperFactory.get();
    String valueAsString = objectMapper.writeValueAsString(map);
    System.out.println(valueAsString);
    Map<String, Long> longList =
        objectMapper.convertValue(map, new TypeReference<Map<String, Long>>() {});
    System.out.println(longList);
  }
}
