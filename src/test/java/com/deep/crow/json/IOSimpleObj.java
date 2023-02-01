package com.deep.crow.json;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class IOSimpleObj implements Serializable {

  String str;

  Integer integer;

  List<String> strings;

  Map<String, Integer> map;

  public IOSimpleObj(String str, Integer integer, List<String> strings, Map<String, Integer> map) {
    this.str = str;
    this.integer = integer;
    this.strings = strings;
    this.map = map;
  }

  public IOSimpleObj() {
  }

  public String getStr() {
    return str;
  }

  public void setStr(String str) {
    this.str = str;
  }

  public Integer getInteger() {
    return integer;
  }

  public void setInteger(Integer integer) {
    this.integer = integer;
  }

  public List<String> getStrings() {
    return strings;
  }

  public void setStrings(List<String> strings) {
    this.strings = strings;
  }

  public Map<String, Integer> getMap() {
    return map;
  }

  public void setMap(Map<String, Integer> map) {
    this.map = map;
  }
}
