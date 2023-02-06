package com.deep.crow.util;

import com.deep.crow.compress.TypeUtil;
import com.deep.crow.model.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Test;

/** @author Create by liuwenhao on 2022/6/2 10:07 */
@SuppressWarnings("all")
public class TypeUtilTest {

  @Test
  public void testTypeFillSpeed() {
    List<Bird> birds = new ArrayList<>();
    List<Cat> cats = new ArrayList<>();
    List<User> users = new ArrayList<>();
    List<Dog> dogs = new ArrayList<>();

    List<Object> list = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      Bird bird1 = new Bird();
      bird1.setName("鸟1");
      birds.add(bird1);
      Cat cat1 = new Cat();
      cat1.setName("猫1");
      cats.add(cat1);
      User user1 = new User();
      user1.setName("liu");
      users.add(user1);
      Dog dog1 = new Dog();
      dog1.setName("狗1");
      dogs.add(dog1);
    }

    list.add(birds);
    list.add(cats);
    list.add(dogs);
    list.add(users);
    List<Animal> animal = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      animal.add(new Animal());
    }
    long l = System.currentTimeMillis();
    animal.forEach(a -> TypeUtil.fillInstance(list, a));
    long t = System.currentTimeMillis();
    System.out.println(t - l);
  }

  @Test
  public void testTypeFillInstance() {
    Bird bird = new Bird();
    bird.setName("鸟");
    Cat cat = new Cat();
    cat.setName("猫");
    User user = new User();
    user.setName("liu");
    Dog dog = new Dog();
    dog.setName("狗");
    Animal animal = new Animal();
    List<Object> list = new ArrayList<>();
    list.add(1);
    list.add(bird);
    list.add(cat);
    list.add(dog);
    list.add(user);
    TypeUtil.fillInstance(list, animal);
    System.out.println(animal);
  }

  @Test
  public void testFillCollection() {
    List<Bird> birds = new ArrayList<>();
    List<Cat> cats = new ArrayList<>();
    List<User> users = new ArrayList<>();
    List<Dog> dogs = new ArrayList<>();

    List<Object> list = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      Bird bird1 = new Bird();
      bird1.setName("鸟1");
      birds.add(bird1);
      Cat cat1 = new Cat();
      cat1.setName("猫1");
      cats.add(cat1);
      User user1 = new User();
      user1.setName("liu");
      users.add(user1);
      Dog dog1 = new Dog();
      dog1.setName("狗1");
      dogs.add(dog1);
    }

    list.add(birds);
    list.add(cats);
    list.add(dogs);
    list.add(users);
    List<Animal> animal = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      animal.add(new Animal());
    }
    long l = System.currentTimeMillis();
    Collection<Animal> animals = TypeUtil.fillCollection(list, animal);
    System.out.println(animals);
    long t = System.currentTimeMillis();
    System.out.println(t - l);
  }
}
