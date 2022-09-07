package com.deep.crow.model;

/** @author Create by liuwenhao on 2022/4/28 11:35 */
public class Cat {

  private String name;

  public Cat(String name) {
    this.name = name;
  }

  public Cat() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Cat{" + "name='" + name + '\'' + '}';
  }
}
