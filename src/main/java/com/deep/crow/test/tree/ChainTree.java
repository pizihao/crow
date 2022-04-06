package com.deep.crow.test.tree;

/**
 * <h2>树，用于任务交汇，单向</h2>
 * and：多个都完成之后<br>
 * or：任意一个完成之后<br>
 * simple：串行化和并行化的交汇操作<br>
 *
 * @author Create by liuwenhao on 2022/4/6 14:11
 */
public interface ChainTree<T> {

    /**
     * <h2>向树中添加一个元素</h2>
     * 该元素会被放置在parent的子节点位置<br>
     *
     * @param t      元素
     * @param parent 上级
     * @return boolean
     * @author liuwenhao
     * @date 2022/4/6 20:26
     */
    boolean add(T t, T parent);

    /**
     * <h2>向树中添加一个元素</h2>
     * 一个特殊的元素，如果在运行过程中出现异常则执行，如果没有出现异常则放弃<br>
     * 独立于整个树，会在抛出异常后动态的拼接在后面<br>
     *
     * @param t 元素
     * @return boolean
     * @author liuwenhao
     * @date 2022/4/6 20:26
     */
    boolean addThrowable(T t);

    /**
     * <h2>获取某个元素的下一级</h2>
     * 下一级可能存在多个
     *
     * @param t 元素
     * @return T[]
     * @author liuwenhao
     * @date 2022/4/6 21:12
     */
    T[] nextNode(T t);

    /**
     * <h2>相对于整个树，返回某个元素的深度</h2>
     *
     * @param t 元素
     * @return int
     * @author liuwenhao
     * @date 2022/4/6 21:14
     */
    int nodeOrder(T t);

    /**
     * <h2>相对于整个树，返回某个元素同级的其他元素</h2>
     *
     * @param t 元素
     * @return T[]
     * @author liuwenhao
     * @date 2022/4/6 21:19
     */
    T[] identical(T t);

    /**
     * <h2>对内部的节点进行排序</h2>
     *
     * @author liuwenhao
     * @date 2022/4/6 19:53
     */
    void sort();

    /**
     * <h2>检查</h2>
     * 对整个树中的顺序进行对比，保证多个节点的顺序性
     *
     * @return boolean
     * @author liuwenhao
     * @date 2022/4/6 20:10
     */
    boolean check();

}