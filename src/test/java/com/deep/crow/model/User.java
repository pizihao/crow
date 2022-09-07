package com.deep.crow.model;

/** @author Create by liuwenhao on 2022/4/14 10:39 */
public class User {

  private String name;

  public User(String name) {
    this.name = name;
  }

  public User() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "User{" + "name='" + name + '\'' + '}';
  }
}
