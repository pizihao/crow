package com.deep.crow.json;

public class IOUser {

  String name;

  public IOUser(String name) {
    this.name = name;
  }

  public IOUser() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "IOUser{" +
        "name='" + name + '\'' +
        '}';
  }
}
