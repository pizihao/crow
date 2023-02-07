package com.deep.crow.json;

import java.util.List;

public class IOPerson<T> {

  T t;

  String name;

  List<T> list;

  public IOPerson(T t, String name, List<T> list) {
    this.t = t;
    this.name = name;
    this.list = list;
  }

  public IOPerson() {
  }

  public T getT() {
    return t;
  }

  public void setT(T t) {
    this.t = t;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<T> getList() {
    return list;
  }

  public void setList(List<T> list) {
    this.list = list;
  }

  @Override
  public String toString() {
    return "IOPerson{" +
        "t=" + t +
        ", name='" + name + '\'' +
        ", list=" + list +
        '}';
  }
}
