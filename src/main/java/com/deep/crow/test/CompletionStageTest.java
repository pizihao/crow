package com.deep.crow.test;

import java.util.concurrent.CompletableFuture;

/**
 * <h2>test</h2>
 *
 * @author Create by liuwenhao on 2022/4/5 16:08
 */
public class CompletionStageTest {
    public static void main(String[] args) throws Exception {
        CompletableFuture.supplyAsync(() -> {
            System.out.println("ssd");
            return 1 + 2;
        })
            .thenApply(i -> i + 5)
            .thenApply(i -> 3 / 0)
            .thenApply(u -> 12)
            .exceptionally(throwable -> {
                System.out.println(123);
                return null;
            })
            .thenApply(integer -> {
                System.out.println(integer);
                return integer;
            });

        CompletableFuture.runAsync(() -> System.out.println(1));
    }
}