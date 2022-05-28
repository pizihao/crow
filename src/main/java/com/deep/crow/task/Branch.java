package com.deep.crow.task;

import com.deep.crow.exception.CrowException;
import com.deep.crow.multi.Multi;

import java.util.Arrays;
import java.util.function.Function;

/**
 * <h2>任务的分支行为</h2>
 * 概括任务由一个变为两个的的行为，形如：A --> B,A --> C 的操作<br>
 * 有以下规则：
 * <ol>
 *     <li>A是B和C的前置任务</li>
 *     <li>A经过一个步长到达B和C</li>
 *     <li>B和C是相互独立的并行任务</li>
 * </ol>
 * 任务如果出现了分支，那么必定是转化成了并行化任务
 *
 * @author Create by liuwenhao on 2022/5/28 13:29
 */
public class Branch {

    /**
     * 分支行为的主导者
     */
    Multi<?> preMulti;

    /**
     * 分支行为的产物
     */
    Multi<?>[] children;

    /**
     * 记录已经装入并行过程的任务
     */
    long sign;

    /**
     * 大小，已存在的任务数
     */
    int size;

    /**
     * 扩容指数，当前大小高于某个值时需要对其进行扩容。
     * 这个值为 children.length * capacityAble
     */
    double capacityAble;

    /**
     * 默认容量
     */
    static final int DEFAULT_CAPACITY = 10;

    /**
     * 默认扩容指数
     */
    static final double DEFAULT_CAPACITY_ABLE = 0.8;

    /**
     * <h2>为preMulti添加一个分支</h2>
     * 请避免一个Multi有太多的分支，每个分支都会消耗一个线程去维护，这会大大占用系统资源
     *
     * @param fn 任务
     * @return boolean
     * @author liuwenhao
     * @date 2022/5/28 14:47
     */
    public <T, R> boolean add(Function<Multi<T>, Multi<R>> fn) {
        if (checkCapacity()) {
            expansion(children.length + 1);
        }
        Multi<R> rMulti = fn.apply((Multi<T>) preMulti);
        return true;
    }

    /**
     * <h2>检查是否需要扩容</h2>
     * true:需要扩容
     * false:不需要扩容
     *
     * @return boolean
     * @author liuwenhao
     * @date 2022/5/28 17:50
     */
    private boolean checkCapacity() {
        return size >= children.length * capacityAble;
    }

    /**
     * <h2>扩容</h2>
     * 将children进行扩容，新容量与现有容量的关系是： (capacityAble + 1) * children.length
     * 的到的值会忽略小数点之后的部分
     *
     * @param min 本次扩容允许的最小值
     * @author liuwenhao
     * @date 2022/5/28 18:12
     */
    private void expansion(int min) {
        int capacity = children.length;
        int newCapacity = (int) (capacity * (capacityAble + 1));
        if (newCapacity - min < 0)
            newCapacity = min;
        if (newCapacity - Integer.MAX_VALUE > 0) {
            throw new OutOfMemoryError();
        }
        children = Arrays.copyOf(children, newCapacity);
    }


}