package com.deep.crow;

import com.deep.crow.multi.MultiHelper;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/4/7 16:09
 */
public class ThrowableTest {

    public static void main(String[] args) {

        ExecutorService executorService = ThreadPool.executorService();
        MultiHelper
            .runAsync(executorService, () -> System.out.println(1))
            .thenApply(unused -> 1)
            .thenApply(integer -> {
                System.out.println(integer);
                return 1;
            })
            .thenApply(integer -> {
                System.out.println(integer);
                return null;
            }).get();

        CompletableFuture.runAsync(() -> System.out.println(1))
            .thenApplyAsync(unused -> 1, executorService)
            .thenApplyAsync(integer -> {
                System.out.println(integer);
                return 1;
            }, executorService)
            .thenApplyAsync(integer -> {
                System.out.println(integer);
                return null;
            }, executorService).join();

//        CompletableFuture.runAsync(() -> System.out.println(123))
//            .thenApply(unused -> {
//                System.out.println(456);
//                return 456;
//            })
//            .thenApply(integer -> {
//                System.out.println(integer);
//                return 142;
//            })
//            .thenApply(integer -> integer / 0)
//            .exceptionally(throwable -> {
//                throw CrowException.exception("异常线程 -> {}", Thread.currentThread().getName());
//            }).join();
//
//        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> 1).thenApply(integer -> "1");
    }
}