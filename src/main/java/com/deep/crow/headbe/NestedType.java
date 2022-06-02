package com.deep.crow.headbe;

/**
 * <h2>参数化类型处理，即存在泛型的类型</h2>
 *
 * @author Create by liuwenhao on 2022/6/2 13:31
 */
interface NestedType {

    /**
     * <h2>拆分</h2>
     * 目的是将由多个相同元素组成的一个集合体中的一个拿出来<br>
     * 如果不存在子元素则返回原对象
     *
     * @return T
     * @author liuwenhao
     * @date 2022/6/2 14:27
     */
    <T> T split();

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