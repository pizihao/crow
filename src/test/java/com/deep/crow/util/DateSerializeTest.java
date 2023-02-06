package com.deep.crow.util;

import com.deep.crow.json.Mapper;
import com.deep.crow.type.TypeBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author Create by liuwenhao on 2022/5/9 11:43
 */
@SuppressWarnings("all")
public class DateSerializeTest extends TestCase {
  public void testDateSerialize() {
    List<Date> localDateTimes = new ArrayList<>();
    Date date1 = new Date();
    localDateTimes.add(date1);
    Date date2 = new Date();
    localDateTimes.add(date2);
    System.out.println(localDateTimes);
    Mapper valueAsString = JsonUtil.objToString(localDateTimes, TypeBuilder.list(Date.class));
    System.out.println(valueAsString);
    List<Date> longList =
        JsonUtil.jsonToObj(valueAsString, TypeBuilder.list(Date.class));
    System.out.println(longList);
  }
}
