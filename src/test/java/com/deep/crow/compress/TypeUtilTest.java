package com.deep.crow.compress;

import com.deep.crow.model.*;
import com.deep.crow.type.TypeBuilder;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Assert;

public class TypeUtilTest extends TestCase {

  List<Object> list = new ArrayList<>();

  {
    String s = "132";
    Bird bird = new Bird("bird");
    List<Bird> birds = new ArrayList<>();
    birds.add(new Bird("bird1"));
    birds.add(new Bird("bird2"));
    User user = new User("user");
    List<User> users = new ArrayList<>();
    users.add(new User("user1"));
    users.add(new User("user2"));
    Cat cat = new Cat("cat");
    List<Cat> cats = new ArrayList<>();
    cats.add(new Cat("cat1"));
    cats.add(new Cat("cat2"));
    Dog dog = new Dog("dog");
    List<Dog> dogs = new ArrayList<>();
    dogs.add(new Dog("dog1"));
    dogs.add(new Dog("dog2"));
    list.add(s);
    list.add(bird);
    list.add(user);
    list.add(cat);
    list.add(dog);
    list.add(birds);
    list.add(users);
    list.add(cats);
    list.add(dogs);
  }

  public void testScreenClass() {
    String s = TypeUtil.screenClass(list, String.class);
    Bird bird = TypeUtil.screenClass(list, Bird.class);
    User user = TypeUtil.screenClass(list, User.class);
    Cat cat = TypeUtil.screenClass(list, Cat.class);
    Dog dog = TypeUtil.screenClass(list, Dog.class);
    Assert.assertEquals("132", s);
    Assert.assertEquals("bird", bird.getName());
    Assert.assertEquals("user", user.getName());
    Assert.assertEquals("cat", cat.getName());
    Assert.assertEquals("dog", dog.getName());
  }

  public void testScreenClasses() {
    Cat cat = new Cat("cat1");
    Dog dog = new Dog("dog2");
    list.add(cat);
    list.add(dog);
    List<User> users = TypeUtil.screenClasses(list, User.class);
    List<Cat> cats = TypeUtil.screenClasses(list, Cat.class);
    List<Dog> dogs = TypeUtil.screenClasses(list, Dog.class);
    Assert.assertEquals(1, users.size());
    Assert.assertEquals(2, cats.size());
    Assert.assertEquals(2, dogs.size());
  }

  public void testScreenType() {
    List<Bird> bird = TypeUtil.screenType(list, TypeBuilder.list(Bird.class));
    List<User> user = TypeUtil.screenType(list, TypeBuilder.list(User.class));
    List<Cat> cat = TypeUtil.screenType(list, TypeBuilder.list(Cat.class));
    List<Dog> dog = TypeUtil.screenType(list, TypeBuilder.list(Dog.class));
    Assert.assertEquals("bird1", bird.get(0).getName());
    Assert.assertEquals("bird2", bird.get(1).getName());
    Assert.assertEquals("user1", user.get(0).getName());
    Assert.assertEquals("user2", user.get(1).getName());
    Assert.assertEquals("cat1", cat.get(0).getName());
    Assert.assertEquals("cat2", cat.get(1).getName());
    Assert.assertEquals("dog1", dog.get(0).getName());
    Assert.assertEquals("dog2", dog.get(1).getName());
  }

  @SuppressWarnings("unchecked")
  public void testTestScreenType() {
    List<Bird> bird = TypeUtil.screenType(list, List.class, Bird.class);
    List<User> user = TypeUtil.screenType(list, List.class, User.class);
    List<Cat> cat = TypeUtil.screenType(list, List.class, Cat.class);
    List<Dog> dog = TypeUtil.screenType(list, List.class, Dog.class);
    Assert.assertEquals("bird1", bird.get(0).getName());
    Assert.assertEquals("bird2", bird.get(1).getName());
    Assert.assertEquals("user1", user.get(0).getName());
    Assert.assertEquals("user2", user.get(1).getName());
    Assert.assertEquals("cat1", cat.get(0).getName());
    Assert.assertEquals("cat2", cat.get(1).getName());
    Assert.assertEquals("dog1", dog.get(0).getName());
    Assert.assertEquals("dog2", dog.get(1).getName());
  }

  public void testFillInstance() {
    Animal animal = new Animal();
    TypeUtil.fillInstance(list, animal);
    Assert.assertEquals("bird", animal.getBird().getName());
    Assert.assertEquals("user", animal.getUser().getName());
    Assert.assertEquals("cat", animal.getCat().getName());
    Assert.assertEquals("dog", animal.getDog().getName());
    Assert.assertEquals(2, animal.getBirds().size());
    Assert.assertEquals(2, animal.getUsers().size());
    Assert.assertEquals(2, animal.getCats().size());
    Assert.assertEquals(2, animal.getDogs().size());
  }

  public void testFillClass() {
    Animal animal = TypeUtil.fillClass(list, Animal.class);
    Assert.assertEquals("bird", animal.getBird().getName());
    Assert.assertEquals("user", animal.getUser().getName());
    Assert.assertEquals("cat", animal.getCat().getName());
    Assert.assertEquals("dog", animal.getDog().getName());
    Assert.assertEquals(2, animal.getBirds().size());
    Assert.assertEquals(2, animal.getUsers().size());
    Assert.assertEquals(2, animal.getCats().size());
    Assert.assertEquals(2, animal.getDogs().size());
  }
}
