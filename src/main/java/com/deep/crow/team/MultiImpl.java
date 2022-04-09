package com.deep.crow.team;

import cn.hutool.core.lang.Tuple;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * @author Create by liuwenhao on 2022/4/9 21:55
 */
@SuppressWarnings("unchecked unused")
public class MultiImpl<T> implements Multi<T> {

    CompletableFuture<T> completableFuture;

    ExecutorService executorService;

    private MultiImpl(ExecutorService executorService,
                      CompletableFuture<T> completableFuture) {
        this.executorService = executorService;
        this.completableFuture = completableFuture;
    }

    @Override
    public CompletableFuture<T> getCpf() {
        return completableFuture;
    }

    @Override
    public T get() {
        return completableFuture.join();
    }

    @Override
    public <U> Multi<U> thenApply(Function<? super T, ? extends U> fn) {
        completableFuture.thenApplyAsync(fn, executorService);
        return (Multi<U>) this;
    }

    @Override
    public Multi<Void> thenAccept(Consumer<? super T> action) {
        completableFuture.thenAcceptAsync(action, executorService);
        return (Multi<Void>) this;
    }


    @Override
    public Multi<Void> thenRun(Runnable action) {
        completableFuture.thenRunAsync(action, executorService);
        return (Multi<Void>) this;
    }

    @Override
    public <U, V> Multi<V> thenCombine(Multi<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn) {
        completableFuture.thenCombineAsync(other.getCpf(), fn, executorService);
        return (Multi<V>) this;
    }

    @Override
    public <U> Multi<Void> thenBiAccept(Multi<? extends U> other, BiConsumer<? super T, ? super U> action) {
        completableFuture.thenAcceptBothAsync(other.getCpf(), action, executorService);
        return (Multi<Void>) this;
    }

    @Override
    public Multi<Void> runRunBoth(Multi<?> other, Runnable action) {
        completableFuture.runAfterBothAsync(other.getCpf(), action, executorService);
        return (Multi<Void>) this;
    }

    @Override
    public <U> Multi<U> applyFun(Multi<? extends T> other, Function<? super T, U> fn) {
        completableFuture.applyToEitherAsync(other.getCpf(), fn, executorService);
        return (Multi<U>) this;
    }

    @Override
    public Multi<Void> acceptFun(Multi<? extends T> other, Consumer<? super T> action) {
        completableFuture.acceptEitherAsync(other.getCpf(), action, executorService);
        return (Multi<Void>) this;
    }

    @Override
    public Multi<Void> runFun(Multi<?> other, Runnable action) {
        completableFuture.runAfterEitherAsync(other.getCpf(), action, executorService);
        return (Multi<Void>) this;
    }

    @Override
    public <U> Multi<U> thenCompose(Function<? super T, ? extends Multi<U>> fn) {
        AtomicReference<Multi<U>> uMulti = new AtomicReference<>((Multi<U>) this);
        executorService.execute(() -> completableFuture.thenApply(s -> {
            Multi<U> multi = fn.apply(s);
            uMulti.set(multi);
            return multi;
        }));
        return uMulti.get();
    }

    @Override
    public Multi<T> exceptionally(Function<Throwable, ? extends T> fn) {
        completableFuture.exceptionally(fn);
        return this;
    }

    @Override
    public Multi<T> whenComplete(BiConsumer<? super T, ? super Throwable> action) {
        completableFuture.whenCompleteAsync(action, executorService);
        return this;
    }

    @Override
    public <U> Multi<U> handle(BiFunction<? super T, Throwable, ? extends U> fn) {
        completableFuture.handleAsync(fn, executorService);
        return (Multi<U>) this;
    }

    @Override
    public List<Object> multipleList(List<Multi<?>> c) {
        return c.stream().map(Multi::get).collect(Collectors.toList());
    }

    @Override
    public Tuple multipleTuple(List<Multi<?>> c) {
        List<Object> resultList = multipleList(c);
        Object[] resultArray = resultList.toArray();
        return new Tuple(resultArray);
    }

    public static <U> Multi<U> supplyAsync(ExecutorService executorService, Supplier<U> supplier) {
        CompletableFuture<U> async = CompletableFuture.supplyAsync(supplier, executorService);
        return new MultiImpl<>(executorService, async);
    }

    public static Multi<Void> runAsync(ExecutorService executorService, Runnable runnable) {
        CompletableFuture<Void> async = CompletableFuture.runAsync(runnable, executorService);
        return new MultiImpl<>(executorService, async);
    }

}