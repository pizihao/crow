package com.deep.crow;

import com.deep.crow.exception.CrowException;
import com.deep.crow.parallel.ParallelMulti;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * <h2>test</h2>
 *
 * @author Create by liuwenhao on 2022/4/2 16:08
 */
public class ParallelMultiTest {

    public static void main(String[] args) {
        ParallelMultiTest multiTest = new ParallelMultiTest();
        multiTest.parallelTest();
    }

    public void parallelTest() {

        ExecutorService executorService = ThreadPool.executorService();
        ParallelMulti of = ParallelMulti.of(executorService);
        of.add(() -> System.out.println(1))
            .add(() -> 2)
            .add(MultiTools.supplyAsync(executorService, () -> 5))
            .add(throwable -> {
                System.out.println(throwable.getMessage());
                return 6;
            })
//            .add((Supplier<Object>) () -> {
//                throw new CrowException("456");
//            })
            .add(() -> {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("==========================");
            })
            .add(throwable -> {
                System.out.println(Thread.currentThread().getName());
                throw CrowException.exception("异常信息");
            })
            .thenExecTuple(t -> {
                for (Object o : t) {
                    System.out.println(o);
                }
            });

    }
}