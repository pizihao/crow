package com.deep.crow.test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * <h2>test</h2>
 *
 * @author Create by liuwenhao on 2022/4/5 16:08
 */
public class CompletionStageTest {

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public static void main(String[] args) throws Exception {
        CompletableFuture<Void> a = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.out.println("A");
        });
        CompletableFuture<String> b = a.thenApply(unused -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.out.println("B");
            return "B";
        });
        CompletableFuture<String> c = a.thenApply(unused -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.out.println("C");
            return "C";
        });
        CompletableFuture<String> d = a.thenApply(unused -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.out.println("D");
            return "D";
        });
        CompletableFuture<String> e = b.thenApply(unused -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.out.println("E");
            return "E";
        });
        CompletableFuture<String> f = c.thenApply(unused -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.out.println("F");
            return "F";
        });
        CompletableFuture<String> g = d.thenApply(unused -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.out.println("G");
            return "G";
        });

        TimeUnit.SECONDS.sleep(50);

    }
}