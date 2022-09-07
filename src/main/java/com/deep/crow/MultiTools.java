package com.deep.crow;

import com.deep.crow.compress.TypeUtil;
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
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * 适用于单元模块的辅助工具类
 *
 * @author Create by liuwenhao on 2022/4/12 23:32
 */
@SuppressWarnings("unused")
public class MultiTools {

  private MultiTools() {}

  /**
   * 创建一个Multi
   *
   * @param executorService 线程池
   * @param supplier 任务
   * @return com.deep.crow.team.Multi<U>
   * @author liuwenhao
   * @date 2022/4/11 13:50
   */
  public static <U> Multi<U> supplyAsync(ExecutorService executorService, Supplier<U> supplier) {
    return MultiHelper.supplyAsync(executorService, supplier);
  }

  /**
   * 创建一个Multi
   *
   * @param executorService 线程池
   * @param runnable 任务
   * @return com.deep.crow.team.Multi<U>
   * @author liuwenhao
   * @date 2022/4/11 13:50
   */
  public static Multi<Void> runAsync(ExecutorService executorService, Runnable runnable) {
    return MultiHelper.runAsync(executorService, runnable);
  }

  /**
   * 创建一个Multi
   *
   * @param executorService 线程池
   * @return com.deep.crow.team.Multi<U>
   * @author liuwenhao
   * @date 2022/4/11 13:50
   */
  public static <T> Multi<T> create(ExecutorService executorService) {
    return MultiHelper.create(executorService);
  }

  // ================================SerialMulti====================================

  public static <T> SerialMulti<T> serialMulti(ExecutorService executorService) {
    return SerialMulti.of(executorService);
  }

  public static <T> SerialMulti<T> serialMulti() {
    return SerialMulti.of();
  }

  public static <T> SerialMulti<T> serialMulti(
      ExecutorService executorService, Supplier<T> supplier) {
    return SerialMulti.of(executorService, supplier);
  }

  public static <T> SerialMulti<T> serialMulti(Supplier<T> supplier) {
    return SerialMulti.of(supplier);
  }

  public static SerialMulti<Void> serialMulti(ExecutorService executorService, Runnable runnable) {
    return SerialMulti.of(executorService, runnable);
  }

  public static SerialMulti<Void> serialMulti(Runnable runnable) {
    return SerialMulti.of(runnable);
  }

  public static <T> SerialMulti<T> serialMulti(Multi<T> multi) {
    return SerialMulti.of(multi);
  }

  // ================================ParallelMulti====================================

  public static ParallelMulti parallelMulti(ExecutorService executorService) {
    return ParallelMulti.of(executorService);
  }

  public static ParallelMulti parallelMulti() {
    return ParallelMulti.of();
  }

  // ================================MixMulti====================================

  public static <T> MixMulti<T> mixMulti(T obj, ExecutorService executorService) {
    return MixMulti.of(obj, executorService);
  }

  // ================================操作====================================

  /**
   * 等待两个Multi执行完成，并使用其结果执行任务
   *
   * @param one 第一个串行化任务
   * @param two 第二个串行化任务
   * @param bi 任务体
   * @return R 结果
   * @author Created by liuwenhao on 2022/4/12 23:42
   */
  public static <R, M, N> R serialMeet(
      SerialMulti<M> one, SerialMulti<N> two, BiFunction<M, N, R> bi) {
    return bi.apply(one.join(), two.join());
  }

  /**
   * 等待两个Multi执行完成，并使用其结果执行任务
   *
   * @param one 第一个串行化任务
   * @param two 第二个串行化任务
   * @param bi 任务体
   * @author Created by liuwenhao on 2022/4/12 23:42
   */
  public static <M, N> void serialMeet(
      SerialMulti<M> one, SerialMulti<N> two, BiConsumer<M, N> bi) {
    bi.accept(one.join(), two.join());
  }

  /**
   * 获取并行Multi结果并执行任务
   *
   * @param <R> 结果类型
   * @param function 任务
   * @return R 结果
   * @author Created by liuwenhao on 2022/4/12 23:20
   */
  public static <R> R parallelTuple(
      ParallelMulti parallelMulti, Function<? super Tuple, R> function) {
    return parallelMulti.thenExecTuple(function);
  }

  /**
   * 获取并行Multi结果并执行任务
   *
   * @param <R> 结果类型
   * @param function 任务
   * @return R 结果
   * @author Created by liuwenhao on 2022/4/12 23:20
   */
  public static <R> R parallelList(
      ParallelMulti parallelMulti, Function<? super List<?>, R> function) {
    return parallelMulti.thenExecList(function);
  }

  /**
   * 获取并行Multi结果并执行任务
   *
   * @param consumer 任务
   * @author Created by liuwenhao on 2022/4/12 23:20
   */
  public static void parallelTuple(ParallelMulti parallelMulti, Consumer<? super Tuple> consumer) {
    parallelMulti.thenExecTuple(consumer);
  }

  /**
   * 获取并行Multi结果并执行任务
   *
   * @param consumer 任务
   * @author Created by liuwenhao on 2022/4/12 23:20
   */
  public static void parallelList(ParallelMulti parallelMulti, Consumer<? super List<?>> consumer) {
    parallelMulti.thenExecList(consumer);
  }

  /**
   * 并行Multi转化成串行Multi
   *
   * @param <R> 结果类型
   * @param function 任务
   * @return R 结果
   * @author Created by liuwenhao on 2022/4/12 23:20
   */
  public static <R> SerialMulti<R> toSerialTuple(
      ParallelMulti parallelMulti, Function<? super Tuple, SerialMulti<R>> function) {
    return parallelMulti.toSerialTuple(function);
  }

  /**
   * 并行Multi转化成串行Multi 差强人意的装换方式，粗暴的将并行任务的结果整合到了一起
   *
   * @param <R> 结果类型
   * @param function 任务
   * @return R 结果
   * @author Created by liuwenhao on 2022/4/12 23:20
   */
  public static <R> SerialMulti<R> toSerialList(
      ParallelMulti parallelMulti, Function<? super List<?>, SerialMulti<R>> function) {
    return parallelMulti.toSerialList(function);
  }

  /**
   * 等待装配的任务全部执行，完成后执行给定任务
   *
   * @param supplier 任务
   * @return T 结果
   * @author Created by liuwenhao on 2022/4/12 23:29
   */
  public static <T> T parallelApply(ParallelMulti parallelMulti, Supplier<T> supplier) {
    return parallelMulti.thenApply(supplier);
  }

  /**
   * 等待装配的任务全部执行，完成后执行给定任务
   *
   * @param runnable 任务
   * @author Created by liuwenhao on 2022/4/12 23:29
   */
  public static void parallelRun(ParallelMulti parallelMulti, Runnable runnable) {
    parallelMulti.thenRun(runnable);
  }

  /**
   * 按照参数的顺序执行，同时返回结果，类似于 Collection.stream().map() 如果Multi的返回结果为null,则转化后对应位置的值依然为null
   *
   * @param c 过程集合
   * @return com.deep.crow.team.Multi<java.lang.Void>
   * @author Created by liuwenhao on 2022/4/9 22:51
   */
  public static List<Object> multipleList(List<Multi<?>> c) {
    return c.stream().map(Multi::join).collect(Collectors.toList());
  }

  /**
   * 按照参数的顺序执行，同时返回结果，类似于 Collection.stream().map() 如果Multi的返回结果为null,则转化后对应位置的值依然为null
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

  /**
   * 获取执行结果，通过type匹配
   *
   * @param c 过程集合
   * @param type 结果类型
   * @return Tuple
   * @author Created by liuwenhao on 2022/4/9 22:51
   */
  public static <T> T multipleGet(List<Multi<?>> c, Type type) {
    return TypeUtil.screenType(multipleList(c), type);
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
    return TypeUtil.screenType(multipleList(c), type.get());
  }

  /**
   * 填充实例 如果在填充之前t中部分属性存在值，则不会覆盖其中的值
   *
   * @param c 过程集合
   * @param t 需要填充的实例对象
   * @return T
   * @author liuwenhao
   * @date 2022/4/30 11:21
   */
  public static <T> T getForInstance(List<Multi<?>> c, T t) {
    TypeUtil.fillInstance(multipleList(c), t, false);
    return t;
  }

  /**
   * 填充实例 会对传入的类对象进行实例化，如果clazz没有对应的无参构造器则会实例化失败
   *
   * @param c 过程集合
   * @param clazz 需要填充的类
   * @return T
   * @author liuwenhao
   * @date 2022/4/30 11:21
   */
  public static <T> T getForClass(List<Multi<?>> c, Class<T> clazz) {
    return TypeUtil.fillClass(multipleList(c), clazz, false);
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
    return TypeUtil.fillCollection(multipleList(c), ts, false);
  }
}
