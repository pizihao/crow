package com.deep.crow.task.serial;

import com.deep.crow.multi.Multi;
import com.deep.crow.task.Task;

/**
 * 串行化任务 T 代表源任务的结果类型
 *
 * @author Create by liuwenhao on 2022/4/11 10:03
 */
interface SerialTask<T> extends Task {

  /**
   * 追加一个任务
   *
   * @param <U> 依赖任务的返回值类型
   * @param multi 一个multi实例
   * @return Multi<U>
   * @author Created by liuwenhao on 2022/4/10 17:23
   */
  <U> Multi<U> increase(Multi<T> multi);
}
