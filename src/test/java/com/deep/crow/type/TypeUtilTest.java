package com.deep.crow.type;

import com.deep.crow.compress.TypeUtil;
import com.deep.crow.model.Person;
import com.deep.crow.model.User;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;

/** @author Create by liuwenhao on 2022/4/24 12:49 */
@SuppressWarnings("all")
public class TypeUtilTest extends TestCase {
  public List<Object> fill() {
    List<Integer> list1 = new ArrayList<>();
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

    Map<Integer, String> map1 = new HashMap<>();
    map1.put(1, "liu");
    map1.put(2, "wen");

    Map<Integer, Integer> map2 = new HashMap<>();
    map2.put(1, 123);
    map2.put(2, 456);

    List<Object> listObj = new ArrayList<>();
    listObj.add(list4);
    listObj.add(list1);
    listObj.add(list2);
    listObj.add(list3);
    listObj.add(user1);
    listObj.add(user2);
    listObj.add(integer);
    listObj.add(str);
    listObj.add(map1);
    listObj.add(map2);
    return listObj;
  }

  public void test1() {
    List<Object> listObj = fill();
    String screenTypes = TypeUtil.screenClass(listObj, String.class);
    System.out.println(screenTypes);
  }

  public void test2() {
    List<Object> listObj = fill();
    List<Integer> screenTypes = TypeUtil.screenType(listObj, List.class, Integer.class);
    System.out.println(screenTypes);
  }

  public void test3() {
    List<Object> listObj = fill();
    List<List<Integer>> screenTypes = TypeUtil.screenTypes(listObj, List.class, Integer.class);
    System.out.println(screenTypes);
  }

  public void test4() {
    List<Object> listObj = fill();
    Type type = TypeBuilder.make(List.class).addArgs(Integer.class).build();
    List<Integer> screenType = TypeUtil.screenType(listObj, type);
    System.out.println(screenType);
  }

  public void test5() {
    List<Object> listObj = fill();
    Type type = TypeBuilder.make(List.class).addArgs(Integer.class).build();
    List<List<Integer>> screenType = TypeUtil.screenTypes(listObj, type);
    System.out.println(screenType);
  }

  public void test7() {
    List<Object> listObj = fill();
    Person person = new Person();
    TypeUtil.fillInstance(listObj, person);
    System.out.println(person);
  }
}
