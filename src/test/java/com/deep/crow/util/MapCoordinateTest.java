package com.deep.crow.util;

import com.deep.crow.model.User;

import java.util.Set;

/**
 * <h2>test</h2>
 *
 * @author Create by liuwenhao on 2022/4/2 16:08
 */
public class MapCoordinateTest {

    public static void main(String[] args) {
        MapCoordinate<User> coordinate = MapCoordinate.create();

        coordinate.put(1, 1, new User("11"));
        coordinate.put(1, 2, new User("12"));
        coordinate.put(1, 3, new User("13"));
        coordinate.put(5, 3, new User("53"));

//        coordinate.xMap().forEach((x, yMap) -> yMap.forEach((y, user) -> System.out.println(StrUtil.format("({},{}) : {}", x, y, user.getName()))));

        Set<Coordinate.Element<User>> elements = coordinate.elementSet();
        elements.add(new MapCoordinate.MapElement<>(10, 12, new User("456")));

        elements.forEach(System.out::println);
        // (1,1)-->User{name='11'}
        // (1,2)-->User{name='12'}
        // (1,3)-->User{name='13'}
        // 10,12)-->User{name='456'}
        // (5,3)-->User{name='53'}
        System.out.println("===============");
        coordinate.elementSet().forEach(System.out::println);
        // (1,1)-->User{name='11'}
        // (1,2)-->User{name='12'}
        // (1,3)-->User{name='13'}
        // (5,3)-->User{name='53'}


        User put = coordinate.put(10, 10, new User("456"));
        coordinate.yMap().forEach((x, yMap) -> yMap.forEach((y, user) -> System.out.println(StrUtil.format("({},{}) : {}", x, y, user.getName()))));

    }
}