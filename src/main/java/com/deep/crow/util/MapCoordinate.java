package com.deep.crow.util;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <h2>通过map接口实现坐标系</h2>
 *
 * @author Create by liuwenhao on 2022/4/13 18:39
 */
public class MapCoordinate<T> implements Coordinate<T> {

    /**
     * 坐标系 [x,[y,T]]
     */
    Map<Integer, Map<Integer, T>> coordinate;

    private MapCoordinate(Map<Integer, Map<Integer, T>> coordinate) {
        this.coordinate = coordinate;
    }

    public static <T> MapCoordinate<T> create() {
        Map<Integer, Map<Integer, T>> map = new HashMap<>();
        return new MapCoordinate<>(map);
    }

    public static <T> MapCoordinate<T> create(Map<Integer, Map<Integer, T>> coordinate) {
        return new MapCoordinate<>(coordinate);
    }

    @Override
    public boolean contains(int x, int y) {
        return containsX(x) && containsY(y);
    }

    @Override
    public boolean containsX(int x) {
        return xMap().containsKey(x);
    }

    @Override
    public boolean containsY(int y) {
        return yMap().containsKey(y);
    }

    @Override
    public boolean containsValue(T value) {
        for (Map<Integer, T> y : xMap().values()) {
            if (y.containsValue(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public T get(int x, int y) {
        Map<Integer, T> map = xMap().get(x);
        return map == null ? null : map.get(y);
    }

    @Override
    public boolean isEmpty() {
        return xMap().isEmpty();
    }

    @Override
    public int size() {
        int size = 0;
        for (Map<Integer, T> map : xMap().values()) {
            size += map.size();
        }
        return size;
    }

    @Override
    public void clear() {
        coordinate.clear();
    }

    @Override
    public T put(int x, int y, T value) {
        Map<Integer, T> map = coordinate.get(x);
        if (map == null) {
            map = new HashMap<>();
        }
        T t = map.put(y, value);
        coordinate.put(x, map);
        return t;
    }

    @Override
    public void putAll(Coordinate<T> coordinate) {
        for (Coordinate.Element<T> element : coordinate.elementSet()) {
            put(element.getX(), element.getY(), element.getValue());
        }
    }

    @Override
    public T remove(int x, int y) {
        Map<Integer, T> map = coordinate.get(x);
        return map.remove(y);
    }

    @Override
    public Map<Integer, T> x(int y) {
        Map<Integer, T> map = new HashMap<>(16);
        this.coordinate.forEach((m, yAxis) -> yAxis.forEach((n, t) -> {
            if (n == y) {
                map.put(m, t);
            }
        }));
        return map;
    }

    @Override
    public Map<Integer, T> y(int x) {
        Map<Integer, T> map = new HashMap<>(16);
        this.coordinate.get(x).forEach(map::put);
        return map;
    }

    @Override
    public Set<Element<T>> elementSet() {
        Set<Element<T>> elements = new HashSet<>();

        this.coordinate.forEach((m, yAxis) -> yAxis.forEach((n, t) -> {
            Element<T> element = new MapElement<>(m, n, t);
            elements.add(element);
        }));
        return elements;
    }

    @Override
    public Set<Integer> xSet() {
        return xMap().keySet();
    }

    @Override
    public Set<Integer> ySet() {
        return yMap().keySet();
    }

    @Override
    public Collection<T> values() {
        Collection<Map<Integer, T>> yMapList = xMap().values();
        return yMapList.stream()
            .map(Map::values)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    @Override
    public Map<Integer, Map<Integer, T>> xMap() {
        MapCoordinate<T> mapCoordinate = MapCoordinate.create();
        mapCoordinate.putAll(this);
        return mapCoordinate.coordinate;
    }

    @Override
    public Map<Integer, Map<Integer, T>> yMap() {
        Coordinate<T> yMap = MapCoordinate.create();
        this.coordinate.forEach((m, yAxis) -> yAxis.forEach((n, t) -> yMap.put(n, m, t)));
        return yMap.xMap();
    }

    static class MapElement<E> implements Element<E> {
        int x;
        int y;
        E value;

        public MapElement(int x, int y, E value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }

        @Override
        public E getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            MapElement<?> that = (MapElement<?>) o;
            return x == that.x && y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")" + "-->" + value;
        }
    }

}