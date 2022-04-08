package com.deep.crow;

import com.deep.crow.exception.CrowException;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/4/7 16:09
 */
public class ThrowableTest {

    public static void main(String[] args) {

        Set<Integer> integers = new HashSet<>();
        integers.add(1);

        System.out.println(integers.add(2));
        System.out.println(integers.add(1));

        CompletableFuture.runAsync(() -> System.out.println(123))
            .thenApply(unused -> {
                System.out.println(456);
                return 456;
            })
            .thenApply(integer -> {
                System.out.println(integer);
                return 142;
            })
            .thenApply(integer -> integer / 0)
            .exceptionally(throwable -> {
                throw CrowException.exception("异常线程 -> {}", Thread.currentThread().getName());
            }).join();

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> 1).thenApply(integer -> "1");
    }
}