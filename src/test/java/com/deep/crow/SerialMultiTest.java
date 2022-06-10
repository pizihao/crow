package com.deep.crow;

import com.deep.crow.task.serial.SerialMulti;
import junit.framework.TestCase;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/4/18 2:55
 */
public class SerialMultiTest extends TestCase {

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
        System.out.println(join);
    }

    public void testQueue() {
        ArrayBlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(10);
        blockingQueue.add(10);
        double size = blockingQueue.size();
        double i = blockingQueue.remainingCapacity() + size;

        System.out.println(size / i);
    }

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
        System.out.println(serial.getIndexSerial(0).join());
        assertEquals(serial.join(), 9);
        System.out.println(of.getResults());
    }

    public void testThrowable() throws InterruptedException {
        ExecutorService executorService = ThreadPool.executorService();
        SerialMulti<Object> of = SerialMulti.of(executorService)
            .add(() -> 1)
            .add(() -> 2)
            .add(() -> 3 / 0)
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
//        SerialMulti<Object> serial = of.getIndexSerial(8);
////        System.out.println(serial.getIndexSerial(6).join());
//        assertEquals(serial.join(), 9);
        TimeUnit.SECONDS.sleep(5);
        System.out.println(of.getResults());
        System.out.println(of.join());
    }

}