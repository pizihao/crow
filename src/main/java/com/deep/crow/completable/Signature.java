package com.deep.crow.completable;

import java.util.concurrent.CompletableFuture;

/**
 * <h2>Completable签名</h2>
 * 建立任务名和completableFuture实例的关联关系
 *
 * @author Create by liuwenhao on 2021/11/11 11:05
 */
@SuppressWarnings("unused")
class Signature<T> {

    private final String name;

    private final CompletableFuture<T> completableFuture;


    public static <T> Signature<T> build(String name, CompletableFuture<T> completableFuture) {
        return new Signature<>(name, completableFuture);
    }

    public Signature(String name, CompletableFuture<T> completableFuture) {
        this.name = name;
        this.completableFuture = completableFuture;
    }

    public String getName() {
        return name;
    }

    public CompletableFuture<T> getCompletableFuture() {
        return completableFuture;
    }
}