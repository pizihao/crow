package com.deep.crow.util;

import com.deep.crow.jackson.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/5/9 11:43
 */
public class DateSerializeTest {
    public static void main(String[] args) throws JsonProcessingException {
        List<Date> localDateTimes = new ArrayList<>();
        Date date1 = new Date();
        localDateTimes.add(date1);
        Date date2 = new Date();
        localDateTimes.add(date2);
        System.out.println(localDateTimes);
        ObjectMapper objectMapper = ObjectMapperFactory.get();

        String valueAsString = objectMapper.writeValueAsString(localDateTimes);
        System.out.println(valueAsString);
        List<Date> longList = objectMapper.convertValue(localDateTimes, new TypeReference<List<Date>>() {
        });
        System.out.println(longList);

//        List<Long> list = objectMapper.readValue(valueAsString, new TypeReference<List<Long>>() {
//        });
//        System.out.println(list);

    }

}