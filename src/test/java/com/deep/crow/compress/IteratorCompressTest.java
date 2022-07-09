package com.deep.crow.compress;

import com.deep.crow.model.Cat;
import com.deep.crow.model.User;
import com.deep.crow.type.TypeBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
        ObjectMapper objectMapper = new ObjectMapper();
        IteratorCompress userCompress = new IteratorCompress(users, TypeBuilder.list(User.class), objectMapper);
        IteratorCompress catCompress = new IteratorCompress(cats, TypeBuilder.list(Cat.class), objectMapper);
        User user = userCompress.compress();
        Cat cat = catCompress.compress();
        Assert.assertEquals(user.getName(), "1");
        Assert.assertEquals(cat.getName(), "3");
    }

    @Test
    public void testCheck() {
        ObjectMapper objectMapper = new ObjectMapper();
        IteratorCompress userCompress = new IteratorCompress(users, TypeBuilder.list(User.class), objectMapper);
        IteratorCompress catCompress = new IteratorCompress(users, TypeBuilder.list(Cat.class), objectMapper);
        boolean userCheck = userCompress.check();
        boolean catCheck = catCompress.check();
        Assert.assertTrue(userCheck);
        Assert.assertFalse(catCheck);
    }
}