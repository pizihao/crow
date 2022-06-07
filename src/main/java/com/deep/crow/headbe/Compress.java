package com.deep.crow.headbe;

/**
 * <h2>参数化类型处理，即存在泛型的类型</h2>
 *
 * @author Create by liuwenhao on 2022/6/2 13:31
 */
interface Compress {

    /**
     * <h2>压缩</h2>
     * 这是一个压缩对象的过程，即有一个可以完全兼容的对象来代表一个大的对象
     * 例如对于集合对象：就是将其中的一个拿出来<br>
     *
     * @return T
     * @author liuwenhao
     * @date 2022/6/2 14:27
     */
    <T> T compress();

    /**
     * <h2>检查</h2>
     *
     * @param o 需要验证的对象
     * @return boolean
     * @author liuwenhao
     * @date 2022/6/2 14:27
     */
    boolean check();


}