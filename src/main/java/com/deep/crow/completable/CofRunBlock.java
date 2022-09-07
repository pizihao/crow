package com.deep.crow.completable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 阻塞的方式执行任务
 *
 * <p>对于程序的整体执行是同步的，但是对于容器内部执行的各个任务来说是异步的
 *
 * <pre>{@code
 *        System.out.println(123);
 *
 *         Integer exec = CofHelper.buildRunBlock()
 *             .register(() -> System.out.println(1))
 *             .register(() -> System.out.println(2))
 *             .exec(); // 2
 *
 *         System.out.println(456);
 * }
 * 其结果为：
 *  123
 *  1
 *  2
 *  456
 *  <b>或</b>
 *  123
 *  2
 *  1
 *  456
 * </pre>
 *
 * @deprecated {@link com.deep.crow.multi.Multi}
 * @author Create by liuwenhao on 2021/12/21 9:37
 */
@Deprecated
@SuppressWarnings("unused deprecated")
class CofRunBlock extends CofRun {

  @Override
  public Integer exec(Predicate<CofTask<Runnable>> predicate) {
    if (Objects.isNull(runs)) {
      return 0;
    }

    CopyOnWriteArrayList<Signature<Void>> signatures =
        runs.stream()
            .filter(predicate)
            .map(
                r -> {
                  if (Objects.nonNull(r.getExecutorService())) {
                    return Signature.build(
                        r.getName(),
                        CompletableFuture.runAsync(r.getTask(), r.getExecutorService()));
                  } else if (Objects.nonNull(executorService)) {
                    return Signature.build(
                        r.getName(), CompletableFuture.runAsync(r.getTask(), executorService));
                  } else {
                    return Signature.build(r.getName(), CompletableFuture.runAsync(r.getTask()));
                  }
                })
            .collect(Collectors.toCollection(CopyOnWriteArrayList::new));

    signatures.forEach(c -> c.getCompletableFuture().join());

    return signatures.size();
  }

  protected CofRunBlock() {
    this.runs = new ArrayList<>();
  }

  protected CofRunBlock(ExecutorService executorService) {
    this.runs = new ArrayList<>();
    this.executorService = executorService;
  }

  protected CofRunBlock(List<CofTask<Runnable>> runs, ExecutorService executorService) {
    this.runs = Objects.nonNull(runs) ? runs : new ArrayList<>();
    this.executorService = executorService;
  }
}
