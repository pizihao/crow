package com.deep.crow.reflect;

import com.deep.crow.ThreadPool;
import com.deep.crow.model.Basic;
import com.deep.crow.parallel.ParallelMulti;
import com.deep.crow.serial.SerialMulti;
import com.esotericsoftware.reflectasm.MethodAccess;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/5/13 0:11
 */
public class ReflectTest {
    public static void main(String[] args) {
        ExecutorService executorService = ThreadPool.executorService();
        SerialMulti<Integer> serialMulti1 = SerialMulti.of(executorService, () -> 10)
            .add(integer -> integer + 10);
        SerialMulti<Integer> serialMulti2 = SerialMulti.of(executorService, () -> 20)
            .add(integer -> integer + 20);
        SerialMulti<Integer> serialMulti3 = SerialMulti.of(executorService, () -> 30)
            .add(integer -> integer + 30);

        ParallelMulti parallelMulti = ParallelMulti.of(executorService)
            .add(serialMulti1)
            .add(serialMulti2)
            .add(serialMulti3);

        parallelMulti.thenRun(() -> System.out.println("处理完成"));
        parallelMulti.thenExecList((Consumer<List<?>>) System.out::println);
        String thenExecList = parallelMulti.thenExecList(objects -> {
            System.out.println(objects);
            return "执行完成";
        });
        System.out.println(thenExecList);

    }
}