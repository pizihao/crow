package com.deep.crow.multi;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;

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

    protected MultiImpl(ExecutorService executorService, Supplier<T> supplier) {
        this(executorService, CompletableFuture.supplyAsync(supplier, executorService));
    }

    protected MultiImpl(ExecutorService executorService, Runnable runnable) {
        this(executorService, (CompletableFuture<T>) CompletableFuture.runAsync(runnable, executorService));
    }

    protected MultiImpl(ExecutorService executorService) {
        this(executorService, CompletableFuture.supplyAsync(() -> null, executorService));
    }

    @Override
    public CompletableFuture<T> getCpf() {
        return completableFuture;
    }

    @Override
    public T join() {
        return completableFuture.join();
    }

    @Override
    public T get() throws ExecutionException, InterruptedException {
        return completableFuture.get();
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return completableFuture.get(timeout, unit);
    }

    @Override
    public T getNow() {
        return completableFuture.getNow(null);
    }

    @Override
    public T getNow(T valueIfAbsent) {
        return completableFuture.getNow(valueIfAbsent);
    }

    @Override
    public Multi<T> copyMulti() {
        CompletableFuture<T> async = CompletableFuture.supplyAsync(() -> completableFuture.join(), executorService);
        return new MultiImpl<>(executorService, async);
    }

    @Override
    public <U> Multi<U> thenApply(Function<? super T, ? extends U> fn) {
        completableFuture = (CompletableFuture<T>) completableFuture.thenApplyAsync(fn, executorService);
        return (Multi<U>) new MultiImpl<>(executorService, completableFuture);
    }

    @Override
    public Multi<Void> thenAccept(Consumer<? super T> action) {
        completableFuture = (CompletableFuture<T>) completableFuture.thenAcceptAsync(action, executorService);
        return (Multi<Void>) new MultiImpl<>(executorService, completableFuture);
    }


    @Override
    public Multi<Void> thenRun(Runnable action) {
        completableFuture = (CompletableFuture<T>) completableFuture.thenRunAsync(action, executorService);
        return (Multi<Void>) new MultiImpl<>(executorService, completableFuture);
    }

    @Override
    public <U, V> Multi<V> thenCombine(Multi<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn) {
        completableFuture = (CompletableFuture<T>) completableFuture.thenCombineAsync(other.getCpf(), fn, executorService);
        return (Multi<V>) this;
    }

    @Override
    public <U> Multi<Void> thenBiAccept(Multi<? extends U> other, BiConsumer<? super T, ? super U> action) {
        completableFuture = (CompletableFuture<T>) completableFuture.thenAcceptBothAsync(other.getCpf(), action, executorService);
        return (Multi<Void>) new MultiImpl<>(executorService, completableFuture);
    }

    @Override
    public Multi<Void> runRunBoth(Multi<?> other, Runnable action) {
        completableFuture = (CompletableFuture<T>) completableFuture.runAfterBothAsync(other.getCpf(), action, executorService);
        return (Multi<Void>) new MultiImpl<>(executorService, completableFuture);
    }

    @Override
    public <U, M extends T> Multi<U> applyFun(Multi<M> other, Function<M, U> fn) {
        completableFuture.join();
        completableFuture = (CompletableFuture<T>) completableFuture.thenApplyAsync(r -> fn.apply(other.join()), executorService);
        return (Multi<U>) new MultiImpl<>(executorService, completableFuture);
    }

    @Override
    public <M extends T> Multi<Void> acceptFun(Multi<M> other, Consumer<M> action) {
        completableFuture.join();
        completableFuture = (CompletableFuture<T>) completableFuture.thenAcceptAsync(r -> action.accept(other.join()), executorService);
        return (Multi<Void>) new MultiImpl<>(executorService, completableFuture);
    }

    @Override
    public Multi<Void> runFun(Multi<?> other, Runnable action) {
        completableFuture = (CompletableFuture<T>) completableFuture.runAfterEitherAsync(other.getCpf(), action, executorService);
        return (Multi<Void>) new MultiImpl<>(executorService, completableFuture);
    }

    @Override
    public <U> Multi<U> thenCompose(Function<? super T, ? extends Multi<U>> fn) {
        AtomicReference<Multi<U>> uMulti = new AtomicReference<>((Multi<U>) this);
        completableFuture = (CompletableFuture<T>) completableFuture.thenApplyAsync(s -> {
            Multi<U> multi = fn.apply(s);
            uMulti.set(multi);
            return completableFuture;
        }, executorService);
        return uMulti.get();
    }

    @Override
    public Multi<T> exceptionally(Function<Throwable, ? extends T> fn) {
        completableFuture = completableFuture.exceptionally(fn);
        return new MultiImpl<>(executorService, completableFuture);
    }

    @Override
    public Multi<T> whenComplete(BiConsumer<? super T, ? super Throwable> action) {
        completableFuture = completableFuture.whenCompleteAsync(action, executorService);
        return new MultiImpl<>(executorService, completableFuture);
    }

    @Override
    public <U> Multi<U> handle(BiFunction<? super T, Throwable, ? extends U> fn) {
        completableFuture = (CompletableFuture<T>) completableFuture.handleAsync(fn, executorService);
        return (Multi<U>) new MultiImpl<>(executorService, completableFuture);
    }

}