package com.deep.crow.task.parallel;

import com.deep.crow.multi.Multi;
import com.deep.crow.multi.MultiHelper;
import java.util.concurrent.ExecutorService;
import java.util.function.LongSupplier;

/**
 * 可以得到一个long的异步计算
 *
 * @author Create by liuwenhao on 2022/4/20 19:32
 */
class LongSupplyTask implements ParallelTask {

  int order;
  LongSupplier longSupplier;
  ExecutorService executorService;

  public LongSupplyTask(int order, LongSupplier longSupplier, ExecutorService executorService) {
    this.order = order;
    this.longSupplier = longSupplier;
    this.executorService = executorService;
  }

  public LongSupplyTask(LongSupplier longSupplier, ExecutorService executorService) {
    this.longSupplier = longSupplier;
    this.executorService = executorService;
  }

  @Override
  public int order() {
    return order;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <U> Multi<U> assembling() {
    return (Multi<U>) MultiHelper.supplyAsync(executorService, longSupplier::getAsLong);
  }
}
