package com.deep.crow.parallel;

import com.deep.crow.multi.Multi;
import com.deep.crow.serial.SerialMulti;
import com.deep.crow.util.Tuple;
import sun.reflect.misc.ReflectUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * <h2>并行任务管理</h2>
 *
 * @author Create by liuwenhao on 2022/4/11 17:15
 */
public class ParallelMulti {

    /**
     * 线程池
     */
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

    public ParallelMulti setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
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
        Objects.requireNonNull(s);
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
     * 占用返回结果的位置，其结果为null，如果存在异常节点则添加
     *
     * @param r Supplier
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author liuwenhao
     * @date 2022/4/12 16:07
     */
    public ParallelMulti add(Runnable r) {
        Objects.requireNonNull(r);
        ParallelTask parallelTask = new RunTask(r, executorService);
        Multi<Void> multi = parallelTask.assembling();
        synchronized (multiList) {
            multiList.add(multi);
        }
        return this;
    }

    /**
     * <h2>添加一个异常任务节点</h2>
     * 统一添加，针对所有的任务，会在执行时添加到队尾<T>
     * 仅会在已添加的Multi中执行
     *
     * @param fn 异常任务
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author liuwenhao
     * @date 2022/4/13 10:46
     */
    @SuppressWarnings("unchecked")
    public <T> ParallelMulti add(Function<Throwable, ? extends T> fn) {
        synchronized (multiList) {
            multiList.forEach(m -> {
                Multi<T> t = (Multi<T>) m;
                t.exceptionally(fn);
            });
        }
        return this;
    }

    /**
     * <h2>额外添加一个任务</h2>
     * 在multiList的队尾添加一个并行执行的任务过程
     *
     * @param multi 并行的任务执行
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author Created by liuwenhao on 2022/4/12 23:00
     */
    public <T> ParallelMulti add(Multi<T> multi) {
        synchronized (multiList) {
            multiList.add(multi);
        }
        return this;
    }

    /**
     * <h2>额外添加一个任务的执行</h2>
     * 在multiList的队尾添加一个并行执行的任务过程
     *
     * @param serialMulti 串行化的任务执行实现
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author Created by liuwenhao on 2022/4/12 23:03
     */
    public <T> ParallelMulti add(SerialMulti<T> serialMulti) {
        Multi<T> multi = serialMulti.multi();
        synchronized (multiList) {
            multiList.add(multi);
        }
        return this;
    }

    /**
     * <h2>额外添加一个任务的执行</h2>
     * 在multiList的队尾添加一个并行执行的任务过程
     *
     * @param parallelMulti 并行化的任务执行实现
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author Created by liuwenhao on 2022/4/12 23:03
     */
    public ParallelMulti add(ParallelMulti parallelMulti) {
        synchronized (multiList) {
            multiList.addAll(parallelMulti.multiList);
        }
        return this;
    }

    /**
     * <h2>获取结果并执行任务</h2>
     *
     * @param <R>      结果类型
     * @param function 任务
     * @return R 结果
     * @author Created by liuwenhao on 2022/4/12 23:20
     */
    public <R> R thenExecTuple(Function<? super Tuple, R> function) {
        Objects.requireNonNull(function);
        Tuple tuple = get();
        return function.apply(tuple);
    }

    /**
     * <h2>获取结果并执行任务</h2>
     *
     * @param <R>      结果类型
     * @param function 任务
     * @return R 结果
     * @author Created by liuwenhao on 2022/4/12 23:20
     */
    public <R> R thenExecList(Function<? super List<?>, R> function) {
        Objects.requireNonNull(function);
        List<?> result = result();
        return function.apply(result);
    }

    /**
     * <h2>并行Multi转化成串行Multi</h2>
     *
     * @param <R>      结果类型
     * @param function 任务
     * @return R 结果
     * @author Created by liuwenhao on 2022/4/12 23:20
     */
    public <R> SerialMulti<R> toSerialTuple(Function<? super Tuple, SerialMulti<R>> function) {
        return thenExecTuple(function);
    }

    /**
     * <h2>并行Multi转化成串行Multi</h2>
     *
     * @param <R>      结果类型
     * @param function 任务
     * @return R 结果
     * @author Created by liuwenhao on 2022/4/12 23:20
     */
    public <R> SerialMulti<R> toSerialList(Function<? super List<?>, SerialMulti<R>> function) {
        return thenExecList(function);
    }

    /**
     * <h2>等待装配的任务全部执行，完成后执行给定任务</h2>
     *
     * @param supplier 任务
     * @return T 结果
     * @author Created by liuwenhao on 2022/4/12 23:29
     */
    public <T> T thenApply(Supplier<T> supplier) {
        Objects.requireNonNull(supplier);
        get();
        return supplier.get();
    }

    /**
     * <h2>等待装配的任务全部执行，完成后执行给定任务</h2>
     *
     * @param runnable 任务
     * @author Created by liuwenhao on 2022/4/12 23:29
     */
    public void thenRun(Runnable runnable) {
        Objects.requireNonNull(runnable);
        get();
        runnable.run();
    }

    /**
     * <h2>获取结果</h2>
     *
     * @return java.util.List<?>
     * @author Created by liuwenhao on 2022/4/12 23:16
     */
    public List<?> result() {
        return multiList.stream().map(Multi::join).collect(Collectors.toList());
    }

    /**
     * <h2>获取结果</h2>
     *
     * @return Tuple
     * @author Created by liuwenhao on 2022/4/12 23:16
     */
    public Tuple get() {
        List<?> resultList = result();
        Object[] resultArray = resultList.toArray();
        return new Tuple(resultArray);
    }

}