package com.deep.crow.model;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/4/28 11:35
 */
public class Dog {

    private String name;

    public Dog(String name) {
        this.name = name;
    }

    public Dog() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Dog{" +
            "name='" + name + '\'' +
            '}';
    }
}