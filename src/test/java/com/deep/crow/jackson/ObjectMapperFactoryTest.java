package com.deep.crow.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

public class ObjectMapperFactoryTest {

  @Test
  public void testIntSerialize() throws JsonProcessingException {
    ObjectMapper objectMapper = ObjectMapperFactory.get();
    Integer i = 123;
    String s = objectMapper.writeValueAsString(i);
    Integer value = objectMapper.readValue(s, Integer.class);
    Assert.assertEquals(value, i);
  }

  @Test
  public void testDoubleSerialize() throws JsonProcessingException {
    ObjectMapper objectMapper = ObjectMapperFactory.get();
    Double i = 123.12;
    String s = objectMapper.writeValueAsString(i);
    Double value = objectMapper.readValue(s, Double.class);
    Assert.assertEquals(value, i);
  }

  @Test
  public void testLocalDateSerialize() throws JsonProcessingException {
    ObjectMapper objectMapper = ObjectMapperFactory.get();
    LocalDate localDate = LocalDate.now();
    String s = objectMapper.writeValueAsString(localDate);
    LocalDate value = objectMapper.readValue(s, LocalDate.class);
    Assert.assertEquals(value, localDate);
  }

  // ...

}
