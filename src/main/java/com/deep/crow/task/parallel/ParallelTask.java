package com.deep.crow.task.parallel;

import com.deep.crow.multi.Multi;
import com.deep.crow.task.Task;

import java.util.Objects;

/**
 * <h2>并行任务</h2>
 *
 * @author Create by liuwenhao on 2022/4/11 11:38
 */
interface ParallelTask extends Task, Comparable<ParallelTask> {

    /**
     * <h2>顺序</h2>
     * 表示获取结果的顺序<br>
     * order越大的数据表示其被获取的优先度越低
     *
     * @return long
     * @author liuwenhao
     * @date 2022/6/10 10:49
     */
    long order();

    /**
     * <h2>将任务装配到Multi中</h2>
     * 直接创建新的Multi并返回
     *
     * @return Multi
     * @author liuwenhao
     * @date 2022/4/11 11:41
     */
    <U> Multi<U> assembling();

    @Override
    default int compareTo(ParallelTask o) {
        if (Objects.isNull(o)) {
            return -1;
        }
        return (int) (o.order() - order());
    }
}