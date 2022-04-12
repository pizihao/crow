package com.deep.crow.task.serial;

import com.deep.crow.multi.Multi;

import java.util.function.Consumer;

/**
 * <h2>对接{@link Consumer}</h2>
 *
 * @author Create by liuwenhao on 2022/4/11 11:12
 */
class ConsumerTask<T> implements SerialTask<T> {

    Consumer<T> action;

    public ConsumerTask(Consumer<T> action) {
        this.action = action;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> Multi<U> increase(Multi<T> multi) {
        return (Multi<U>) multi.thenAccept(action);
    }
}