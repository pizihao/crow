package com.deep.crow.serial;

import com.deep.crow.multi.Multi;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <h2>对接{@link Supplier}</h2>
 *
 * @author Create by liuwenhao on 2022/4/11 16:12
 */
class SupplierTask<T, R> implements SerialTask<T> {

    Supplier<? super R> supplier;

    public SupplierTask(Supplier<? super R> supplier) {
        this.supplier = supplier;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> Multi<U> increase(Multi<T> multi) {
        Function<T, ? super R> function = t -> (R) supplier.get();
        return (Multi<U>) multi.thenApply(function);
    }

}