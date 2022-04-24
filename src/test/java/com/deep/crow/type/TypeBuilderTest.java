package com.deep.crow.type;

import com.deep.crow.User;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/4/24 12:49
 */
public class TypeBuilderTest {

    public static void main(String[] args) {
        Type list = TypeBuilder.make(List.class)
            .add(User.class)
            .build();
        System.out.println(list.getTypeName());

        Type map = TypeBuilder.make(Map.class)
            .add(Integer.class)
            .add(User.class)
            .build();
        System.out.println(map.getTypeName());


        Type list0 = TypeBuilder.make(Map.class)
            .add(Integer.class)
            .nested(List.class)
            .add(Integer.class)
            .parent()
            .build();
        System.out.println(list0.getTypeName());

        Type list1 = TypeBuilder.make(User.class)
            .build();
        System.out.println(list1.getTypeName());

        Type list2 = TypeBuilder.make(List.class)
            .add(Integer.class)
            .add(User.class)
            .build();
        System.out.println(list2.getTypeName());

    }

}