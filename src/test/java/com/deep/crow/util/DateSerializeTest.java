package com.deep.crow.util;

import com.deep.crow.jackson.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/5/9 11:43
 */
public class DateSerializeTest {
    public static void main(String[] args) throws JsonProcessingException {
        List<LocalDateTime> localDateTimes = new ArrayList<>();
        LocalDateTime date = LocalDateTime.now();
        localDateTimes.add(date);
        localDateTimes.add(date.plusMinutes(30));
        System.out.println(localDateTimes);
        ObjectMapper objectMapper = ObjectMapperFactory.get();

        String valueAsString = objectMapper.writeValueAsString(localDateTimes);
        System.out.println(valueAsString);
        List<LocalDateTime> longList = objectMapper.convertValue(localDateTimes, new TypeReference<List<LocalDateTime>>() {
        });
        System.out.println(longList);

//        List<Long> list = objectMapper.readValue(valueAsString, new TypeReference<List<Long>>() {
//        });
//        System.out.println(list);

    }

}