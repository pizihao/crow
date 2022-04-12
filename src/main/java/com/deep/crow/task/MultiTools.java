package com.deep.crow.task;

import com.deep.crow.multi.Multi;
import com.deep.crow.multi.MultiHelper;
import com.deep.crow.task.parallel.ParallelMulti;
import com.deep.crow.task.serial.SerialMulti;
import com.deep.crow.util.Tuple;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Create by liuwenhao on 2022/4/12 23:32
 */
@SuppressWarnings("unused")
public class MultiTools {

    private MultiTools() {
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
        return MultiHelper.supplyAsync(executorService, supplier);
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
        return MultiHelper.runAsync(executorService, runnable);
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
        return MultiHelper.create(executorService);
    }

    /**
     * <h2>等待两个Multi执行完成，并使用其结果执行任务</h2>
     *
     * @param one 第一个串行化任务
     * @param two 第二个串行化任务
     * @param bi  任务体
     * @return R 结果
     * @author Created by liuwenhao on 2022/4/12 23:42
     */
    public static <R, M, N> R serialMeet(SerialMulti<M> one,
                                         SerialMulti<N> two,
                                         BiFunction<M, N, R> bi) {
        return bi.apply(one.get(), two.get());
    }

    /**
     * <h2>等待两个Multi执行完成，并使用其结果执行任务</h2>
     *
     * @param one 第一个串行化任务
     * @param two 第二个串行化任务
     * @param bi  任务体
     * @author Created by liuwenhao on 2022/4/12 23:42
     */
    public static <M, N> void serialMeet(SerialMulti<M> one,
                                         SerialMulti<N> two,
                                         BiConsumer<M, N> bi) {
        bi.accept(one.get(), two.get());
    }

    /**
     * <h2>获取并行Multi结果并执行任务</h2>
     *
     * @param <R>      结果类型
     * @param function 任务
     * @return R 结果
     * @author Created by liuwenhao on 2022/4/12 23:20
     */
    public static <R> R parallelTuple(ParallelMulti parallelMulti, Function<? super Tuple, R> function) {
        return parallelMulti.thenExecTuple(function);
    }

    /**
     * <h2>并行Multi转化成串行Multi</h2>
     *
     * @param <R>      结果类型
     * @param function 任务
     * @return R 结果
     * @author Created by liuwenhao on 2022/4/12 23:20
     */
    public static <R> SerialMulti<R> toSerialTuple(ParallelMulti parallelMulti, Function<? super Tuple, SerialMulti<R>> function) {
        return parallelMulti.toSerialTuple(function);
    }

    /**
     * <h2>获取并行Multi结果并执行任务</h2>
     *
     * @param <R>      结果类型
     * @param function 任务
     * @return R 结果
     * @author Created by liuwenhao on 2022/4/12 23:20
     */
    public static <R> R parallelList(ParallelMulti parallelMulti, Function<? super List<?>, R> function) {
        return parallelMulti.thenExecList(function);
    }

    /**
     * <h2>并行Multi转化成串行Multi</h2>
     *
     * @param <R>      结果类型
     * @param function 任务
     * @return R 结果
     * @author Created by liuwenhao on 2022/4/12 23:20
     */
    public static <R> SerialMulti<R> toSerialList(ParallelMulti parallelMulti, Function<? super List<?>, SerialMulti<R>> function) {
        return parallelMulti.toSerialList(function);
    }

    /**
     * <h2>等待转配的任务全部执行，完成后执行给定任务</h2>
     *
     * @param supplier 任务
     * @return T 结果
     * @author Created by liuwenhao on 2022/4/12 23:29
     */
    public static <T> T parallelApply(ParallelMulti parallelMulti, Supplier<T> supplier) {
        return parallelMulti.thenApply(supplier);
    }

    /**
     * <h2>等待转配的任务全部执行，完成后执行给定任务</h2>
     *
     * @param runnable 任务
     * @author Created by liuwenhao on 2022/4/12 23:29
     */
    public void parallelRun(ParallelMulti parallelMulti, Runnable runnable) {
        parallelMulti.thenRun(runnable);
    }

    /**
     * 按照参数的顺序执行，同时返回结果，类似于 Collection.stream().map()
     * 如果Multi的返回结果为null,则转化后对应位置的值依然为null
     *
     * @param c 过程集合
     * @return com.deep.crow.team.Multi<java.lang.Void>
     * @author Created by liuwenhao on 2022/4/9 22:51
     */
    public static List<Object> multipleList(List<Multi<?>> c) {
        return c.stream().map(Multi::get).collect(Collectors.toList());
    }

    /**
     * 按照参数的顺序执行，同时返回结果，类似于 Collection.stream().map()
     * 如果Multi的返回结果为null,则转化后对应位置的值依然为null
     *
     * @param c 过程集合
     * @return Tuple
     * @author Created by liuwenhao on 2022/4/9 22:51
     */
    public static Tuple multipleTuple(List<Multi<?>> c) {
        List<Object> resultList = multipleList(c);
        Object[] resultArray = resultList.toArray();
        return new Tuple(resultArray);
    }


}