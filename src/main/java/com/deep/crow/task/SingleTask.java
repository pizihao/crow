package com.deep.crow.task;


import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Create by liuwenhao on 2022/4/10 22:19
 */
public class SingleTask<T, R> implements Task {

    Function<T, R> function;

    public SingleTask(Function<T, R> function) {
        this.function = function;
    }

    @Override
    public T result() {
        return null;
    }

}