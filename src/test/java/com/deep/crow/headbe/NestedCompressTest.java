package com.deep.crow.headbe;

import junit.framework.TestCase;

import java.util.List;

public class NestedCompressTest extends TestCase {

    public void testRegister() {
        CompressHelper nestedTypeHelper = new CompressHelper();

        nestedTypeHelper.register(List.class, DefaultCompress.class);
        System.out.println(nestedTypeHelper.getMap());
    }
}