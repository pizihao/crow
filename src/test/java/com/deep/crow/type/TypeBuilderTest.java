package com.deep.crow.type;

import com.deep.crow.model.User;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;

/** @author Create by liuwenhao on 2022/4/24 12:49 */
public class TypeBuilderTest extends TestCase {

  public void testBuilder() {
    Type list = TypeBuilder.make(List.class).addArgs(User.class).build();
    System.out.println(list.getTypeName());

    Type build = TypeBuilder.make(Map.class).addArgs(String.class).addArgs(Integer.class).build();
    System.out.println(build);

    Type map = TypeBuilder.make(Map.class).addArgs(Integer.class).addArgs(User.class).build();
    System.out.println(map.getTypeName());
    Type list0 =
        TypeBuilder.make(Map.class)
            .addArgs(Integer.class)
            .nested(List.class)
            .addArgs(Integer.class)
            .parent()
            .build();
    System.out.println(list0.getTypeName());
    Type list1 = TypeBuilder.make(User.class).build();
    System.out.println(list1.getTypeName());
  }
}
