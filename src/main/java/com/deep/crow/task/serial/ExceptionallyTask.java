package com.deep.crow.task.serial;

import com.deep.crow.multi.Multi;

import java.util.function.Function;

/**
 * <h2>异常任务</h2>
 *
 * @author Create by liuwenhao on 2022/4/11 11:35
 */
class ExceptionallyTask<T> implements SerialTask<T> {

    Function<Throwable, T> function;

    public ExceptionallyTask(Function<Throwable, T> function) {
        this.function = function;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> Multi<U> increase(Multi<T> multi) {
        return (Multi<U>) multi.exceptionally(function);
    }

}