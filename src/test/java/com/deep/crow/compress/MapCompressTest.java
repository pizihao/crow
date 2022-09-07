package com.deep.crow.compress;

import com.deep.crow.model.Cat;
import com.deep.crow.model.User;
import com.deep.crow.type.TypeBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class MapCompressTest {

  Map<Integer, User> userMap = new HashMap<>();

  Map<String, Cat> catMap = new HashMap<>();

  {
    User user1 = new User("1");
    User user2 = new User("2");
    userMap.put(123, user1);
    userMap.put(456, user2);
    Cat cat1 = new Cat("3");
    Cat cat2 = new Cat("4");
    catMap.put("cat1", cat1);
    catMap.put("cat2", cat2);
  }

  @Test
  public void testCompress() {
    ObjectMapper objectMapper = new ObjectMapper();
    Compress userCompress =
        new MapCompress(userMap, TypeBuilder.map(Integer.class, User.class), objectMapper);
    Compress catCompress =
        new MapCompress(catMap, TypeBuilder.map(String.class, Cat.class), objectMapper);
    Map.Entry<Integer, User> usersEntity = userCompress.compress();
    Map.Entry<String, Cat> catsEntity = catCompress.compress();
    Assert.assertEquals(usersEntity.getKey(), Integer.valueOf(456));
    Assert.assertEquals(usersEntity.getValue().getName(), "2");
    Assert.assertEquals(catsEntity.getKey(), "cat2");
    Assert.assertEquals(catsEntity.getValue().getName(), "4");
  }

  @Test
  public void testCheck() {
    ObjectMapper objectMapper = new ObjectMapper();
    Compress userCompress =
        new MapCompress(userMap, TypeBuilder.map(Integer.class, User.class), objectMapper);
    Compress catCompress =
        new MapCompress(catMap, TypeBuilder.map(Integer.class, Cat.class), objectMapper);
    boolean userCheck = userCompress.check();
    boolean catCheck = catCompress.check();
    Assert.assertTrue(userCheck);
    Assert.assertFalse(catCheck);
  }
}
