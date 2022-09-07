package com.deep.crow.model;

/** @author Create by liuwenhao on 2022/4/28 11:36 */
public class Bird {

  private String name;

  public Bird(String name) {
    this.name = name;
  }

  public Bird() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Bird{" + "name='" + name + '\'' + '}';
  }
}
