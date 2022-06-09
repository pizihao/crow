package com.deep.crow.task.serial;

import com.deep.crow.multi.Multi;
import com.deep.crow.multi.MultiHelper;
import com.deep.crow.task.parallel.ParallelMulti;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.*;

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

    /**
     * 存储任务的队列，不会添加异常节点
     */
    BlockingQueue<Multi<?>> multiQueue = new ArrayBlockingQueue<>(DEFAULT_CAPACITY);

    /**
     * 存储结果的集合，在获取结果时才会写入，通过getNow的方式
     */
    List<Object> resultList = new ArrayList<>();

    /**
     * 锁
     */
    Lock lock = new ReentrantLock();

    /**
     * 同步信号，再将multiQueue的结果同步到resultList时，为防止多个线程同时操作从而设置信号<br>
     * 该信号在进入同步方方法时被设置为false，同步完成后被设置为true<br>
     * 当任务拿到的信号为true时需调用同步方法进行结果同步，如果没有进入则再次检查信号<br>
     * 如果信号已经被改变成false则等待，直到信号再次被修改为true，则说明同步已经完成，方法返回
     */
    volatile boolean syncSign = true;

    /**
     * 任务队列默认的初始容量
     */
    static final int DEFAULT_CAPACITY = 8;

    /**
     * 任务队列默认的扩容比例
     */
    static final double DEFAULT_PROPORTION = 0.8;


    private SerialMulti(Multi<T> multi) {
        this(ForkJoinPool.commonPool(), multi);
        addMulti(this.multi);
    }

    private SerialMulti(ExecutorService executorService) {
        this(executorService, MultiHelper.create(executorService));
    }

    private SerialMulti(ExecutorService executorService, Multi<T> multi) {
        this.multi = multi;
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

    public static <T> SerialMulti<T> of(Multi<T> multi) {
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
        return createSerial(new FunctionTask<>(function));
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
        return createSerial(new RunnableTask<>(runnable));
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
        return createSerial(new SupplierTask<>(supplier));
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
        return createSerial(new ConsumerTask<>(consumer));
    }

    /**
     * <h2>异常节点</h2>
     *
     * @param function 任务
     * @return com.deep.crow.task.serial.SerialMulti
     * @author liuwenhao
     * @date 2022/4/11 14:24
     */
    @SuppressWarnings("unchecked")
    public synchronized <U> SerialMulti<U> addThrowable(Function<Throwable, U> function) {
        Objects.requireNonNull(function);
        Multi<U> tMulti = (Multi<U>) this.multi;
        SerialTask<U> task = new ExceptionallyTask<>(function);
        this.multi = task.increase(tMulti);
        return (SerialMulti<U>) this;
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
        return parallelMulti.add(this);
    }

    // ===============================结果的获取======================================

    /**
     * <h2>通过索引获取结果</h2>
     * 索引代表着装入串行化结构的顺序，初始化时如果为空任务则不会占据空间<br>
     * 如果某个任务的返回值为null或者是不存在返回值也会占据空间
     *
     * @param index 索引
     * @return T
     * @author liuwenhao
     * @date 2022/6/9 11:16
     */
    @SuppressWarnings("unchecked")
    public T getIndexOf(int index) {
        T t;
        // 如果修改成功说明需要进行同步信息
        if (compareAndSwapSign(true, false)) {
            syncResult();
        }
        // 如果修改失败说明已经有其他的线程进行修改
        for (; ; ) {
            if (syncSign) {
                t = (T) resultList.get(index);
                break;
            }
        }
        return t;
    }

    /**
     * <h2>获取结果列表</h2>
     * 会生成一个新的集合来存放这些结果<br>
     * 对该集合的修改不会影响到串行化过程的结果顺序
     *
     * @return java.util.Collection<java.lang.Object>
     * @author liuwenhao
     * @date 2022/6/9 13:37
     */
    public Collection<Object> getResults() {
        Collection<Object> results;
        if (compareAndSwapSign(true, false)) {
            syncResult();
        }
        for (; ; ) {
            if (syncSign) {
                results = new ArrayList<>(resultList);
                break;
            }
        }
        return results;
    }

    /**
     * <h2>通过索引获取某一个位置的串行化过程</h2>
     *
     * @param index 索引
     * @return com.deep.crow.task.serial.SerialMulti<T>
     * @author liuwenhao
     * @date 2022/6/9 14:24
     */
    @SuppressWarnings("unchecked")
    public <U> SerialMulti<U> getIndexSerial(int index) {
        Multi<?>[] array = multiQueue.toArray(new Multi[0]);
        if (index > array.length) {
            throw new IndexOutOfBoundsException(index + "超出范围");
        }
        int count = Math.max(index, DEFAULT_CAPACITY);

        // 还原队列
        ArrayBlockingQueue<Multi<?>> blockingQueue = new ArrayBlockingQueue<>(count);

        blockingQueue.addAll(Arrays.asList(array).subList(0, index));
        SerialMulti<U> serialMulti = new SerialMulti<>(executorService, (Multi<U>) array[index]);
        serialMulti.supplementMulti(blockingQueue);
        return serialMulti;
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

    // ===============================其他======================================

    private synchronized <U> SerialMulti<U> createSerial(SerialTask<T> task) {
        Multi<T> copyMulti = this.multi.copyMulti();
        Multi<U> uMulti = task.increase(copyMulti);
        SerialMulti<U> serialMulti = new SerialMulti<>(executorService, uMulti);
        serialMulti.supplementMulti(multiQueue);
        serialMulti.addMulti(uMulti);
        return serialMulti;
    }

    private synchronized boolean compareAndSwapSign(boolean oldValue, boolean newValue) {
        if (syncSign == oldValue) {
            syncSign = newValue;
            return true;
        }
        return false;
    }

    private void syncResult() {
        lock.lock();
        try {
            Multi<?>[] array = multiQueue.toArray(new Multi[0]);
            int size = array.length;
            if (resultList.size() < size) {
                for (int i = resultList.size(); i < size; i++) {
                    resultList.add(array[i].getNow());
                }
            }
            for (; ; ) {
                if (compareAndSwapSign(false, true)) {
                    break;
                }
            }
        } finally {
            lock.unlock();
        }

    }

    private void addMulti(Multi<?> multi) {
        lock.lock();
        try {
            /*
             * 判断是否需要扩容
             * 考虑到单个串行化任务的增加只能是单个单个的增加，所以内部队列的扩充并不需要预留太大的空间
             */
            double size = multiQueue.size();
            double capacity = multiQueue.remainingCapacity() + size;
            double proportion = size / capacity;
            if (proportion > DEFAULT_PROPORTION) {
                // 需要扩容
                replaceQueue(multiQueue.size() + 1);
            }
            multiQueue.add(multi);
        } finally {
            lock.unlock();
        }
    }

    /**
     * <h2>扩容的本质是替换一个更大的队列</h2>
     *
     * @param minCapacity 最小的扩容量
     * @author liuwenhao
     * @date 2022/6/9 15:56
     */
    private synchronized void replaceQueue(int minCapacity) {
        int oldSize = multiQueue.size() + multiQueue.remainingCapacity();
        // 新的容量是在原有的基础上增加8个
        int newSize = oldSize + DEFAULT_CAPACITY;
        if (newSize - minCapacity < 0) {
            newSize = minCapacity;
        }
        if (newSize - Integer.MAX_VALUE > 0) {
            throw new OutOfMemoryError();
        }
        // 替换的过程
        ArrayBlockingQueue<Multi<?>> blockingQueue = new ArrayBlockingQueue<>(newSize);
        blockingQueue.addAll(multiQueue);
        multiQueue = blockingQueue;
    }

    private void supplementMulti(BlockingQueue<Multi<?>> blockingQueue) {
        lock.lock();
        try {
            if (Objects.nonNull(multiQueue.peek())) {
                throw new UnsupportedOperationException("已运行中的串行化过程不可更改队列");
            }
            if (multiQueue.remainingCapacity() < blockingQueue.size()) {
                replaceQueue(blockingQueue.size());
            }
            multiQueue.addAll(blockingQueue);
            resultList = new ArrayList<>(multiQueue.remainingCapacity() + multiQueue.size());
        } finally {
            lock.unlock();
        }
    }
}