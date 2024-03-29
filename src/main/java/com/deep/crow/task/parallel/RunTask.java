package com.deep.crow.task.parallel;

import com.deep.crow.multi.Multi;
import com.deep.crow.multi.MultiHelper;
import java.util.concurrent.ExecutorService;

/**
 * 异步任务
 *
 * @author Create by liuwenhao on 2022/4/11 11:44
 */
class RunTask implements ParallelTask {

  int order;
  Runnable runnable;
  ExecutorService executorService;

  public RunTask(int order, Runnable runnable, ExecutorService executorService) {
    this.order = order;
    this.runnable = runnable;
    this.executorService = executorService;
  }

  public RunTask(Runnable runnable, ExecutorService executorService) {
    this.runnable = runnable;
    this.executorService = executorService;
  }

  @Override
  public int order() {
    return order;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <U> Multi<U> assembling() {
    return (Multi<U>) MultiHelper.runAsync(executorService, runnable);
  }
}
