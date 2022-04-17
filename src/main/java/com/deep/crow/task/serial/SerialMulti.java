package com.deep.crow.task.serial;

import com.deep.crow.multi.Multi;
import com.deep.crow.multi.MultiHelper;
import com.deep.crow.task.parallel.ParallelMulti;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <h2>串行任务管理</h2>
 *
 * @author Create by liuwenhao on 2022/4/11 13:27
 */
@SuppressWarnings("unused")
public class SerialMulti<T> {

    /**
     * multi
     */
    Multi<T> multi;

    /**
     * executorService
     */
    ExecutorService executorService;

    private SerialMulti(Multi<T> multi) {
        this.multi = multi;
        this.executorService = ForkJoinPool.commonPool();
    }

    private SerialMulti(ExecutorService executorService) {
        this.multi = MultiHelper.create(executorService);
        this.executorService = executorService;
    }

    public static <T> SerialMulti<T> of() {
        return new SerialMulti<>(ForkJoinPool.commonPool());
    }

    public static <T> SerialMulti<T> of(ExecutorService executorService) {
        return new SerialMulti<>(executorService);
    }

    public static <T> SerialMulti<T> of(Supplier<T> supplier) {
        return SerialMulti.of(ForkJoinPool.commonPool(), supplier);
    }

    public static SerialMulti<Void> of(Runnable runnable) {
        return SerialMulti.of(ForkJoinPool.commonPool(), runnable);
    }

    public static <T> SerialMulti<T> of(ExecutorService executorService, Supplier<T> supplier) {
        Multi<T> multi = MultiHelper.supplyAsync(executorService, supplier);
        return new SerialMulti<>(multi);
    }

    public static SerialMulti<Void> of(ExecutorService executorService, Runnable runnable) {
        Multi<Void> multi = MultiHelper.runAsync(executorService, runnable);
        return new SerialMulti<>(multi);
    }

    /**
     * <h2>function</h2>
     *
     * @param function 任务
     * @return com.deep.crow.task.serial.SerialMulti
     * @author liuwenhao
     * @date 2022/4/11 14:24
     */
    public <U> SerialMulti<U> add(Function<T, U> function) {
        Objects.requireNonNull(function);
        Multi<T> tMulti = this.multi;
        SerialTask<T> task = new FunctionTask<>(function);
        Multi<U> uMulti = task.increase(tMulti);
        return new SerialMulti<>(uMulti);
    }

    /**
     * <h2>runnable</h2>
     *
     * @param runnable 任务
     * @return com.deep.crow.task.serial.SerialMulti
     * @author liuwenhao
     * @date 2022/4/11 14:24
     */
    public <U> SerialMulti<U> add(Runnable runnable) {
        Objects.requireNonNull(runnable);
        Multi<T> tMulti = this.multi;
        SerialTask<T> task = new RunnableTask<>(runnable);
        Multi<U> uMulti = task.increase(tMulti);
        return new SerialMulti<>(uMulti);
    }

    /**
     * <h2>supplier</h2>
     *
     * @param supplier 任务
     * @return com.deep.crow.task.serial.SerialMulti
     * @author liuwenhao
     * @date 2022/4/11 14:24
     */
    public <U> SerialMulti<U> add(Supplier<U> supplier) {
        Objects.requireNonNull(supplier);
        Multi<T> tMulti = this.multi;
        SerialTask<T> task = new SupplierTask<>(supplier);
        Multi<U> uMulti = task.increase(tMulti);
        return new SerialMulti<>(uMulti);
    }

    /**
     * <h2>consumer</h2>
     *
     * @param consumer 任务
     * @return com.deep.crow.task.serial.SerialMulti
     * @author liuwenhao
     * @date 2022/4/11 14:24
     */
    public <U> SerialMulti<U> add(Consumer<T> consumer) {
        Objects.requireNonNull(consumer);
        Multi<T> tMulti = this.multi;
        SerialTask<T> task = new ConsumerTask<>(consumer);
        Multi<U> uMulti = task.increase(tMulti);
        return new SerialMulti<>(uMulti);
    }

    /**
     * <h2>异常节点</h2>
     *
     * @param function 任务
     * @return com.deep.crow.task.serial.SerialMulti
     * @author liuwenhao
     * @date 2022/4/11 14:24
     */
    public <U> SerialMulti<U> addThrowable(Function<Throwable, T> function) {
        Objects.requireNonNull(function);
        Multi<T> tMulti = this.multi;
        SerialTask<T> task = new ExceptionallyTask<>(function);
        Multi<U> uMulti = task.increase(tMulti);
        return new SerialMulti<>(uMulti);
    }

    /**
     * <h2>SerialMulti -> Multi</h2>
     *
     * @return com.deep.crow.multi.Multi<T>
     * @author Created by liuwenhao on 2022/4/12 23:04
     */
    public Multi<T> multi() {
        return multi;
    }

    /**
     * <h2>转化为 ParallelMulti</h2>
     * 将一条串行化的链整合为一个并行化的链，this会成为第一个元素
     *
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author Created by liuwenhao on 2022/4/12 23:09
     */
    public ParallelMulti toParallel() {
        ParallelMulti parallelMulti = ParallelMulti.of(executorService);
        parallelMulti.add(this);
        return parallelMulti;
    }

    public T get() throws ExecutionException, InterruptedException {
        return multi.get();
    }

    public T join() {
        return multi.join();
    }

    public T get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
        return multi.get(timeout, unit);
    }

}