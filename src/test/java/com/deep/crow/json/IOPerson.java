package com.deep.crow.json;

public class IOPerson<T> {

  T t;

  String name;

  public IOPerson(T t, String name) {
    this.t = t;
    this.name = name;
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
}
