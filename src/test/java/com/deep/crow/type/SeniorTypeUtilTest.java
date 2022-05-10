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
        packing();
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

    /**
     * 测试包装类型
     * Integer，Long，Double，Character，Float，Byte，Short，Boolean
     *
     * @author liuwenhao
     * @date 2022/5/9 10:16
     */
    private static void packing() {
        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        List<String> strings = new ArrayList<>();
        strings.add("123");
        strings.add("456");
        List<Double> doubles = new ArrayList<>();
        doubles.add(1.20001);
        doubles.add(20.220002);
        List<Float> floats = new ArrayList<>();
        floats.add(1.1f);
        floats.add(1.556f);
        List<Short> shorts = new ArrayList<>();
        shorts.add((short) 4);
        shorts.add((short) 1288);
        List<Byte> bytes = new ArrayList<>();
        bytes.add((byte) 125);
        bytes.add((byte) 15);
        List<Long> longs = new ArrayList<>();
        longs.add(45L);
        longs.add(45487L);
        List<Character> characters = new ArrayList<>();
        characters.add('a');
        characters.add('Z');
        List<Boolean> booleans = new ArrayList<>();
        booleans.add(true);
        booleans.add(false);

        Basic basic = new Basic();
        List<Object> list = new ArrayList<>();
        list.add(longs);
        list.add(integers);
        list.add(shorts);
        list.add(characters);
        list.add(bytes);
        list.add(floats);
        list.add(doubles);
        list.add(strings);
        list.add(booleans);
        TypeUtil.fillInstance(list, basic);
        System.out.println(basic);

    }

}