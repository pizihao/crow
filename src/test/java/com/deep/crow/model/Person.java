package com.deep.crow.model;

import java.util.List;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/4/26 15:02
 */
public class Person {

    List<Integer> list1;
    List<String> list2;
    List<Integer> list3;
    List<String> list4;
    User user;
    Integer integer;

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

    @Override
    public String toString() {
        return "Person{" +
            "list1=" + list1 +
            ", list2=" + list2 +
            ", list3=" + list3 +
            ", list4=" + list4 +
            ", user=" + user +
            ", integer=" + integer +
            '}';
    }
}