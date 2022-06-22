package com.deep.crow.task.mix;

import com.deep.crow.exception.CrowException;
import com.deep.crow.multi.Multi;
import com.deep.crow.multi.MultiHelper;
import com.sun.istack.internal.Nullable;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * <h2>混合执行过程</h2>
 * <ol>
 *     <li>任务的装配顺序：从前到后</li>
 *     <li>任务的观察顺序：从后到前</li>
 * </ol>
 *
 * @author Create by liuwenhao on 2022/6/14 15:59
 */
public class MixMulti<T> {

    /**
     * 公共操作对象
     */
    T obj;

    /**
     * 线程池
     */
    ExecutorService executorService;

    /**
     * 混合任务集合
     */
    List<MixTask<T>> mixTasks = new ArrayList<>();

    /**
     * 任务名称列表，用于验证
     */
    Set<String> taskName = new HashSet<>();

    /**
     * 任务尾结点集合，尾结点的前置节点不能是尾结点
     */
    @Deprecated
    List<MixTask<T>> tailMixTasks = new ArrayList<>();

    /**
     * 等待任务集合，等待其前置任务完成
     */
    List<MixWaitTask<T>> waitForTask = new ArrayList<>();

    public MixMulti(T obj, ExecutorService executorService) {
        this.obj = obj;
        this.executorService = executorService;
    }

    /**
     * <h2>添加任务。前置任务默认为开始节点</h2>
     * 无需验证前置任务是否已完成
     *
     * @param name     当前任务标识
     * @param consumer 任务
     * @return MixMulti
     * @author liuwenhao
     * @date 2022/6/18 17:20
     */
    public MixMulti<T> add(String name, Consumer<T> consumer) {
        MixTask<T> mixTask = mixTaskBuilder(name, consumer, null);
        addTask(mixTask);
        return this;
    }

    /**
     * <h2>添加任务。并声明前置任务</h2>
     *
     * @param name     当前任务标识
     * @param consumer 任务
     * @param preName  前置任务
     * @return MixMulti
     * @author liuwenhao
     * @date 2022/6/18 17:20
     */
    public MixMulti<T> add(String name, Consumer<T> consumer, String... preName) {
        // 验证前置任务是否已经完成
        Set<String> pre = Arrays.stream(preName).collect(Collectors.toSet());
        boolean checkPreBegin = checkPreBegin(pre, false);
        if (checkPreBegin) {
            MixTask<T> mixTask = mixTaskBuilder(name, consumer, pre);
            addTask(mixTask);
        } else {
            MixWaitTask<T> mixWaitTask = new MixWaitTask<>(name, consumer, pre);
            waitForTask.add(mixWaitTask);
        }
        recastPre(false);
        return this;
    }

    /**
     * <h2>强制任务执行</h2>
     *
     * @author liuwenhao
     * @date 2022/6/20 19:09
     */
    public T exec() {
        recastEnd();
        return obj;
    }

    /**
     * <h2>添加任务</h2>
     *
     * @param mixTask 任务实体
     * @author liuwenhao
     * @date 2022/6/18 17:34
     */
    private synchronized void addTask(MixTask<T> mixTask) {
        Set<String> pre = mixTask.pre();
        // 验证阶段
        checkName(mixTask.name());
        checkPreName(pre);
        // 添加到混合任务节点集合和尾节点集合
        mixTasks.add(mixTask);
        tailMixTasks.add(mixTask);
        taskName.addAll(pre);
        // 从尾结点集合中移出其前置节点并设置为非尾节点
        List<MixTask<T>> mixTaskList = mixTasks.stream()
            .filter(m -> pre.contains(m.name()))
            .peek(MixTask::cancelTail)
            .collect(Collectors.toList());
        tailMixTasks.removeAll(mixTaskList);
        taskName.add(mixTask.name());
    }

    /**
     * <h2>构建混合任务</h2>
     *
     * @param name     任务标识
     * @param consumer 任务体
     * @param pre      前置节点，如果为null则前置节点为
     * @return com.deep.crow.task.mix.MixTask
     * @author liuwenhao
     * @date 2022/6/20 14:47
     */
    private MixTask<T> mixTaskBuilder(String name, Consumer<T> consumer, @Nullable Set<String> pre) {
        Multi<T> multi = MultiHelper.supplyAsync(executorService, () -> obj)
            .thenApply(o -> {
                consumer.accept(o);
                return o;
            });
        MixTask<T> mixTask = new RunnableMixTask<>(name, multi);
        if (Objects.nonNull(pre)) {
            for (String s : pre) {
                mixTask.addPreName(s);
            }
        }
        return mixTask;
    }

    /**
     * <h2>验证任务标识是否已经存在</h2>
     *
     * @param name 任务名称
     * @author liuwenhao
     * @date 2022/6/18 17:27
     */
    private void checkName(String name) {
        if (taskName.contains(name)) {
            CrowException.of("任务名称{}已存在", name);
        }
    }

    /**
     * <h2>验证前置任务是否已经存在</h2>
     * 如果不存在则异常
     *
     * @param preName 前置任务集合
     * @author liuwenhao
     * @date 2022/6/18 17:38
     */
    private void checkPreName(Set<String> preName) {
        boolean flag = true;
        String name = null;

        for (String s : preName) {
            boolean contains = taskName.contains(s);
            if (!contains) {
                flag = false;
                name = s;
                break;
            }
        }
        if (!flag) {
            CrowException.of("前置任务{}不存在", name);
        }
    }

    /**
     * <h2>对等待中的任务进行重新排查</h2>
     *
     * @param force 是否强制
     * @author liuwenhao
     * @date 2022/6/20 14:03
     */
    private void recastPre(boolean force) {
        Iterator<MixWaitTask<T>> iterator = waitForTask.iterator();
        boolean flag = false;
        while (iterator.hasNext()) {
            MixWaitTask<T> waitTask = iterator.next();
            Set<String> preName = waitTask.getPreName();
            boolean preBegin = checkPreBegin(preName, force);
            if (preBegin) {
                flag = true;
                // 需要处理
                MixTask<T> mixTask = mixTaskBuilder(waitTask.getName(), waitTask.getConsumer(), waitTask.getPreName());
                addTask(mixTask);
                iterator.remove();
            }
        }
        if (flag || (force && !waitForTask.isEmpty())) {
            recastPre(force);
        }
    }

    private void recastEnd() {
        if (!waitForTask.isEmpty()) {
            recastPre(true);
        }
        mixTasks.forEach(m -> m.complete(true));
    }

    /**
     * <h2>检查前置任务是否已经完成</h2>
     * 任务完成的标准是前置任务已完成
     *
     * @param preName 前置任务集合
     * @param force   是否强制
     * @return boolean
     * @author liuwenhao
     * @date 2022/6/20 12:01
     */
    private boolean checkPreBegin(Set<String> preName, boolean force) {
        // 优先判断是否存在等待中的

        List<MixWaitTask<T>> waitTasks = waitForTask.stream()
            .filter(m -> preName.contains(m.getName()))
            .collect(Collectors.toList());
        if (!waitTasks.isEmpty()) {
            return false;
        }

        List<MixTask<T>> mixTaskList = mixTasks.stream()
            .filter(m -> preName.contains(m.name()))
            .collect(Collectors.toList());
        if (mixTaskList.isEmpty()) {
            return false;
        }
        for (MixTask<T> mixTask : mixTaskList) {
            if (!mixTask.complete(force)) {
                return false;
            }
        }
        return true;
    }

}