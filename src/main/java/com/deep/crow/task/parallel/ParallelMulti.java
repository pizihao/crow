package com.deep.crow.task.parallel;

import com.deep.crow.exception.CrowException;
import com.deep.crow.multi.Multi;
import com.deep.crow.task.serial.SerialMulti;
import com.deep.crow.util.Tuple;
import com.deep.crow.compress.TypeUtil;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * <h2>并行任务管理</h2>
 *
 * @author Create by liuwenhao on 2022/4/11 17:15
 */
@SuppressWarnings("unused")
public class ParallelMulti {

    /**
     * MultiOrder
     */
    final List<MultiOrder<?>> multiList = new ArrayList<>();

    /**
     * 保存已被占用的字符
     */
    final Set<Integer> set = new HashSet<>();

    /**
     * 保存排序的进度
     */
    int state = 0;

    /**
     * 线程池
     */
    ExecutorService executorService;

    IndexRepeat indexRepeat = new ThrowableStrategy();

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

    public void setIndexRepeat(IndexRepeat indexRepeat) {
        this.indexRepeat = indexRepeat;
    }

    /**
     * <h2>将任务添加到执行序列中</h2>
     * 冲突的排序索引会导致任务添加失败
     *
     * @param task 任务
     * @author liuwenhao
     * @date 2022/6/10 11:23
     */
    private synchronized <T> void add(ParallelTask task) {
        // 先判断能不能添加任务
        int order = task.order();
        Multi<T> multi = task.assembling();
        add(order, multi);
    }

    private synchronized <T> void add(int order, Multi<T> multi) {
        if (order < 0) {
            throw CrowException.exception("序号{}小于0，不可用", order);
        }
        if (set.contains(order)) {
            MultiOrder<?> multiOrder = indexRepeat.get(set, order, multi);
            if (multiOrder != null){
                set.add(multiOrder.getOrder());
                multiList.add(multiOrder);
            }
        }
    }

    /**
     * <h2>获取下一个排序索引</h2>
     *
     * @return long
     * @author liuwenhao
     * @date 2022/6/10 13:45
     */
    private synchronized int getState() {
        while (set.contains(state)) {
            state++;
        }
        return state;
    }

    // ===============================添加任务======================================

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
        return add(getState(), s);
    }

    /**
     * <h2>添加一个{@link Supplier}</h2>
     * 并行执行，不影响其他任务的执行
     *
     * @param order 排序号
     * @param s     Supplier
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author liuwenhao
     * @date 2022/6/10 14:00
     */
    public <T> ParallelMulti add(int order, Supplier<T> s) {
        Objects.requireNonNull(s);
        ParallelTask parallelTask = new SupplyTask<>(order, s, executorService);
        add(parallelTask);
        return this;
    }

    /**
     * <h2>添加一个{@link IntSupplier}</h2>
     * 并行执行，不影响其他任务的执行
     *
     * @param s IntSupplier
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author liuwenhao
     * @date 2022/4/12 16:07
     */
    public <T> ParallelMulti add(IntSupplier s) {
        return add(getState(), s);
    }

    /**
     * <h2>添加一个{@link IntSupplier}</h2>
     * 并行执行，不影响其他任务的执行
     *
     * @param order 排序号
     * @param s     IntSupplier
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author liuwenhao
     * @date 2022/6/10 14:00
     */
    public <T> ParallelMulti add(int order, IntSupplier s) {
        Objects.requireNonNull(s);
        ParallelTask parallelTask = new IntSupplyTask(order, s, executorService);
        add(parallelTask);
        return this;
    }

    /**
     * <h2>添加一个{@link LongSupplier}</h2>
     * 并行执行，不影响其他任务的执行
     *
     * @param s LongSupplier
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author liuwenhao
     * @date 2022/4/12 16:07
     */
    public <T> ParallelMulti add(LongSupplier s) {
        return add(getState(), s);
    }

    /**
     * <h2>添加一个{@link LongSupplier}</h2>
     * 并行执行，不影响其他任务的执行
     *
     * @param order 排序号
     * @param s     LongSupplier
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author liuwenhao
     * @date 2022/6/10 14:00
     */
    public <T> ParallelMulti add(int order, LongSupplier s) {
        Objects.requireNonNull(s);
        ParallelTask parallelTask = new LongSupplyTask(order, s, executorService);
        add(parallelTask);
        return this;
    }

    /**
     * <h2>添加一个{@link DoubleSupplier}</h2>
     * 并行执行，不影响其他任务的执行
     *
     * @param s DoubleSupplier
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author liuwenhao
     * @date 2022/4/12 16:07
     */
    public <T> ParallelMulti add(DoubleSupplier s) {
        return add(getState(), s);
    }

    /**
     * <h2>添加一个{@link DoubleSupplier}</h2>
     * 并行执行，不影响其他任务的执行
     *
     * @param order 排序号
     * @param s     DoubleSupplier
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author liuwenhao
     * @date 2022/6/10 14:00
     */
    public <T> ParallelMulti add(int order, DoubleSupplier s) {
        Objects.requireNonNull(s);
        ParallelTask parallelTask = new DoubleSupplyTask(order, s, executorService);
        add(parallelTask);
        return this;
    }

    /**
     * <h2>添加一个{@link Runnable}</h2>
     * 并行执行，不影响其他任务的执行<br>
     * 占用返回结果的位置，其结果为null，如果存在异常节点则添加
     *
     * @param r Runnable
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author liuwenhao
     * @date 2022/4/12 16:07
     */
    public ParallelMulti add(Runnable r) {
        return add(getState(), r);
    }

    /**
     * <h2>添加一个{@link Runnable}</h2>
     * 并行执行，不影响其他任务的执行<br>
     * 占用返回结果的位置，其结果为null，如果存在异常节点则添加
     *
     * @param order 排序号
     * @param r     Runnable
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author liuwenhao
     * @date 2022/6/10 14:00
     */
    public ParallelMulti add(int order, Runnable r) {
        Objects.requireNonNull(r);
        ParallelTask parallelTask = new RunTask(order, r, executorService);
        add(parallelTask);
        return this;
    }

    /**
     * <h2>添加一个异常任务节点</h2>
     * 为处于[start,end]之间的任务添加异常处理
     *
     * @param start 开始序号
     * @param end   结束序号
     * @param fn    异常任务
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author liuwenhao
     * @date 2022/6/10 16:58
     */
    @SuppressWarnings("unchecked")
    public <T> ParallelMulti addThrowable(Function<Throwable, ? extends T> fn, int start, int end) {
        synchronized (multiList) {
            multiList.stream()
                .filter(m -> m.getOrder() >= start && m.getOrder() <= end)
                .forEach(m -> {
                    MultiOrder<T> multiOrder = (MultiOrder<T>) m;
                    multiOrder.setFn(fn);
                });
        }
        return this;
    }

    /**
     * <h2>添加一个异常任务节点</h2>
     * 为处于一组任务添加异常处理
     *
     * @param numbers 序号
     * @param fn      异常任务
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author liuwenhao
     * @date 2022/6/10 16:58
     */
    @SuppressWarnings("unchecked")
    public <T> ParallelMulti addThrowable(Function<Throwable, ? extends T> fn, int... numbers) {
        List<Integer> list = Arrays.stream(numbers).boxed().collect(Collectors.toList());
        synchronized (multiList) {
            multiList.stream()
                .filter(m -> list.contains(m.getOrder()))
                .forEach(m -> {
                    MultiOrder<T> multiOrder = (MultiOrder<T>) m;
                    multiOrder.setFn(fn);
                });
        }
        return this;
    }

    /**
     * <h2>添加一个异常任务节点</h2>
     * 如果还有任务没有被设置异常处理节点，则添加<br>
     * 如果所有的任务都已经被设置了异常会处理，则无效
     *
     * @param fn 异常任务
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author liuwenhao
     * @date 2022/4/13 10:46
     */
    @SuppressWarnings("unchecked")
    public <T> ParallelMulti addThrowable(Function<Throwable, ? extends T> fn) {
        synchronized (multiList) {
            multiList.stream().filter(MultiOrder::isThrowable)
                .forEach(m -> {
                    MultiOrder<T> multiOrder = (MultiOrder<T>) m;
                    multiOrder.setFn(fn);
                });
        }
        return this;
    }

    // ===============================额外添加======================================

    /**
     * <h2>额外添加一个任务</h2>
     * 在multiList的队尾添加一个并行执行的任务过程
     *
     * @param multi 并行的任务执行
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author Created by liuwenhao on 2022/4/12 23:00
     */
    public <T> ParallelMulti add(Multi<T> multi) {
        add(getState(), multi);
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
        add(getState(), multi);
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
        parallelMulti.multiList.forEach(m -> add(getState(), m.getMulti()));
        return this;
    }

    // ===============================获取Multi======================================

    /**
     * <h2>通过定义的索引获取Multi</h2>
     *
     * @param index 索引
     * @return com.deep.crow.multi.Multi<T>
     * @throws CrowException 当指明的索引值没有映射时
     * @author liuwenhao
     * @date 2022/6/13 9:17
     */
    @SuppressWarnings("unchecked")
    public <T> Multi<T> multiForIndex(int index) {
        if (!set.contains(index)) {
            throw CrowException.exception("不存在的索引值 -- {}", index);
        }
        MultiOrder<?> multiOrder = multiList.stream()
            .filter(m -> m.getOrder() == index)
            .findFirst().orElse(null);
        return multiOrder == null ? null : (Multi<T>) multiOrder.getMulti();
    }

    // ===============================后置操作和前置操作======================================

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
     * <h2>获取结果并执行任务</h2>
     *
     * @param consumer 任务
     * @author Created by liuwenhao on 2022/4/12 23:20
     */
    public void thenExecTuple(Consumer<? super Tuple> consumer) {
        Objects.requireNonNull(consumer);
        Tuple tuple = resultTuple();
        consumer.accept(tuple);
    }

    /**
     * <h2>获取结果并执行任务</h2>
     *
     * @param consumer 任务
     * @author Created by liuwenhao on 2022/4/12 23:20
     */
    public void thenExecList(Consumer<? super List<?>> consumer) {
        Objects.requireNonNull(consumer);
        List<?> result = resultList();
        consumer.accept(result);
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

    // ===============================结果的获取======================================

    /**
     * <h2>等待所有任务执行完成</h2>
     * 不获取结果
     *
     * @author liuwenhao
     * @date 2022/6/13 11:24
     */
    public void join() {
        resultList();
    }

    /**
     * <h2>获取结果</h2>
     *
     * @return java.util.List<?>
     * @author Created by liuwenhao on 2022/4/12 23:16
     */
    @SuppressWarnings("unchecked")
    public <T> List<Object> resultList() {
        return multiList.stream()
            .sorted()
            .peek(m -> {
                if (!m.isThrowable()) {
                    Multi<T> mMulti = (Multi<T>) m.getMulti();
                    mMulti.exceptionally((Function<Throwable, ? extends T>) m.getFn());
                }
            })
            .map(m -> m.getMulti().join())
            .collect(Collectors.toList());
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
     * 如果存在多个符合条件的则组成List集合返回<br>
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
        return getForInstance(t, false);
    }

    /**
     * <h2>填充实例</h2>
     *
     * @param t       需要填充的实例对象
     * @param isCover 是否覆盖已有的数据
     * @return T
     * @author liuwenhao
     * @date 2022/4/30 11:21
     */
    public <T> T getForInstance(T t, boolean isCover) {
        TypeUtil.fillInstance(resultList(), t, isCover);
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
    public <T> T getForClass(Class<T> clazz) {
        return getForClass(clazz, false);
    }

    /**
     * <h2>填充实例</h2>
     *
     * @param clazz   需要填充的类
     * @param isCover 是否覆盖已有的数据
     * @return T
     * @author liuwenhao
     * @date 2022/4/30 11:21
     */
    public <T> T getForClass(Class<T> clazz, boolean isCover) {
        return TypeUtil.fillClass(resultList(), clazz, isCover);
    }

    /**
     * <h2>批量填充</h2>
     *
     * @param ts      需要填充的类对象集合
     * @param isCover 是否覆盖
     * @return java.util.Collection<T>
     * @author liuwenhao
     * @date 2022/6/7 17:01
     */
    public <T> Collection<T> batchForInstance(Collection<T> ts, boolean isCover) {
        return TypeUtil.fillCollection(resultList(), ts, isCover);
    }

    /**
     * <h2>批量填充</h2>
     * 默认不覆盖
     *
     * @param ts 需要填充的类对象集合
     * @return java.util.Collection<T>
     * @author liuwenhao
     * @date 2022/6/7 17:01
     */
    public <T> Collection<T> batchForInstance(Collection<T> ts) {
        return batchForInstance(ts, false);
    }


    /**
     * 直接抛出异常，默认
     */
    public static class ThrowableStrategy implements IndexRepeat {
        @Override
        public MultiOrder<?> get(Set<Integer> index, int order, Multi<?> multi) {
            throw CrowException.exception("序号{}处已存在任务，不可继续添加", order);
        }
    }


    /**
     * 顺延出下一个序号，这可能和用户指定的不一致
     */
    public static class PostponeStrategy implements IndexRepeat {
        @Override
        public MultiOrder<?> get(Set<Integer> index, int order, Multi<?> multi) {
            do {
                order++;
            } while (index.contains(order));
            return new MultiOrder<>(multi, order);
        }
    }

    /**
     * 直接忽略
     */
    public static class IgnoreStrategy implements IndexRepeat {
        @Override
        public MultiOrder<?> get(Set<Integer> index, int order, Multi<?> multi) {
            return null;
        }
    }

}