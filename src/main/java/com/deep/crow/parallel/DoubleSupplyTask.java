package com.deep.crow.parallel;

import com.deep.crow.multi.Multi;
import com.deep.crow.multi.MultiHelper;

import java.util.concurrent.ExecutorService;
import java.util.function.DoubleSupplier;

/**
 * <h2>可以得到一个double的异步计算</h2>
 *
 * @author Create by liuwenhao on 2022/4/20 19:34
 */
class DoubleSupplyTask implements ParallelTask {

    DoubleSupplier doubleSupplier;

    ExecutorService executorService;

    public DoubleSupplyTask(DoubleSupplier doubleSupplier, ExecutorService executorService) {
        this.doubleSupplier = doubleSupplier;
        this.executorService = executorService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> Multi<U> assembling() {
        return (Multi<U>) MultiHelper.supplyAsync(executorService, () -> doubleSupplier.getAsDouble());
    }
}