package com.deep.crow.task.serial;

import com.deep.crow.multi.Multi;

/**
 * 对接{@link Runnable}
 *
 * @author Create by liuwenhao on 2022/4/11 10:44
 */
class RunnableTask<T> implements SerialTask<T> {

  Runnable runnable;

  public RunnableTask(Runnable runnable) {
    this.runnable = runnable;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <U> Multi<U> increase(Multi<T> multi) {
    return (Multi<U>) multi.thenRun(runnable);
  }
}
