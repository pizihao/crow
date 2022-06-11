package com.deep.crow.task.parallel;

import com.deep.crow.MultiTools;
import com.deep.crow.util.ThreadPool;
import junit.framework.TestCase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <h2>test</h2>
 *
 * @author Create by liuwenhao on 2022/4/2 16:08
 */
public class ParallelMultiTest extends TestCase {

    public void testParallel() {

        ExecutorService executorService = ThreadPool.executorService();
        ParallelMulti of = ParallelMulti.of(executorService);
        of.add(() -> System.out.println(1))
            .add(() -> 2)
            .add(MultiTools.supplyAsync(executorService, () -> 5))
            .addThrowable(throwable -> {
                System.out.println(throwable.getMessage());
                return 6;
            })
//            .add((Supplier<Object>) () -> {
//                throw new CrowException("456");
//            })
            .add(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("==========================");
            })
            .thenExecTuple(t -> {
                for (Object o : t) {
                    System.out.println(o);
                }
            });
    }

    public void testOrder() {
        ExecutorService executorService = ThreadPool.executorService();
        ParallelMulti parallelMulti = ParallelMulti.of(executorService)
            .add(() -> 1)
            .add(() -> 2)
            .add(5, () -> 3)
            .add(() -> 4)
            .add(() -> 5)
            .add(19, () -> 6)
            .add(() -> 7)
            .add(() -> 8)
            .add(() -> 9);
        System.out.println(parallelMulti.resultList());
    }

    public void testThrowable() {
        ExecutorService executorService = ThreadPool.executorService();
        ParallelMulti parallelMulti = ParallelMulti.of(executorService)
            .add(() -> 1)
            .add(() -> 2)
            .add(5, () -> 3)
            .add(() -> 4)
            .add(() -> 5 / 0)
            .addThrowable(throwable -> {
                System.out.println(throwable.getMessage());
                return "11";
            }, 1, 5)
            .add(19, () -> 6)
            .add(() -> 7 / 0)
            .add(() -> 8)
            .add(() -> 9)
            .addThrowable(throwable -> {
                System.out.println(throwable.getMessage());
                return "123132";
            });
        System.out.println(parallelMulti.resultList());
    }

}