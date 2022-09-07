package com.deep.crow.task;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import junit.framework.TestCase;

/**
 * test
 *
 * @author Create by liuwenhao on 2022/4/2 16:08
 */
public class CompletionStageTest extends TestCase {

  public void testCompletableFuture() throws InterruptedException {
    CompletableFuture<Void> a =
        CompletableFuture.runAsync(
            () -> {
              try {
                TimeUnit.SECONDS.sleep(1);
              } catch (InterruptedException ex) {
                ex.printStackTrace();
              }
              System.out.println("A");
            });
    CompletableFuture<String> b =
        a.thenApply(
            unused -> {
              try {
                TimeUnit.SECONDS.sleep(1);
              } catch (InterruptedException ex) {
                ex.printStackTrace();
              }
              System.out.println("B");
              return "B";
            });
    CompletableFuture<String> c =
        a.thenApply(
            unused -> {
              try {
                TimeUnit.SECONDS.sleep(1);
              } catch (InterruptedException ex) {
                ex.printStackTrace();
              }
              System.out.println("C");
              return "C";
            });
    CompletableFuture<String> d =
        a.thenApply(
            unused -> {
              try {
                TimeUnit.SECONDS.sleep(1);
              } catch (InterruptedException ex) {
                ex.printStackTrace();
              }
              System.out.println("D");
              return "D";
            });
    CompletableFuture<String> e =
        b.thenApply(
            unused -> {
              try {
                TimeUnit.SECONDS.sleep(1);
              } catch (InterruptedException ex) {
                ex.printStackTrace();
              }
              System.out.println("E");
              return "E";
            });
    CompletableFuture<String> f =
        c.thenApply(
            unused -> {
              try {
                TimeUnit.SECONDS.sleep(1);
              } catch (InterruptedException ex) {
                ex.printStackTrace();
              }
              System.out.println("F");
              return "F";
            });
    CompletableFuture<String> g =
        d.thenApply(
                unused -> {
                  try {
                    TimeUnit.SECONDS.sleep(1);
                  } catch (InterruptedException ex) {
                    ex.printStackTrace();
                  }
                  System.out.println("G");
                  return "G";
                })
            .thenApply(
                s -> {
                  try {
                    TimeUnit.SECONDS.sleep(1);
                  } catch (InterruptedException ex) {
                    ex.printStackTrace();
                  }
                  System.out.println("G1");
                  return "G";
                });

    TimeUnit.SECONDS.sleep(5);
  }
}
