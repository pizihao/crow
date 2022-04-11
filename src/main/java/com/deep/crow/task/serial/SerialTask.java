package com.deep.crow.task.serial;

import com.deep.crow.multi.Multi;


/**
 * <h2>串行化任务</h2>
 * T 代表源任务的结果类型
 *
 * @author Create by liuwenhao on 2022/4/11 10:03
 */
public interface SerialTask<T> {

    /**
     * <h2>追加一个任务</h2>
     *
     * @param <U>   依赖任务的返回值类型
     * @param multi 一个multi实例
     * @return Multi<U>
     * @author Created by liuwenhao on 2022/4/10 17:23
     */
    <U> Multi<U> increase(Multi<T> multi);


}