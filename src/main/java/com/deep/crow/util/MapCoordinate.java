package com.deep.crow.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * <h2>通过map接口实现坐标系</h2>
 *
 * @author Create by liuwenhao on 2022/4/13 18:39
 */
public class MapCoordinate<T> implements Coordinate<T> {

    /**
     * 坐标系 [x,[y,T]]
     */
    Map<Integer, Map<Integer, T>> map;


    @Override
    public boolean contains(int x, int y) {
        return containsX(x) && containsY(y);
    }

    @Override
    public boolean containsX(int x) {
        return map.containsKey(x);
    }

    @Override
    public boolean containsY(int y) {
        Collection<Map<Integer, T>> yMapList = map.values();
        Set<Integer> yKey = yMapList.stream()
            .map(Map::keySet)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
        return yKey.contains(y);
    }

    @Override
    public boolean containsValue(T value) {
        for (Map<Integer, T> y : map.values()) {
            if (y.containsValue(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public T get(int x, int y) {
        Map<Integer, T> yMap = map.get(x);
        return yMap == null ? null : yMap.get(y);
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public int size() {
        int size = 0;
        for (Map<Integer, T> map : map.values()) {
            size += map.size();
        }
        return size;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public T put(int x, int y, T value) {
        return null;
    }

    @Override
    public void putAll(Coordinate<T> coordinate) {

    }

    @Override
    public T remove(int x, int y) {
        return null;
    }

    @Override
    public Map<Integer, T> x(int y) {
        return null;
    }

    @Override
    public Map<Integer, T> y(int x) {
        return null;
    }

    @Override
    public Set<Element<T>> elementSet() {
        return null;
    }

    @Override
    public Set<T> xSet() {
        return null;
    }

    @Override
    public Set<T> ySet() {
        Collection<Map<Integer, T>> yMapList = map.values();
        return yMapList.stream()
            .map(Map::values)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
    }

    @Override
    public Collection<T> values() {
        Collection<Map<Integer, T>> yMapList = map.values();
        return yMapList.stream()
            .map(Map::values)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    @Override
    public Map<Integer, Map<Integer, T>> xMap() {
        return null;
    }

    @Override
    public Map<Integer, Map<Integer, T>> yMap() {
        return null;
    }

}