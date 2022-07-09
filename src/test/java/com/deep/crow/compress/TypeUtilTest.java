package com.deep.crow.compress;


import com.deep.crow.model.*;
import com.deep.crow.type.TypeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TypeUtilTest {

    List<Object> list = new ArrayList<>();

    {
        String s = "132";
        Bird bird = new Bird("bird");
        List<Bird> birds = new ArrayList<>();
        birds.add(new Bird("bird1"));
        birds.add(new Bird("bird2"));
        User user = new User("user");
        List<User> users = new ArrayList<>();
        users.add(new User("user1"));
        users.add(new User("user2"));
        Cat cat = new Cat("cat");
        List<Cat> cats = new ArrayList<>();
        cats.add(new Cat("cat1"));
        cats.add(new Cat("cat2"));
        Dog dog = new Dog("dog");
        List<Dog> dogs = new ArrayList<>();
        dogs.add(new Dog("dog1"));
        dogs.add(new Dog("dog2"));
        list.add(s);
        list.add(bird);
        list.add(user);
        list.add(cat);
        list.add(dog);
        list.add(birds);
        list.add(users);
        list.add(cats);
        list.add(dogs);
    }

    @Test
    public void testScreenClass() {
        String s = TypeUtil.screenClass(list, String.class);
        Bird bird = TypeUtil.screenClass(list, Bird.class);
        User user = TypeUtil.screenClass(list, User.class);
        Cat cat = TypeUtil.screenClass(list, Cat.class);
        Dog dog = TypeUtil.screenClass(list, Dog.class);
        Assert.assertEquals(s, "132");
        Assert.assertEquals(bird.getName(), "bird");
        Assert.assertEquals(user.getName(), "user");
        Assert.assertEquals(cat.getName(), "cat");
        Assert.assertEquals(dog.getName(), "dog");
    }

    @Test
    public void testScreenClasses() {
        Cat cat = new Cat("cat1");
        Dog dog = new Dog("dog2");
        list.add(cat);
        list.add(dog);
        List<User> users = TypeUtil.screenClasses(list, User.class);
        List<Cat> cats = TypeUtil.screenClasses(list, Cat.class);
        List<Dog> dogs = TypeUtil.screenClasses(list, Dog.class);
        Assert.assertEquals(users.size(), 1);
        Assert.assertEquals(cats.size(), 2);
        Assert.assertEquals(dogs.size(), 2);
    }

    @Test
    public void testScreenType() {
        List<Bird> bird = TypeUtil.screenType(list, TypeBuilder.list(Bird.class));
        List<User> user = TypeUtil.screenType(list, TypeBuilder.list(User.class));
        List<Cat> cat = TypeUtil.screenType(list, TypeBuilder.list(Cat.class));
        List<Dog> dog = TypeUtil.screenType(list, TypeBuilder.list(Dog.class));
        Assert.assertEquals(bird.get(0).getName(), "bird1");
        Assert.assertEquals(bird.get(1).getName(), "bird2");
        Assert.assertEquals(user.get(0).getName(), "user1");
        Assert.assertEquals(user.get(1).getName(), "user2");
        Assert.assertEquals(cat.get(0).getName(), "cat1");
        Assert.assertEquals(cat.get(1).getName(), "cat2");
        Assert.assertEquals(dog.get(0).getName(), "dog1");
        Assert.assertEquals(dog.get(1).getName(), "dog2");
    }

    @Test
    public void testTestScreenType() {
        List<Bird> bird = TypeUtil.screenType(list, List.class, Bird.class);
        List<User> user = TypeUtil.screenType(list, List.class, User.class);
        List<Cat> cat = TypeUtil.screenType(list, List.class, Cat.class);
        List<Dog> dog = TypeUtil.screenType(list, List.class, Dog.class);
        Assert.assertEquals(bird.get(0).getName(), "bird1");
        Assert.assertEquals(bird.get(1).getName(), "bird2");
        Assert.assertEquals(user.get(0).getName(), "user1");
        Assert.assertEquals(user.get(1).getName(), "user2");
        Assert.assertEquals(cat.get(0).getName(), "cat1");
        Assert.assertEquals(cat.get(1).getName(), "cat2");
        Assert.assertEquals(dog.get(0).getName(), "dog1");
        Assert.assertEquals(dog.get(1).getName(), "dog2");
    }

    @Test
    public void testFillInstance() {
        Animal animal = new Animal();
        TypeUtil.fillInstance(list, animal);
        Assert.assertEquals(animal.getBird().getName(), "bird");
        Assert.assertEquals(animal.getUser().getName(), "user");
        Assert.assertEquals(animal.getCat().getName(), "cat");
        Assert.assertEquals(animal.getDog().getName(), "dog");
        Assert.assertEquals(animal.getBirds().size(), 2);
        Assert.assertEquals(animal.getUsers().size(), 2);
        Assert.assertEquals(animal.getCats().size(), 2);
        Assert.assertEquals(animal.getDogs().size(), 2);
    }

    public void testFillClass() {
        Animal animal = TypeUtil.fillClass(list, Animal.class);
        Assert.assertEquals(animal.getBird().getName(), "bird");
        Assert.assertEquals(animal.getUser().getName(), "user");
        Assert.assertEquals(animal.getCat().getName(), "cat");
        Assert.assertEquals(animal.getDog().getName(), "dog");
        Assert.assertEquals(animal.getBirds().size(), 2);
        Assert.assertEquals(animal.getUsers().size(), 2);
        Assert.assertEquals(animal.getCats().size(), 2);
        Assert.assertEquals(animal.getDogs().size(), 2);
    }

}