package com.deep.crow.model;

import java.util.List;
import java.util.Map;

/** @author Create by liuwenhao on 2022/4/26 15:02 */
public class Person {

  List<Integer> list1;
  List<String> list2;
  List<Integer> list3;
  List<String> list4;
  User user;
  Integer integer;

  Map<Integer, String> map1;

  Map<Integer, Integer> map2;

  public List<Integer> getList1() {
    return list1;
  }

  public void setList1(List<Integer> list1) {
    this.list1 = list1;
  }

  public List<String> getList2() {
    return list2;
  }

  public void setList2(List<String> list2) {
    this.list2 = list2;
  }

  public List<Integer> getList3() {
    return list3;
  }

  public void setList3(List<Integer> list3) {
    this.list3 = list3;
  }

  public List<String> getList4() {
    return list4;
  }

  public void setList4(List<String> list4) {
    this.list4 = list4;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Integer getInteger() {
    return integer;
  }

  public void setInteger(Integer integer) {
    this.integer = integer;
  }

  public Map<Integer, String> getMap1() {
    return map1;
  }

  public void setMap1(Map<Integer, String> map1) {
    this.map1 = map1;
  }

  public Map<Integer, Integer> getMap2() {
    return map2;
  }

  public void setMap2(Map<Integer, Integer> map2) {
    this.map2 = map2;
  }

  @Override
  public String toString() {
    return "Person{"
        + "list1="
        + list1
        + ", list2="
        + list2
        + ", list3="
        + list3
        + ", list4="
        + list4
        + ", user="
        + user
        + ", integer="
        + integer
        + ", map1="
        + map1
        + ", map2="
        + map2
        + '}';
  }
}
