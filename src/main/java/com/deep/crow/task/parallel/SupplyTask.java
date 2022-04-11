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
public class SupplyTask<T> implements ParallelTask {

    Supplier<T> supplier;

    public SupplyTask(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> Multi<U> assembling(ExecutorService executorService) {
        return (Multi<U>) MultiHelper.supplyAsync(executorService, supplier);
    }
}