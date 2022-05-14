package com.deep.crow.type;

import com.deep.crow.model.User;

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

        Type build = TypeBuilder.make(Map.class)
            .add(String.class)
            .add(Integer.class)
            .build();
        System.out.println(build);

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
    }

}