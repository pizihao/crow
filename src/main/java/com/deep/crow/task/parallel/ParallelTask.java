package com.deep.crow.task.parallel;

import com.deep.crow.multi.Multi;

import java.util.concurrent.ExecutorService;

/**
 * <h2>并行任务</h2>
 *
 * @author Create by liuwenhao on 2022/4/11 11:38
 */
public interface ParallelTask {

    /**
     * <h2>将任务装配到Multi中</h2>
     * 直接创建新的Multi并返回
     *
     * @param executorService 线程池
     * @return Multi
     * @author liuwenhao
     * @date 2022/4/11 11:41
     */
    <U> Multi<U> assembling(ExecutorService executorService);

}