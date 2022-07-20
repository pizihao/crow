package com.deep.crow.completable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * <h2>Supplier的实现</h2>
 * <p>根据各个任务中执行的定义位置进行执行</p>
 * <p>具体的返回类型任交由子类定义</p>
 *
 * @deprecated {@link com.deep.crow.multi.Multi}
 * @author Create by liuwenhao on 2021/11/26 11:24
 */
@Deprecated
@SuppressWarnings("unused deprecated")
abstract class CofSupply<R> implements Cof<Supplier<Object>, R> {

    protected List<CofTask<Supplier<Object>>> supplies;

    protected ExecutorService executorService;

    protected CopyOnWriteArrayList<Signature<Object>> perform(Collection<CofTask<Supplier<Object>>> suppliers) {
        return suppliers.stream().map(r -> {
            if (Objects.nonNull(r.getExecutorService())) {
                return Signature.build(r.getName(), CompletableFuture.supplyAsync(r.getTask(), r.getExecutorService()));
            } else if (Objects.nonNull(executorService)) {
                return Signature.build(r.getName(), CompletableFuture.supplyAsync(r.getTask(), executorService));
            } else {
                return Signature.build(r.getName(), CompletableFuture.supplyAsync(r.getTask()));
            }
        }).collect(Collectors.toCollection(CopyOnWriteArrayList::new));
    }

    @Override
    public Cof<Supplier<Object>, R> register(CofTask<Supplier<Object>> e) {
        supplies.add(e);
        return this;
    }

    @Override
    public Cof<Supplier<Object>, R> registers(List<CofTask<Supplier<Object>>> e) {
        supplies.addAll(e);
        return this;
    }

}