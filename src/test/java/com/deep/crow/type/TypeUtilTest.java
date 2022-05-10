package com.deep.crow.type;

import com.deep.crow.model.Person;
import com.deep.crow.model.User;
import com.deep.crow.util.TypeUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/4/24 12:49
 */
@SuppressWarnings("unchecked")
public class TypeUtilTest {
    public static void main(String[] args) throws IOException, NoSuchMethodException {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        List<String> list2 = new ArrayList<>();
        list2.add("liu");
        List<String> list3 = new ArrayList<>();
        list3.add("wen");
        List<Integer> list4 = new ArrayList<>();
        list4.add(6);
        list4.add(7);
        User user1 = new User("刘");
        User user2 = new User("wen");
        Integer integer = 10;
        String str = "456";

        List<Object> listObj = new ArrayList<>();
        listObj.add(list4);
        listObj.add(list1);
        listObj.add(list3);
        listObj.add(user1);
        listObj.add(user2);
        listObj.add(integer);
        listObj.add(str);
        TypeUtilTest utilTest = new TypeUtilTest();
//        utilTest.test1(listObj);
//        utilTest.test2(listObj);
//        utilTest.test3(listObj);
//        utilTest.test4(listObj);
//        utilTest.test5(listObj);
        utilTest.test7(listObj);
    }

    public void test1(List<Object> listObj) {
        String screenTypes = TypeUtil.screenClass(listObj, String.class);
        System.out.println(screenTypes);
    }

    public void test2(List<Object> listObj) {
        List<Integer> screenTypes = TypeUtil.screenType(listObj, List.class, Integer.class);
        System.out.println(screenTypes);
    }

    public void test3(List<Object> listObj) {
        List<List<Integer>> screenTypes = TypeUtil.screenTypes(listObj, List.class, Integer.class);
        System.out.println(screenTypes);
    }

    public void test4(List<Object> listObj) {
        Type type = TypeBuilder.make(List.class)
            .add(Integer.class)
            .build();
        List<Integer> screenType = TypeUtil.screenType(listObj, type);
        System.out.println(screenType);
    }

    public void test5(List<Object> listObj) {
        Type type = TypeBuilder.make(List.class)
            .add(Integer.class)
            .build();
        List<List<Integer>> screenType = TypeUtil.screenTypes(listObj, type);
        System.out.println(screenType);
    }

    public void test7(List<Object> listObj) {
        Person person = new Person();
        TypeUtil.fillInstance(listObj, person);
        System.out.println(person);
    }
}