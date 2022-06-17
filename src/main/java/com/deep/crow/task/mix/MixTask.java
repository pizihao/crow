package com.deep.crow.task.mix;

import com.deep.crow.task.Task;

/**
 * <h2>混合任务节点</h2>
 *
 * @author Create by liuwenhao on 2022/6/17 19:07
 */
public interface MixTask<T> extends Task {

    /**
     * <h2>获取任务的开始节点</h2>
     * 开始节点的责任就是确定整个任务的头，无其他意义
     *
     * @return com.deep.crow.task.mix.MixTask
     * @author liuwenhao
     * @date 2022/6/17 19:10
     */
    MixTask<Void> head();

    /**
     * <h2>获取任务的后置任务</h2>
     * 后置任务可能不只有一个
     *
     * @return com.deep.crow.task.mix.MixTask
     * @author liuwenhao
     * @date 2022/6/17 19:10
     */
    MixTask<Object>[] next();

    /**
     * <h2>执行当前的任务</h2>
     *
     * @return T
     * @author liuwenhao
     * @date 2022/6/17 19:10
     */
    T exec();
}