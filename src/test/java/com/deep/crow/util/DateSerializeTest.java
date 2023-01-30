package com.deep.crow.util;

import com.deep.crow.json.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import junit.framework.TestCase;

/** @author Create by liuwenhao on 2022/5/9 11:43 */
public class DateSerializeTest extends TestCase {
  public void testDateSerialize() throws JsonProcessingException {
    List<Date> localDateTimes = new ArrayList<>();
    Date date1 = new Date();
    localDateTimes.add(date1);
    Date date2 = new Date();
    localDateTimes.add(date2);
    System.out.println(localDateTimes);
    ObjectMapper objectMapper = ObjectMapperFactory.get();
    String valueAsString = objectMapper.writeValueAsString(localDateTimes);
    System.out.println(valueAsString);
    List<Date> longList =
        objectMapper.convertValue(localDateTimes, new TypeReference<List<Date>>() {});
    System.out.println(longList);
  }
}
