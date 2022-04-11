package com.deep.crow.task.parallel;

import com.deep.crow.multi.Multi;
import com.deep.crow.multi.MultiHelper;

import java.util.concurrent.ExecutorService;

/**
 * <h2>异步任务</h2>
 *
 * @author Create by liuwenhao on 2022/4/11 11:44
 */
public class RunTask implements ParallelTask {

    Runnable runnable;

    public RunTask(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> Multi<U> assembling(ExecutorService executorService) {
        return (Multi<U>) MultiHelper.runAsync(executorService, runnable);
    }
}