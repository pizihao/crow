package com.deep.crow.headbe;

import junit.framework.TestCase;

import java.util.List;

public class NestedTypeHelperTest extends TestCase {

    public void testRegister() {
        NestedTypeHelper nestedTypeHelper = new NestedTypeHelper();

        nestedTypeHelper.register(List.class,DefaultType.class);
        System.out.println(nestedTypeHelper.getMap());
    }
}