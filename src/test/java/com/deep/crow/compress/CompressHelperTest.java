package com.deep.crow.compress;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class CompressHelperTest {

    @Test
    public void testRegister() {
        CompressHelper compressHelper = new CompressHelper();
        compressHelper.register(List.class, DefaultCompress.class);
        Map<Class<?>, Class<? extends Compress>> map = compressHelper.getMap();
        Class<? extends Compress> listCls = map.get(List.class);
        Class<? extends Compress> iterableCls = map.get(Iterable.class);
        Class<? extends Compress> mapCls = map.get(Map.class);
        Assert.assertNull(listCls);
        Assert.assertEquals(mapCls.getName(), "com.deep.crow.compress.MapCompress");
    }

    @Test
    public void testTopLevelCover() {
        CompressHelper compressHelper = new CompressHelper();
        compressHelper.topLevelCover(List.class, DefaultCompress.class);
        compressHelper.topLevelCover(String.class, DefaultCompress.class);
        compressHelper.topLevelCover(CharSequence.class, IteratorCompress.class);
        Map<Class<?>, Class<? extends Compress>> map = compressHelper.getMap();
        Class<? extends Compress> listCls = map.get(List.class);
        Class<? extends Compress> stringCls = map.get(Iterable.class);
        Class<? extends Compress> charSequenceCls = map.get(CharSequence.class);
        Assert.assertNull(listCls);
        Assert.assertEquals(stringCls.getName(), "com.deep.crow.compress.DefaultCompress");
        Assert.assertEquals(charSequenceCls.getName(), "com.deep.crow.compress.IteratorCompress");
    }

}