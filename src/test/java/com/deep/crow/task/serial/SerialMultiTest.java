package com.deep.crow.task.serial;

import com.deep.crow.util.ThreadPool;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("all")
public class SerialMultiTest {

  @Test
  public void testAdd() {
    ExecutorService executorService = ThreadPool.executorService();
    SerialMulti<Object> of = SerialMulti.of(executorService);
    Object join =
        of.add(() -> System.out.println("新增一个 runnable"))
            .add(
                () -> {
                  System.out.println("新增一个 supplier");
                  return 1;
                })
            .add(
                integer -> {
                  System.out.println("新增一个 consumer");
                  System.out.println(integer);
                })
            .add(() -> 5)
            .add(
                o -> {
                  System.out.println("新增一个 function");
                  return 3 + o;
                })
            .addThrowable(
                throwable -> {
                  System.out.println(throwable.getMessage());
                  return 5;
                })
            .join();
    Assert.assertEquals(8, join);
  }

  @Test
  public void testQueue() {
    ArrayBlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(10);
    blockingQueue.add(10);
    double size = blockingQueue.size();
    double i = blockingQueue.remainingCapacity() + size;
    Assert.assertEquals(0.1, size / i, 1);
  }

  @Test
  public void testReplaceQueue() {
    ExecutorService executorService = ThreadPool.executorService();
    SerialMulti<Object> of =
        SerialMulti.of(executorService)
            .add(() -> 1)
            .add(() -> 2)
            .add(() -> 3)
            .add(() -> 4)
            .add(() -> 5)
            .add(() -> 6)
            .add(() -> 7)
            .add(() -> 8)
            .add(() -> 9)
            .add(() -> "10");
    SerialMulti<Object> serial = of.getIndexSerial(8);
    Assert.assertEquals(1, serial.getIndexSerial(0).join());
    Assert.assertEquals(9, serial.join());
    Assert.assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", of.getResults().toString());
  }

  @Test
  public void testThrowable() throws InterruptedException {
    ExecutorService executorService = ThreadPool.executorService();
    SerialMulti<Object> of =
        SerialMulti.of(executorService)
            .add(() -> 1)
            .add(() -> 2)
            .add(() -> 3)
            .add(() -> 4)
            .add(() -> 5)
            .add(() -> 6)
            .add(() -> 7)
            .add(() -> 8)
            .add(() -> 9)
            .add(() -> "10")
            .addThrowable(
                throwable -> {
                  System.out.println(throwable.getMessage());
                  return "11";
                });
    SerialMulti<Object> serial = of.getIndexSerial(8);
    System.out.println();
    Assert.assertEquals(7, serial.getIndexSerial(6).join());
    Assert.assertEquals(9, serial.join());
    TimeUnit.SECONDS.sleep(5);
    Assert.assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", of.getResults().toString());
    Assert.assertEquals("10", of.join());
  }
}
