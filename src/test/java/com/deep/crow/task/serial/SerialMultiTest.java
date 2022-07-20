package com.deep.crow.task.serial;

import com.deep.crow.util.ThreadPool;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class SerialMultiTest {

    @Test
    public void testAdd() {
        ExecutorService executorService = ThreadPool.executorService();
        SerialMulti<Object> of = SerialMulti.of(executorService);
        Object join = of.add(() -> System.out.println("新增一个 runnable"))
            .add(() -> {
                System.out.println("新增一个 supplier");
                return 1;
            })
            .add(integer -> {
                System.out.println("新增一个 consumer");
                System.out.println(integer);
            })
            .add(() -> 5)
            .add(o -> {
                System.out.println("新增一个 function");
                return 3 + o;
            })
            .addThrowable(throwable -> {
                System.out.println(throwable.getMessage());
                return 5;
            }).join();
        Assert.assertEquals(join, 8);
    }

    @Test
    public void testQueue() {
        ArrayBlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(10);
        blockingQueue.add(10);
        double size = blockingQueue.size();
        double i = blockingQueue.remainingCapacity() + size;
        Assert.assertEquals(size / i, 0.1,1);
    }

    @Test

    public void testReplaceQueue() {
        ExecutorService executorService = ThreadPool.executorService();
        SerialMulti<Object> of = SerialMulti.of(executorService)
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
        Assert.assertEquals(serial.getIndexSerial(0).join(), 1);
        Assert.assertEquals(serial.join(), 9);
        Assert.assertEquals(of.getResults().toString(), "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]");
    }

    @Test
    public void testThrowable() throws InterruptedException {
        ExecutorService executorService = ThreadPool.executorService();
        SerialMulti<Object> of = SerialMulti.of(executorService)
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
            .addThrowable(throwable -> {
                System.out.println(throwable.getMessage());
                return "11";
            });
        SerialMulti<Object> serial = of.getIndexSerial(8);
        System.out.println();
        Assert.assertEquals(serial.getIndexSerial(6).join(),7);
        Assert.assertEquals(serial.join(), 9);
        TimeUnit.SECONDS.sleep(5);
        Assert.assertEquals(of.getResults().toString(), "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]");
        Assert.assertEquals(of.join(), "10");
    }

}