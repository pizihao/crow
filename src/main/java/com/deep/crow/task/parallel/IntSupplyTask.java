package com.deep.crow.task.parallel;

import com.deep.crow.multi.Multi;
import com.deep.crow.multi.MultiHelper;
import java.util.concurrent.ExecutorService;
import java.util.function.IntSupplier;

/**
 * 可以得到一个int的异步计算
 *
 * @author Create by liuwenhao on 2022/4/20 19:28
 */
class IntSupplyTask implements ParallelTask {

  int order;
  IntSupplier intSupplier;
  ExecutorService executorService;

  public IntSupplyTask(int order, IntSupplier intSupplier, ExecutorService executorService) {
    this.order = order;
    this.intSupplier = intSupplier;
    this.executorService = executorService;
  }

  public IntSupplyTask(IntSupplier intSupplier, ExecutorService executorService) {
    this.intSupplier = intSupplier;
    this.executorService = executorService;
  }

  @Override
  public int order() {
    return order;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <U> Multi<U> assembling() {
    return (Multi<U>) MultiHelper.supplyAsync(executorService, () -> intSupplier.getAsInt());
  }
}
