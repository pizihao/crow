package com.deep.crow.model;

import java.util.List;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/5/9 10:21
 */
public class Basic {

    List<String> strings;
    List<Integer> integers;
    List<Double> doubles;
    List<Float> floats;
    List<Short> shorts;
    List<Byte> bytes;
    List<Long> longs;
    List<Character> characters;
    List<Boolean> booleans;

    public List<String> getStrings() {
        return strings;
    }

    public void setStrings(List<String> strings) {
        this.strings = strings;
    }

    public List<Integer> getIntegers() {
        return integers;
    }

    public void setIntegers(List<Integer> integers) {
        this.integers = integers;
    }

    public List<Double> getDoubles() {
        return doubles;
    }

    public void setDoubles(List<Double> doubles) {
        this.doubles = doubles;
    }

    public List<Float> getFloats() {
        return floats;
    }

    public void setFloats(List<Float> floats) {
        this.floats = floats;
    }

    public List<Short> getShorts() {
        return shorts;
    }

    public void setShorts(List<Short> shorts) {
        this.shorts = shorts;
    }

    public List<Byte> getBytes() {
        return bytes;
    }

    public void setBytes(List<Byte> bytes) {
        this.bytes = bytes;
    }

    public List<Long> getLongs() {
        return longs;
    }

    public void setLongs(List<Long> longs) {
        this.longs = longs;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }

    public List<Boolean> getBooleans() {
        return booleans;
    }

    public void setBooleans(List<Boolean> booleans) {
        this.booleans = booleans;
    }

    @Override
    public String toString() {
        return "Basic{" +
            "strings=" + strings +
            ", integers=" + integers +
            ", doubles=" + doubles +
            ", floats=" + floats +
            ", shorts=" + shorts +
            ", bytes=" + bytes +
            ", longs=" + longs +
            ", characters=" + characters +
            ", booleans=" + booleans +
            '}';
    }
}