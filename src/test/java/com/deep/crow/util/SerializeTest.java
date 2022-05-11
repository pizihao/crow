package com.deep.crow.util;

import com.deep.crow.jackson.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/5/9 11:43
 */
public class SerializeTest {
    public static void main(String[] args) throws JsonProcessingException {

        Map<String, Long> map = new HashMap<>();

        map.put("liu", 123L);
        map.put("45", 54878L);
        ObjectMapper objectMapper = ObjectMapperFactory.get();
        String valueAsString = objectMapper.writeValueAsString(map);
        System.out.println(valueAsString);
        Map<String, Long> longList = objectMapper.convertValue(map, new TypeReference<Map<String, Long>>() {
        });
        System.out.println(longList);
//        List<Long> longs = new ArrayList<>();
//        longs.add(123L);
//        longs.add(4563L);
//        ObjectMapper objectMapper = ObjectMapperFactory.get();
//
//        String valueAsString = objectMapper.writeValueAsString(longs);
//        System.out.println(valueAsString);
//        List<Long> longList = objectMapper.convertValue(longs, new TypeReference<List<Long>>() {
//        });
//        System.out.println(longList);

//        List<Long> list = objectMapper.readValue(valueAsString, new TypeReference<List<Long>>() {
//        });
//        System.out.println(list);

    }

    public static class LongType {
        String type = "java.lang.Long";
        Long aLong;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Long getaLong() {
            return aLong;
        }

        public void setaLong(Long aLong) {
            this.aLong = aLong;
        }
    }
}