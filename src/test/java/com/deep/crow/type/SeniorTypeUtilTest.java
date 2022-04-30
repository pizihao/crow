package com.deep.crow.type;

import com.deep.crow.model.*;
import com.deep.crow.util.TypeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/4/28 10:33
 */
public class SeniorTypeUtilTest {

    public static void main(String[] args) {
        parameter();
    }

    private static void example() {
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

    private static void parameter() {
        Bird bird1 = new Bird();
        bird1.setName("鸟1");
        Bird bird2 = new Bird();
        bird2.setName("鸟2");
        List<Bird> birds = new ArrayList<>();
        birds.add(bird1);
        birds.add(bird2);

        Cat cat1 = new Cat();
        cat1.setName("猫1");
        Cat cat2 = new Cat();
        cat2.setName("猫2");
        List<Cat> cats = new ArrayList<>();
        cats.add(cat1);
        cats.add(cat2);

        User user1 = new User();
        user1.setName("liu");
        User user2 = new User();
        user2.setName("wen");
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        Dog dog1 = new Dog();
        dog1.setName("狗1");
        Dog dog2 = new Dog();
        dog2.setName("狗2");
        List<Dog> dogs = new ArrayList<>();
        dogs.add(dog1);
        dogs.add(dog2);

        Animal animal = new Animal();
        List<Object> list = new ArrayList<>();
        list.add(1);
        list.add(birds);
        list.add(cats);
        list.add(dogs);
        list.add(users);
        TypeUtil.fillInstance(list, animal);
        System.out.println(animal);
    }

}