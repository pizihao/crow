package com.deep.crow.task.serial;

import com.deep.crow.multi.Multi;
import java.util.function.Function;

/**
 * 对接{@link Function}
 *
 * @author Create by liuwenhao on 2022/4/11 10:14
 */
class FunctionTask<T, R> implements SerialTask<T> {

  Function<? super T, ? super R> function;

  public FunctionTask(Function<? super T, ? super R> function) {
    this.function = function;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <U> Multi<U> increase(Multi<T> multi) {
    return (Multi<U>) multi.thenApply(function);
  }
}
