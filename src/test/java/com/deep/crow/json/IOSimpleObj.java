package com.deep.crow.json;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class IOSimpleObj implements Serializable {

  String str;

  Integer integer;

  List<IOUser> strings;

  IOPerson<Integer> ioPerson;

  Map<String, Integer> map;

  public IOSimpleObj(String str, Integer integer, List<IOUser> strings, IOPerson<Integer> ioPerson, Map<String, Integer> map) {
    this.str = str;
    this.integer = integer;
    this.strings = strings;
    this.ioPerson = ioPerson;
    this.map = map;
  }

  public IOPerson<Integer> getIoPerson() {
    return ioPerson;
  }

  public void setIoPerson(IOPerson<Integer> ioPerson) {
    this.ioPerson = ioPerson;
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

  public List<IOUser> getStrings() {
    return strings;
  }

  public void setStrings(List<IOUser> strings) {
    this.strings = strings;
  }

  public Map<String, Integer> getMap() {
    return map;
  }

  public void setMap(Map<String, Integer> map) {
    this.map = map;
  }

}
