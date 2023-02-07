package com.deep.crow.compress;

import com.deep.crow.model.Cat;
import com.deep.crow.model.User;
import com.deep.crow.type.TypeBuilder;
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
    Compress userCompress =
        new MapCompress(userMap, TypeBuilder.map(Integer.class, User.class));
    Compress catCompress =
        new MapCompress(catMap, TypeBuilder.map(String.class, Cat.class));
    Map.Entry<Integer, User> usersEntity = userCompress.compress();
    Map.Entry<String, Cat> catsEntity = catCompress.compress();
    Assert.assertEquals(usersEntity.getKey(), Integer.valueOf(456));
    Assert.assertEquals("2", usersEntity.getValue().getName());
    Assert.assertEquals("cat2", catsEntity.getKey());
    Assert.assertEquals("4", catsEntity.getValue().getName());
  }

  @Test
  public void testCheck() {
    Compress userCompress =
        new MapCompress(userMap, TypeBuilder.map(Integer.class, User.class));
    Compress catCompress =
        new MapCompress(catMap, TypeBuilder.map(Integer.class, Cat.class));
    boolean userCheck = userCompress.check();
    boolean catCheck = catCompress.check();
    Assert.assertTrue(userCheck);
    Assert.assertFalse(catCheck);
  }
}
