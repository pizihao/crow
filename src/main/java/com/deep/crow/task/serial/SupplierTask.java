package com.deep.crow.task.serial;

import com.deep.crow.multi.Multi;
import java.util.function.Supplier;

/**
 * 对接{@link Supplier}
 *
 * @author Create by liuwenhao on 2022/4/11 16:12
 */
class SupplierTask<T, R> implements SerialTask<T> {

  Supplier<? super R> supplier;

  public SupplierTask(Supplier<? super R> supplier) {
    this.supplier = supplier;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <U> Multi<U> increase(Multi<T> multi) {
    return (Multi<U>) multi.thenApply(t -> (R) supplier.get());
  }
}
