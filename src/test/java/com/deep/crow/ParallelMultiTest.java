package com.deep.crow;

import com.deep.crow.task.parallel.ParallelMulti;
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
            .add(throwable -> {
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
}