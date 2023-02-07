package com.deep.crow.compress;

import com.deep.crow.model.Cat;
import com.deep.crow.model.User;
import com.deep.crow.type.TypeBuilder;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class IteratorCompressTest {

  List<User> users = new ArrayList<>();
  List<Cat> cats = new ArrayList<>();

  {
    User user1 = new User("1");
    User user2 = new User("2");
    users.add(user1);
    users.add(user2);
    Cat cat1 = new Cat("3");
    Cat cat2 = new Cat("4");
    cats.add(cat1);
    cats.add(cat2);
  }

  @Test
  public void testCompress() {
    IteratorCompress userCompress =
        new IteratorCompress(users, TypeBuilder.list(User.class));
    IteratorCompress catCompress =
        new IteratorCompress(cats, TypeBuilder.list(Cat.class));
    User user = userCompress.compress();
    Cat cat = catCompress.compress();
    Assert.assertEquals("1", user.getName());
    Assert.assertEquals("3", cat.getName());
  }

  @Test
  public void testCheck() {
    IteratorCompress userCompress =
        new IteratorCompress(users, TypeBuilder.list(User.class));
    IteratorCompress catCompress =
        new IteratorCompress(users, TypeBuilder.list(Cat.class));
    boolean userCheck = userCompress.check();
    boolean catCheck = catCompress.check();
    Assert.assertTrue(userCheck);
    Assert.assertFalse(catCheck);
  }
}
