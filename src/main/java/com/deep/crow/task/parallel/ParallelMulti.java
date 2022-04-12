package com.deep.crow.task.parallel;

import com.deep.crow.multi.Multi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Supplier;

/**
 * <h2>并行任务管理</h2>
 *
 * @author Create by liuwenhao on 2022/4/11 17:15
 */
public class ParallelMulti {

    ExecutorService executorService;

    /**
     * Multi
     */
    final List<Multi<?>> multiList = new ArrayList<>();

    private ParallelMulti(ExecutorService executorService) {
        this.executorService = executorService;
    }

    private ParallelMulti() {
        this(ForkJoinPool.commonPool());
    }

    public static ParallelMulti of(ExecutorService executorService) {
        return new ParallelMulti(executorService);
    }

    public static ParallelMulti of() {
        return new ParallelMulti();
    }

    /**
     * <h2>添加一个{@link Supplier}</h2>
     * 并行执行，不影响其他任务的执行
     *
     * @param s Supplier
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author liuwenhao
     * @date 2022/4/12 16:07
     */
    public <T> ParallelMulti add(Supplier<T> s) {
        Objects.requireNonNull(s, "任务不能为null");
        ParallelTask parallelTask = new SupplyTask<>(s, executorService);
        Multi<T> multi = parallelTask.assembling();
        synchronized (multiList) {
            multiList.add(multi);
        }
        return this;
    }

    /**
     * <h2>添加一个{@link Runnable}</h2>
     * 并行执行，不影响其他任务的执行<br>
     * 占用返回结果的位置，其结果为null
     *
     * @param r Supplier
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author liuwenhao
     * @date 2022/4/12 16:07
     */
    public ParallelMulti add(Runnable r) {
        Objects.requireNonNull(r, "任务不能为null");
        ParallelTask parallelTask = new RunTask(r, executorService);
        Multi<Void> multi = parallelTask.assembling();
        synchronized (multiList) {
            multiList.add(multi);
        }
        return this;
    }

}