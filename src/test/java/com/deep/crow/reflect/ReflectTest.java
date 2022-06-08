package com.deep.crow.reflect;

import com.deep.crow.ThreadPool;
import com.deep.crow.task.parallel.ParallelMulti;
import com.deep.crow.type.TypeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/5/13 0:11
 */
public class ReflectTest {
    public void testReflect() {
        ExecutorService executorService = ThreadPool.executorService();
        List<String> str = ParallelMulti.of(executorService)
            .add(() -> {
                List<Integer> list = new ArrayList<>();
                list.add(1);
                return list;
            })
            .add(() -> {
                List<String> list = new ArrayList<>();
                list.add("Multi");
                return list;
            })
            .add(() -> {
                List<Thread> list = new ArrayList<>();
                list.add(new Thread());
                return list;
            })
            .get(TypeBuilder.list(String.class));

        System.out.println(str);
    }
}