package com.deep.crow.task.mix;

import com.deep.crow.multi.Multi;
import com.deep.crow.task.Task;

import java.lang.annotation.Inherited;
import java.util.Set;

/**
 * <h2>混合任务节点</h2>
 *
 * @author Create by liuwenhao on 2022/6/17 19:07
 */
interface MixTask<T> extends Task {

    /**
     * <h2>获取任务的前置任务的标识</h2>
     *
     * @return Set<String>
     * @author liuwenhao
     * @date 2022/6/17 19:10
     */
    Set<String> pre();

    /**
     * <h2>添加一个前置任务</h2>
     *
     * @param preName 前置任务标识
     * @author liuwenhao
     * @date 2022/6/18 16:58
     */
    void addPreName(String preName);

    /**
     * <h2>删除前置任务</h2>
     *
     * @param preName 前置任务标识
     * @author liuwenhao
     * @date 2022/6/18 16:59
     */
    void removePreName(String preName);

    /**
     * <h2>是否是尾结点</h2>
     *
     * @return boolean
     * @author liuwenhao
     * @date 2022/6/18 17:10
     */
    @Deprecated
    boolean isTail();

    /**
     * <h2>声明当前节点不再是尾结点</h2>
     *
     * @author liuwenhao
     * @date 2022/6/18 17:11
     */
    void cancelTail();

    /**
     * <h2>获取任务名称</h2>
     *
     * @return java.lang.String
     * @author liuwenhao
     * @date 2022/6/18 15:58
     */
    String name();

    /**
     * <h2>判断任务是否完成</h2>
     * 如果强制则使用join 如果非强制则使用getNow
     *
     * @param force 是否强制
     * @return T
     * @author liuwenhao
     * @date 2022/6/17 19:10
     */
    boolean complete(boolean force);

    /**
     * <h2>获得任务</h2>
     *
     * @return com.deep.crow.multi.Multi<T>
     * @author liuwenhao
     * @date 2022/6/22 18:00
     */
    Multi<T> multi();

}