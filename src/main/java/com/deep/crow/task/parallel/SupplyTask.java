package com.deep.crow.task.parallel;

import com.deep.crow.multi.Multi;
import com.deep.crow.multi.MultiHelper;

import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

/**
 * <h2>异步计算</h2>
 *
 * @author Create by liuwenhao on 2022/4/11 11:48
 */
class SupplyTask<T> implements ParallelTask {

    int order;
    Supplier<T> supplier;
    ExecutorService executorService;

    public SupplyTask(int order, Supplier<T> supplier, ExecutorService executorService) {
        this.order = order;
        this.supplier = supplier;
        this.executorService = executorService;
    }

    public SupplyTask(Supplier<T> supplier, ExecutorService executorService) {
        this.supplier = supplier;
        this.executorService = executorService;
    }

    @Override
    public int order() {
        return order;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> Multi<U> assembling() {
        return (Multi<U>) MultiHelper.supplyAsync(executorService, supplier);
    }
}