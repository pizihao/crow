package com.deep.crow.completable;

import com.deep.crow.util.Tuple;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

/**
 * 构建Cof扩展器的工具类 建立Cof容器的工具类，详情可见：
 *
 * <ul>
 *   <li>{@link CofSupplyTuple}
 *   <li>{@link CofSupplyMap}
 *   <li>{@link CofRunAsync}
 *   <li>{@link CofRunBlock}
 * </ul>
 *
 * @deprecated {@link com.deep.crow.multi.Multi}
 * @author Create by liuwenhao on 2021/11/26 16:07
 */
@Deprecated
@SuppressWarnings("unused deprecated")
public class CofHelper {
  private CofHelper() {}

  /**
   * 创建一个{@link CofRunBlock}
   *
   * @return cn.net.nova.trm.util.test.Com<java.lang.Runnable, java.lang.Integer>
   * @author liuwenhao
   * @date 2021/11/26 16:14
   */
  public static Cof<Runnable, Integer> buildRunBlock() {
    return new CofRunBlock();
  }

  /**
   * 创建一个{@link CofRunBlock}
   *
   * @param e 执行器
   * @return cn.net.nova.trm.util.test.Com<java.lang.Runnable, java.lang.Integer>
   * @author liuwenhao
   * @date 2021/11/26 16:14
   */
  public static Cof<Runnable, Integer> buildRunBlock(ExecutorService e) {
    return new CofRunBlock(e);
  }

  /**
   * 创建一个{@link CofRunBlock}
   *
   * @param r 任务集合
   * @param e 执行器
   * @return cn.net.nova.trm.util.test.Com<java.lang.Runnable, java.lang.Integer>
   * @author liuwenhao
   * @date 2021/11/26 16:14
   */
  public static Cof<Runnable, Integer> buildRunBlock(List<CofTask<Runnable>> r, ExecutorService e) {
    return new CofRunBlock(r, e);
  }

  /**
   * 创建一个{@link CofRunAsync}
   *
   * @return cn.net.nova.trm.util.test.Com<java.lang.Runnable, java.lang.Integer>
   * @author liuwenhao
   * @date 2021/11/26 16:14
   */
  public static Cof<Runnable, Integer> buildRunAsync() {
    return new CofRunAsync();
  }

  /**
   * 创建一个{@link CofRunAsync}
   *
   * @param e 执行器
   * @return cn.net.nova.trm.util.test.Com<java.lang.Runnable, java.lang.Integer>
   * @author liuwenhao
   * @date 2021/11/26 16:14
   */
  public static Cof<Runnable, Integer> buildRunAsync(ExecutorService e) {
    return new CofRunAsync(e);
  }

  /**
   * 创建一个{@link CofRunAsync}
   *
   * @param r 任务集合
   * @param e 执行器
   * @return cn.net.nova.trm.util.test.Com<java.lang.Runnable, java.lang.Integer>
   * @author liuwenhao
   * @date 2021/11/26 16:14
   */
  public static Cof<Runnable, Integer> buildRunAsync(List<CofTask<Runnable>> r, ExecutorService e) {
    return new CofRunAsync(r, e);
  }

  /**
   * 创建一个{@link CofSupplyMap}
   *
   * @return cn.net.nova.trm.util.test.Com<java.util.function.Supplier <
   *     java.lang.Object>,java.util.Map<java.lang.String,java.lang.Object>>
   * @author liuwenhao
   * @date 2021/11/26 16:14
   */
  public static Cof<Supplier<Object>, Map<String, Object>> buildSupplyMap() {
    return new CofSupplyMap();
  }

  /**
   * 创建一个{@link CofSupplyMap}
   *
   * @param e 执行器
   * @return cn.net.nova.trm.util.test.Com<java.util.function.Supplier <
   *     java.lang.Object>,java.util.Map<java.lang.String,java.lang.Object>>
   * @author liuwenhao
   * @date 2021/11/26 16:14
   */
  public static Cof<Supplier<Object>, Map<String, Object>> buildSupplyMap(ExecutorService e) {
    return new CofSupplyMap(e);
  }

  /**
   * 创建一个{@link CofSupplyMap}
   *
   * @param r 任务集合
   * @param e 执行器
   * @return cn.net.nova.trm.util.test.Com<java.util.function.Supplier <
   *     java.lang.Object>,java.util.Map<java.lang.String,java.lang.Object>>
   * @author liuwenhao
   * @date 2021/11/26 16:14
   */
  public static Cof<Supplier<Object>, Map<String, Object>> buildSupplyMap(
      List<CofTask<Supplier<Object>>> r, ExecutorService e) {
    return new CofSupplyMap(r, e);
  }

  /**
   * 创建一个{@link CofSupplyTuple}
   *
   * @return cn.net.nova.trm.util.test.Com<java.util.function.Supplier <
   *     java.lang.Object>,cn.hutool.core.lang.Tuple>
   * @author liuwenhao
   * @date 2021/11/26 16:14
   */
  public static Cof<Supplier<Object>, Tuple> buildSupplyTuple() {
    return new CofSupplyTuple();
  }

  /**
   * 创建一个{@link CofSupplyTuple}
   *
   * @param e 执行器
   * @return cn.net.nova.trm.util.test.Com<java.util.function.Supplier <
   *     java.lang.Object>,cn.hutool.core.lang.Tuple>
   * @author liuwenhao
   * @date 2021/11/26 16:14
   */
  public static Cof<Supplier<Object>, Tuple> buildSupplyTuple(ExecutorService e) {
    return new CofSupplyTuple(e);
  }

  /**
   * 创建一个{@link CofSupplyTuple}
   *
   * @param r 任务集合
   * @param e 执行器
   * @return cn.net.nova.trm.util.test.Com<java.util.function.Supplier <
   *     java.lang.Object>,cn.hutool.core.lang.Tuple>
   * @author liuwenhao
   * @date 2021/11/26 16:14
   */
  public static Cof<Supplier<Object>, Tuple> buildSupplyTuple(
      List<CofTask<Supplier<Object>>> r, ExecutorService e) {
    return new CofSupplyTuple(r, e);
  }
}
