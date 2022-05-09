package com.deep.crow.util;

import com.deep.crow.jackson.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/5/9 11:43
 */
public class SerializeTest {
    public static void main(String[] args) throws JsonProcessingException {
        List<Long> longs = new ArrayList<>();
        longs.add(123L);
        longs.add(4563L);
        ObjectMapper objectMapper = ObjectMapperFactory.get();

//        String valueAsString = objectMapper.writeValueAsString(longs);
//        System.out.println(valueAsString);
        List<Long> longList = objectMapper.convertValue(longs, new TypeReference<List<Long>>() {
        });
        System.out.println(longList);

//        List<Long> list = objectMapper.readValue(valueAsString, new TypeReference<List<Long>>() {
//        });
//        System.out.println(list);

    }
}