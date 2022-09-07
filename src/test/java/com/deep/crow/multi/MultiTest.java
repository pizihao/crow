package com.deep.crow.multi;

import com.deep.crow.util.ThreadPool;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Assert;
import org.junit.Test;

public class MultiTest {

  @Test
  public void thenApplyTest() {
    ExecutorService executor = ThreadPool.executorService();
    String join = MultiHelper.create(executor).thenApply(o -> "测试").thenApply(s -> s + "测试").join();
    Assert.assertEquals(join, "测试测试");
  }

  @Test
  public void thenAcceptTest() {
    ExecutorService executor = ThreadPool.executorService();
    AtomicInteger a = new AtomicInteger();
    MultiHelper.create(executor)
        .thenAccept(o -> a.getAndIncrement())
        .thenAccept(s -> a.getAndIncrement())
        .join();
    Assert.assertEquals(a.get(), 2);
  }

  @Test
  public void thenRunTest() {
    ExecutorService executor = ThreadPool.executorService();
    AtomicInteger a = new AtomicInteger();
    MultiHelper.create(executor).thenRun(a::getAndIncrement).thenRun(a::getAndIncrement).join();
    Assert.assertEquals(a.get(), 2);
  }

  @Test
  public void thenCombineTest() {
    ExecutorService executor = ThreadPool.executorService();
    Multi<Integer> multi1 = MultiHelper.supplyAsync(executor, () -> 1);
    Integer join =
        MultiHelper.create(executor).thenApply(o -> 2).thenCombine(multi1, Integer::sum).join();
    Assert.assertEquals(join.intValue(), 3);
  }

  @Test
  public void thenBiAcceptTest() {
    ExecutorService executor = ThreadPool.executorService();
    AtomicInteger a = new AtomicInteger();
    Multi<Integer> multi1 = MultiHelper.supplyAsync(executor, () -> 1);
    MultiHelper.create(executor)
        .thenApply(o -> 2)
        .thenBiAccept(multi1, (i, j) -> a.set(i + j))
        .join();
    Assert.assertEquals(a.get(), 3);
  }

  @Test
  public void applyFunTest() {
    ExecutorService executor = ThreadPool.executorService();
    Multi<Integer> multi1 = MultiHelper.supplyAsync(executor, () -> 1);
    Integer join = MultiHelper.create(executor).applyFun(multi1, o -> o + 2).join();
    Assert.assertEquals(join.intValue(), 3);
  }
}
