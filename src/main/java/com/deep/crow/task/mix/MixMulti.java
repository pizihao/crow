package com.deep.crow.task.mix;

import com.deep.crow.exception.CrowException;
import com.deep.crow.multi.Multi;
import com.deep.crow.multi.MultiHelper;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 混合执行过程
 *
 * <ol>
 *   <li>任务的装配顺序：从前到后
 *   <li>任务的观察顺序：从后到前
 * </ol>
 *
 * 限于任务的合理性，如果存在某个任务拥有返回值，那么这个返回值将很难被其后续的任务所使用。 原因是这样的：一个任务可能存在多个不同的前置任务，那么就必须同时接收多个前置任务的返回值，
 * MixMulti的任务链都是动态生成的在任务真正执行之前是不会完全确定有几个参数的，其次参数的 类型都是不确定的，所以采用了公共变量的形式，这样的话，在多线程的情况下就需要考虑线程安全
 * 问题，如果多个线程通过修改的话该听谁的？ 这种情况下，这个公共对象就应该使用{@link AtomicReference}，但是在MixMulti中涉及到
 * 公共变量的操作都应存在于一个唯一的中间节点中，他不允许有其他的节点和他同时执行，这样就保证 了所有的写操作都会在同一个线程中进行，而其他的多线程操作都使用读的方式。 这样一来{@link
 * AtomicReference}和普通的泛型对象得到的结果都一样。
 *
 * @author Create by liuwenhao on 2022/6/14 15:59
 */
public class MixMulti<T> {

  /** 公共操作对象 */
  T obj;

  /** 线程池 */
  ExecutorService executorService;

  /** 混合任务集合 */
  List<MixTask<T>> mixTasks = new ArrayList<>();

  /** 任务名称列表，用于验证 */
  Set<String> taskName = new HashSet<>();

  /** 任务尾结点集合，尾结点的前置节点不能是尾结点 */
  List<MixTask<T>> tailMixTasks = new ArrayList<>();

  /** 等待任务集合，等待最终阶段进行装配 */
  List<MixWaitTask<T>> waitForTask = new ArrayList<>();

  /** 已装配异常节点的任务名称集合 */
  Set<String> throwableName = new HashSet<>();

  /** 异常节点，即使在任务的执行过程中出现了异常也不应该对最终的返回结果产生结果 */
  Consumer<Throwable> fn;

  private MixMulti(T obj, ExecutorService executorService) {
    this.obj = obj;
    this.executorService = executorService;
  }

  public static <T> MixMulti<T> of(T obj, ExecutorService executorService) {
    return new MixMulti<>(obj, executorService);
  }

  /**
   * 添加任务。前置任务默认为开始节点 无需验证前置任务是否已完成
   *
   * @param name 当前任务标识
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
   * 添加任务。并声明前置任务
   *
   * @param name 当前任务标识
   * @param consumer 任务
   * @param preName 前置任务
   * @return MixMulti
   * @author liuwenhao
   * @date 2022/6/18 17:20
   */
  public MixMulti<T> add(String name, Consumer<T> consumer, String... preName) {

    // 加入到等待集合中
    Set<String> pre = Arrays.stream(preName).collect(Collectors.toSet());
    MixWaitTask<T> mixWaitTask = new MixWaitTask<>(name, consumer, pre);
    waitForTask.add(mixWaitTask);
    return this;
  }

  /**
   * 添加一个异常节点
   *
   * @param fn 异常节点
   * @return com.deep.crow.task.mix.MixMulti<T>
   * @author liuwenhao
   * @date 2022/6/22 16:52
   */
  public MixMulti<T> addThrowable(Consumer<Throwable> fn) {
    this.fn = fn;
    return this;
  }

  /**
   * 强制任务执行 一直等待直到任务完成
   *
   * @author liuwenhao
   * @date 2022/6/20 19:09
   */
  public T exec() {
    recastEnd();
    return obj;
  }

  /**
   * 添加任务
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
    List<MixTask<T>> mixTaskList =
        mixTasks.stream()
            .filter(m -> pre.contains(m.name()))
            .peek(MixTask::cancelTail)
            .collect(Collectors.toList());
    tailMixTasks.removeAll(mixTaskList);
    taskName.add(mixTask.name());
  }

  /**
   * 构建混合任务
   *
   * @param name 任务标识
   * @param consumer 任务体
   * @param pre 前置节点，如果为null则前置节点为
   * @return com.deep.crow.task.mix.MixTask
   * @author liuwenhao
   * @date 2022/6/20 14:47
   */
  private MixTask<T> mixTaskBuilder(String name, Consumer<T> consumer, Set<String> pre) {
    Multi<T> multi =
        MultiHelper.supplyAsync(executorService, () -> obj)
            .thenApply(
                o -> {
                  consumer.accept(o);
                  return o;
                });
    MixTask<T> mixTask = new RunnableMixTask<>(name, multi);
    if (null != fn) {
      addThrowableTask(mixTask);
    }
    if (Objects.nonNull(pre)) {
      for (String s : pre) {
        mixTask.addPreName(s);
      }
    }
    return mixTask;
  }

  /**
   * 验证任务标识是否已经存在
   *
   * @param name 任务名称
   * @author liuwenhao
   * @date 2022/6/18 17:27
   */
  private void checkName(String name) {
    if (taskName.contains(name)) {
      CrowException.of("任务名称{}重复", name);
    }
  }

  /**
   * 验证前置任务是否已经存在 如果不存在则异常
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
   * 对等待中的任务进行重新排查
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
        MixTask<T> mixTask =
            mixTaskBuilder(waitTask.getName(), waitTask.getConsumer(), waitTask.getPreName());
        addTask(mixTask);
        iterator.remove();
      }
    }
    if (flag || (force && !waitForTask.isEmpty())) {
      recastPre(force);
    }
  }

  private void recastEnd() {
    // 为已装配的任务添加异常节点
    mixTasks.stream()
        .filter(t -> !throwableName.contains(t.name()))
        .forEach(this::addThrowableTask);

    while (!waitForTask.isEmpty()) {
      recastPre(true);
    }
    mixTasks.forEach(m -> m.complete(true));
  }

  private synchronized void addThrowableTask(MixTask<T> mixTask) {
    throwableName.add(mixTask.name());
    mixTask
        .multi()
        .exceptionally(
            throwable -> {
              fn.accept(throwable);
              return obj;
            });
  }

  /**
   * 检查前置任务是否已经完成 任务完成的标准是前置任务已完成
   *
   * @param preName 前置任务集合
   * @param force 是否强制
   * @return boolean
   * @author liuwenhao
   * @date 2022/6/20 12:01
   */
  private boolean checkPreBegin(Set<String> preName, boolean force) {
    // 优先判断是否存在等待中的

    List<MixWaitTask<T>> waitTasks =
        waitForTask.stream()
            .filter(m -> preName.contains(m.getName()))
            .collect(Collectors.toList());
    if (!waitTasks.isEmpty()) {
      return false;
    }

    List<MixTask<T>> mixTaskList =
        mixTasks.stream().filter(m -> preName.contains(m.name())).collect(Collectors.toList());
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
