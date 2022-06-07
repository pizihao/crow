package com.deep.crow.compress;

import junit.framework.TestCase;

import java.util.List;

public class CompressTest extends TestCase {

    public void testRegister() {
        CompressHelper compressHelper = new CompressHelper();

        compressHelper.register(List.class, DefaultCompress.class);
        System.out.println(compressHelper.getMap());
    }

    public void testTopLevelCover() {
        CompressHelper compressHelper = new CompressHelper();
        compressHelper.topLevelCover(List.class, DefaultCompress.class);
        compressHelper.topLevelCover(String.class,DefaultCompress.class);
        compressHelper.topLevelCover(CharSequence.class,IteratorCompress.class);
        System.out.println(compressHelper.getMap());
    }
}