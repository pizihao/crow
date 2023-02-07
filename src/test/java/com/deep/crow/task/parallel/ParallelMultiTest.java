package com.deep.crow.task.parallel;

import com.deep.crow.MultiTools;
import com.deep.crow.util.ThreadPool;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.junit.Test;

/**
 * test
 *
 * @author Create by liuwenhao on 2022/4/2 16:08
 */
@SuppressWarnings("all")
public class ParallelMultiTest {

  @Test
  public void testParallel() {

    ExecutorService executorService = ThreadPool.executorService();
    ParallelMulti of = ParallelMulti.of(executorService);
    of.add(() -> System.out.println(1))
        .add(() -> 2)
        .add(MultiTools.supplyAsync(executorService, () -> 5))
        .addThrowable(
            throwable -> {
              System.out.println(throwable.getMessage());
              return 6;
            })
        .add(
            () -> {
              try {
                TimeUnit.SECONDS.sleep(1);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              System.out.println("==========================");
            })
        .thenExecTuple(
            t -> {
              for (Object o : t) {
                System.out.println(o);
              }
            });
  }

  @Test
  public void testOrder() {
    ExecutorService executorService = ThreadPool.executorService();
    ParallelMulti parallelMulti =
        ParallelMulti.of(executorService)
            .add(() -> 1)
            .add(() -> 2)
            .add(5, () -> 3)
            .add(() -> 4)
            .add(() -> 5)
            .add(19, () -> 6)
            .add(() -> 7)
            .add(() -> 8)
            .add(() -> 9)
            .join();
    Assert.assertEquals("[1, 2, 4, 5, 7, 3, 8, 9, 6]", parallelMulti.resultList().toString());
  }

  @Test
  public void testThrowable() {
    ExecutorService executorService = ThreadPool.executorService();
    ParallelMulti parallelMulti =
        ParallelMulti.of(executorService)
            .add(() -> 1)
            .add(() -> 2)
            .add(5, () -> 3)
            .add(() -> 4)
            .add(() -> 5/0)
            .addThrowable(
                throwable -> {
                  System.out.println(throwable.getMessage());
                  return "11";
                },
                1,
                5)
            .add(19, () -> 6)
            .add(() -> 7/0)
            .add(() -> 8)
            .add(() -> 9)
            .addThrowable(
                throwable -> {
                  System.out.println(throwable.getMessage());
                  return "123132";
                });
    Assert.assertEquals("[1, 2, 4, 11, 123132, 3, 8, 9, 6]", parallelMulti.resultList().toString());
  }
}
