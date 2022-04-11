package com.deep.crow.team;

import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

/**
 * <h2>Multi协助类</h2>
 *
 * @author Create by liuwenhao on 2022/4/11 11:50
 */
public class MultiHelper {

    private MultiHelper() {
    }

    /**
     * <h2>创建一个Multi</h2>
     *
     * @param executorService 线程池
     * @param supplier        任务
     * @return com.deep.crow.team.Multi<U>
     * @author liuwenhao
     * @date 2022/4/11 13:50
     */
    public static <U> Multi<U> supplyAsync(ExecutorService executorService, Supplier<U> supplier) {
        return new MultiImpl<>(executorService, supplier);
    }

    /**
     * <h2>创建一个Multi</h2>
     *
     * @param executorService 线程池
     * @param runnable        任务
     * @return com.deep.crow.team.Multi<U>
     * @author liuwenhao
     * @date 2022/4/11 13:50
     */
    public static Multi<Void> runAsync(ExecutorService executorService, Runnable runnable) {
        return new MultiImpl<>(executorService, runnable);
    }

    /**
     * <h2>创建一个Multi</h2>
     *
     * @param executorService 线程池
     * @return com.deep.crow.team.Multi<U>
     * @author liuwenhao
     * @date 2022/4/11 13:50
     */
    public static <T> Multi<T> create(ExecutorService executorService) {
        return new MultiImpl<>(executorService);
    }

}