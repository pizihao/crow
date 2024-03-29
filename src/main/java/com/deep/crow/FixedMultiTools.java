package com.deep.crow;

import com.deep.crow.multi.Multi;
import com.deep.crow.multi.MultiHelper;
import com.deep.crow.task.mix.MixMulti;
import com.deep.crow.task.parallel.ParallelMulti;
import com.deep.crow.task.serial.SerialMulti;
import com.deep.crow.util.Tuple;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.function.*;

/**
 * 适用于单元模块的辅助工具类，依托于MultiTools实现，但有相对少变的线程池
 *
 * @author Create by liuwenhao on 2022/4/18 11:25
 */
@SuppressWarnings("unused")
public class FixedMultiTools {

  ExecutorService executorService;

  public FixedMultiTools() {
    this.executorService = ForkJoinPool.commonPool();
  }

  public FixedMultiTools(ExecutorService executorService) {
    this.executorService = executorService;
  }

  // ================================Builder====================================

  public static FixedMultiTools builder(ExecutorService executorService) {
    return new FixedMultiTools(executorService);
  }

  public static FixedMultiTools builder() {
    return new FixedMultiTools();
  }

  // ================================Multi====================================

  /**
   * 获取执行结果，通过type匹配
   *
   * @param c 过程集合
   * @param type 结果类型
   * @return Tuple
   * @author Created by liuwenhao on 2022/4/9 22:51
   */
  public static <T> T multipleGet(List<Multi<?>> c, Type type) {
    return MultiTools.multipleGet(c, type);
  }

  /**
   * 获取执行结果，通过type匹配
   *
   * @param c 过程集合
   * @param type 结果类型
   * @return Tuple
   * @author Created by liuwenhao on 2022/4/9 22:51
   */
  public static <T> T multipleGet(List<Multi<?>> c, Supplier<Type> type) {
    return MultiTools.multipleGet(c, type);
  }

  /**
   * 创建一个Multi
   *
   * @param supplier 任务
   * @return com.deep.crow.team.Multi<U>
   * @author liuwenhao
   * @date 2022/4/11 13:50
   */
  public <U> Multi<U> supplyAsync(Supplier<U> supplier) {
    return MultiTools.supplyAsync(executorService, supplier);
  }

  /**
   * 创建一个Multi
   *
   * @param runnable 任务
   * @return com.deep.crow.team.Multi<U>
   * @author liuwenhao
   * @date 2022/4/11 13:50
   */
  public Multi<Void> runAsync(Runnable runnable) {
    return MultiTools.runAsync(executorService, runnable);
  }

  /**
   * 创建一个Multi
   *
   * @return com.deep.crow.team.Multi<U>
   * @author liuwenhao
   * @date 2022/4/11 13:50
   */
  public <T> Multi<T> create() {
    return MultiHelper.create(executorService);
  }

  // ================================SerialMulti====================================

  public <T> SerialMulti<T> serialMulti() {
    return SerialMulti.of(executorService);
  }

  public <T> SerialMulti<T> serialMulti(Supplier<T> supplier) {
    return SerialMulti.of(executorService, supplier);
  }

  public SerialMulti<Void> serialMulti(Runnable runnable) {
    return SerialMulti.of(executorService, runnable);
  }

  public <T> SerialMulti<T> serialMulti(Multi<T> multi) {
    return SerialMulti.of(multi);
  }

  // ================================ParallelMulti====================================

  public ParallelMulti parallelMulti() {
    return ParallelMulti.of(executorService);
  }

  // ================================ParallelMulti====================================

  public <T> MixMulti<T> mixMulti(T obj) {
    return MixMulti.of(obj, executorService);
  }

  // ======================================操作=========================================

  /**
   * 等待两个Multi执行完成，并使用其结果执行任务
   *
   * @param one 第一个串行化任务
   * @param two 第二个串行化任务
   * @param bi 任务体
   * @return R 结果
   * @author Created by liuwenhao on 2022/4/12 23:42
   */
  public <R, M, N> R serialMeet(SerialMulti<M> one, SerialMulti<N> two, BiFunction<M, N, R> bi) {
    return MultiTools.serialMeet(one, two, bi);
  }

  /**
   * 等待两个Multi执行完成，并使用其结果执行任务
   *
   * @param one 第一个串行化任务
   * @param two 第二个串行化任务
   * @param bi 任务体
   * @author Created by liuwenhao on 2022/4/12 23:42
   */
  public <M, N> void serialMeet(SerialMulti<M> one, SerialMulti<N> two, BiConsumer<M, N> bi) {
    MultiTools.serialMeet(one, two, bi);
  }

  /**
   * 获取并行Multi结果并执行任务
   *
   * @param <R> 结果类型
   * @param function 任务
   * @return R 结果
   * @author Created by liuwenhao on 2022/4/12 23:20
   */
  public <R> R parallelTuple(ParallelMulti parallelMulti, Function<? super Tuple, R> function) {
    return MultiTools.parallelTuple(parallelMulti, function);
  }

  /**
   * 获取并行Multi结果并执行任务
   *
   * @param <R> 结果类型
   * @param function 任务
   * @return R 结果
   * @author Created by liuwenhao on 2022/4/12 23:20
   */
  public <R> R parallelList(ParallelMulti parallelMulti, Function<? super List<?>, R> function) {
    return MultiTools.parallelList(parallelMulti, function);
  }

  /**
   * 获取并行Multi结果并执行任务
   *
   * @param consumer 任务
   * @author Created by liuwenhao on 2022/4/12 23:20
   */
  public void parallelTuple(ParallelMulti parallelMulti, Consumer<? super Tuple> consumer) {
    MultiTools.parallelTuple(parallelMulti, consumer);
  }

  /**
   * 获取并行Multi结果并执行任务
   *
   * @param consumer 任务
   * @author Created by liuwenhao on 2022/4/12 23:20
   */
  public void parallelList(ParallelMulti parallelMulti, Consumer<? super List<?>> consumer) {
    MultiTools.parallelList(parallelMulti, consumer);
  }

  /**
   * 并行Multi转化成串行Multi
   *
   * @param <R> 结果类型
   * @param function 任务
   * @return R 结果
   * @author Created by liuwenhao on 2022/4/12 23:20
   */
  public <R> SerialMulti<R> toSerialTuple(
      ParallelMulti parallelMulti, Function<? super Tuple, SerialMulti<R>> function) {
    return MultiTools.toSerialTuple(parallelMulti, function);
  }

  /**
   * 并行Multi转化成串行Multi
   *
   * @param <R> 结果类型
   * @param function 任务
   * @return R 结果
   * @author Created by liuwenhao on 2022/4/12 23:20
   */
  public <R> SerialMulti<R> toSerialList(
      ParallelMulti parallelMulti, Function<? super List<?>, SerialMulti<R>> function) {
    return MultiTools.toSerialList(parallelMulti, function);
  }

  /**
   * 等待装配的任务全部执行，完成后执行给定任务
   *
   * @param supplier 任务
   * @return T 结果
   * @author Created by liuwenhao on 2022/4/12 23:29
   */
  public <T> T parallelApply(ParallelMulti parallelMulti, Supplier<T> supplier) {
    return MultiTools.parallelApply(parallelMulti, supplier);
  }

  /**
   * 等待装配的任务全部执行，完成后执行给定任务
   *
   * @param runnable 任务
   * @author Created by liuwenhao on 2022/4/12 23:29
   */
  public void parallelRun(ParallelMulti parallelMulti, Runnable runnable) {
    MultiTools.parallelRun(parallelMulti, runnable);
  }

  /**
   * 按照参数的顺序执行，同时返回结果，类似于 Collection.stream().map() 如果Multi的返回结果为null,则转化后对应位置的值依然为null
   *
   * @param c 过程集合
   * @return com.deep.crow.team.Multi<java.lang.Void>
   * @author Created by liuwenhao on 2022/4/9 22:51
   */
  public List<Object> multipleList(List<Multi<?>> c) {
    return MultiTools.multipleList(c);
  }

  /**
   * 按照参数的顺序执行，同时返回结果，类似于 Collection.stream().map() 如果Multi的返回结果为null,则转化后对应位置的值依然为null
   *
   * @param c 过程集合
   * @return Tuple
   * @author Created by liuwenhao on 2022/4/9 22:51
   */
  public Tuple multipleTuple(List<Multi<?>> c) {
    return MultiTools.multipleTuple(c);
  }

  /**
   * 填充实例 直接修改参数对象
   *
   * @param c 过程集合
   * @param t 需要填充的实例对象
   * @return T
   * @author liuwenhao
   * @date 2022/4/30 11:21
   */
  public <T> T getForInstance(List<Multi<?>> c, T t) {
    return MultiTools.getForInstance(c, t);
  }

  /**
   * 填充实例
   *
   * @param c 过程集合
   * @param clazz 需要填充的类
   * @return T
   * @author liuwenhao
   * @date 2022/4/30 11:21
   */
  public <T> T getForClass(List<Multi<?>> c, Class<T> clazz) {
    return MultiTools.getForClass(c, clazz);
  }

  /**
   * 批量填充 默认不覆盖
   *
   * @param c 过程集合
   * @param ts 需要填充的类对象集合
   * @return java.util.Collection<T>
   * @author liuwenhao
   * @date 2022/6/7 17:01
   */
  public static <T> Collection<T> batchForInstance(List<Multi<?>> c, Collection<T> ts) {
    return MultiTools.batchForInstance(c, ts);
  }
}
