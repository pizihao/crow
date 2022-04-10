package com.deep.crow.completable;

import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <h2>异步的方式执行任务</h2>
 * <p>直接通过异步化的方式执行任务，返回值为本次容器实际执行的任务数目，返回结果和实际任务的执行均为异步</p>
 * <pre>
 * {@code
 *         System.out.println(123);
 *
 *         Integer exec = CofHelper.buildRunAsync()
 *             .register(() -> System.out.println(1))
 *             .register(() -> System.out.println(2))
 *             .exec(); // 2
 *
 *         System.out.println(456);
 * }
 * 其结果为：
 *  123
 *  456
 *  1
 *  2
 *  <b>或</b>
 *  123
 *  456
 *  2
 *  1
 * </pre>
 *
 * @author Create by liuwenhao on 2021/12/21 9:42
 */
class CofRunAsync extends CofRun {

    @Override
    public Integer exec(Predicate<CofTask<Runnable>> predicate) {
        if (Objects.isNull(runs)) {
            return 0;
        }
        List<CofTask<Runnable>> list = runs.stream().filter(predicate).collect(Collectors.toList());
        list.forEach(r -> {
            if (Objects.nonNull(r.getExecutorService())) {
                CompletableFuture.runAsync(r.getTask(), r.getExecutorService());
            } else if (Objects.nonNull(executorService)) {
                CompletableFuture.runAsync(r.getTask(), executorService);
            } else {
                CompletableFuture.runAsync(r.getTask());
            }
        });
        return list.size();
    }

    protected CofRunAsync() {
        this.runs = Lists.newArrayList();
    }

    protected CofRunAsync(@Nullable ExecutorService executorService) {
        this.runs = Lists.newArrayList();
        this.executorService = executorService;
    }

    protected CofRunAsync(@Nullable List<CofTask<Runnable>> runs, @Nullable ExecutorService executorService) {
        this.runs = Objects.nonNull(runs) ? runs : Lists.newArrayList();
        this.executorService = executorService;
    }

}