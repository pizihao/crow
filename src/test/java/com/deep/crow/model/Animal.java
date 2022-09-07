package com.deep.crow.model;

import java.util.List;

/** @author Create by liuwenhao on 2022/4/28 11:37 */
public class Animal {

  String string;
  Bird bird;
  List<Bird> birds;

  User user;
  List<User> users;

  Cat cat;
  List<Cat> cats;

  Dog dog;
  List<Dog> dogs;

  public List<Bird> getBirds() {
    return birds;
  }

  public void setBirds(List<Bird> birds) {
    this.birds = birds;
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  public List<Cat> getCats() {
    return cats;
  }

  public void setCats(List<Cat> cats) {
    this.cats = cats;
  }

  public List<Dog> getDogs() {
    return dogs;
  }

  public void setDogs(List<Dog> dogs) {
    this.dogs = dogs;
  }

  public Bird getBird() {
    return bird;
  }

  public void setBird(Bird bird) {
    this.bird = bird;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Cat getCat() {
    return cat;
  }

  public void setCat(Cat cat) {
    this.cat = cat;
  }

  public Dog getDog() {
    return dog;
  }

  public void setDog(Dog dog) {
    this.dog = dog;
  }

  public String getString() {
    return string;
  }

  public void setString(String string) {
    this.string = string;
  }

  @Override
  public String toString() {
    return "Animal{"
        + "string='"
        + string
        + '\''
        + ", bird="
        + bird
        + ", birds="
        + birds
        + ", user="
        + user
        + ", users="
        + users
        + ", cat="
        + cat
        + ", cats="
        + cats
        + ", dog="
        + dog
        + ", dogs="
        + dogs
        + '}';
  }
}
