package com.deep.crow.parallel;

import com.deep.crow.multi.Multi;
import com.deep.crow.serial.SerialMulti;
import com.deep.crow.util.Tuple;
import com.deep.crow.util.TypeUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * <h2>并行任务管理</h2>
 *
 * @author Create by liuwenhao on 2022/4/11 17:15
 */
public class ParallelMulti {

    /**
     * Multi
     */
    final List<Multi<?>> multiList = new ArrayList<>();
    /**
     * 线程池
     */
    ExecutorService executorService;

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
     * <h2>添加一个{@link IntSupplier}</h2>
     * 并行执行，不影响其他任务的执行
     *
     * @param s Supplier
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author liuwenhao
     * @date 2022/4/12 16:07
     */
    public <T> ParallelMulti add(IntSupplier s) {
        Objects.requireNonNull(s);
        ParallelTask parallelTask = new IntSupplyTask(s, executorService);
        Multi<T> multi = parallelTask.assembling();
        synchronized (multiList) {
            multiList.add(multi);
        }
        return this;
    }

    /**
     * <h2>添加一个{@link LongSupplier}</h2>
     * 并行执行，不影响其他任务的执行
     *
     * @param s Supplier
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author liuwenhao
     * @date 2022/4/12 16:07
     */
    public <T> ParallelMulti add(LongSupplier s) {
        Objects.requireNonNull(s);
        ParallelTask parallelTask = new LongSupplyTask(s, executorService);
        Multi<T> multi = parallelTask.assembling();
        synchronized (multiList) {
            multiList.add(multi);
        }
        return this;
    }

    /**
     * <h2>添加一个{@link DoubleSupplier}</h2>
     * 并行执行，不影响其他任务的执行
     *
     * @param s Supplier
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author liuwenhao
     * @date 2022/4/12 16:07
     */
    public <T> ParallelMulti add(DoubleSupplier s) {
        Objects.requireNonNull(s);
        ParallelTask parallelTask = new DoubleSupplyTask(s, executorService);
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
        Tuple tuple = resultTuple();
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
        List<?> result = resultList();
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
        resultTuple();
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
        resultTuple();
        runnable.run();
    }

    /**
     * <h2>获取结果</h2>
     *
     * @return java.util.List<?>
     * @author Created by liuwenhao on 2022/4/12 23:16
     */
    public List<?> resultList() {
        return multiList.stream().map(Multi::join).collect(Collectors.toList());
    }

    /**
     * <h2>获取结果</h2>
     *
     * @return Tuple
     * @author Created by liuwenhao on 2022/4/12 23:16
     */
    public Tuple resultTuple() {
        List<?> resultList = resultList();
        Object[] resultArray = resultList.toArray();
        return new Tuple(resultArray);
    }

    /**
     * <h2>获取结果</h2>
     * 如果存在多个符合条件的则返回最先遍历到的<br>
     * 不适用于存在泛型的情况
     *
     * @return T 一个未知的类型
     * @author Created by liuwenhao on 2022/4/12 23:16
     */
    public <T> T get(Class<T> clazz) {
        return TypeUtil.screenClass(resultList(), clazz);
    }

    /**
     * <h2>获取结果</h2>
     * 如果存在多个符合条件的则组成集List集合返回<br>
     * 不适用于存在泛型的情况
     *
     * @return T 一个未知的类型
     * @author Created by liuwenhao on 2022/4/12 23:16
     */
    public <T> List<T> getList(Class<T> clazz) {
        return TypeUtil.screenClasses(resultList(), clazz);
    }

    /**
     * <h2>获取结果</h2>
     * 如果存在多个符合条件的则返回最先遍历到的<br>
     *
     * @return T 一个未知的类型
     * @author Created by liuwenhao on 2022/4/12 23:16
     */
    public <T> T get(Type type) {
        return TypeUtil.screenType(resultList(), type);
    }

    /**
     * <h2>获取结果</h2>
     * 如果存在多个符合条件的则组成集List集合返回<br>
     *
     * @return T 一个未知的类型
     * @author Created by liuwenhao on 2022/4/12 23:16
     */
    public <T> List<T> getList(Type type) {
        return TypeUtil.screenTypes(resultList(), type);
    }


    /**
     * <h2>获取结果</h2>
     * 如果存在多个符合条件的则返回最先遍历到的<br>
     *
     * @return T 一个未知的类型
     * @author Created by liuwenhao on 2022/4/12 23:16
     */
    public <T> T get(Supplier<Type> supplier) {
        return TypeUtil.screenType(resultList(), supplier.get());
    }

    /**
     * <h2>获取结果</h2>
     * 如果存在多个符合条件的则组成集List集合返回<br>
     *
     * @return T 一个未知的类型
     * @author Created by liuwenhao on 2022/4/12 23:16
     */
    public <T> List<T> getList(Supplier<Type> supplier) {
        return TypeUtil.screenTypes(resultList(), supplier.get());
    }

    /**
     * <h2>填充实例</h2>
     *
     * @param t 需要填充的实例对象
     * @return T
     * @author liuwenhao
     * @date 2022/4/30 11:21
     */
    public <T> T getForInstance(T t) {
        TypeUtil.fillInstance(resultList(), t, false);
        return t;
    }

    /**
     * <h2>填充实例</h2>
     *
     * @param clazz 需要填充的类
     * @return T
     * @author liuwenhao
     * @date 2022/4/30 11:21
     */
    public <T> T getForClass(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        return TypeUtil.fillClass(resultList(), clazz, false);
    }

}