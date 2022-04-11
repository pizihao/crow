package com.deep.crow.task.serial;

import com.deep.crow.multi.Multi;

/**
 * <h2>对接{@link Runnable}</h2>
 *
 * @author Create by liuwenhao on 2022/4/11 10:44
 */
public class RunnableTask<T> implements SerialTask<T> {

    Runnable runnable;

    public RunnableTask(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> Multi<U> increase(Multi<T> multi) {
        return (Multi<U>) multi.thenRun(runnable);
    }

}