package com.deep.crow.completable;

import com.sun.istack.internal.NotNull;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * <h2>Runnable的实现</h2>
 * <p>{@link Runnable}本身无返回值，故定义整个容器中本次执行的任务个数为其返回值</p>
 * <p>如果不在程序中进行等待则无法得知任务是否运行完成</p>
 *
 * @author Create by liuwenhao on 2021/11/26 11:18
 */
abstract class CofRun implements Cof<Runnable, Integer> {

    protected List<CofTask<Runnable>> runs;

    protected ExecutorService executorService;

    @Override
    public Cof<Runnable, Integer> register(CofTask<Runnable> e) {
        runs.add(e);
        return this;
    }

    @Override
    public Cof<Runnable, Integer> registers(List<CofTask<Runnable>> e) {
        this.runs.addAll(e);
        return this;
    }

    @Override
    public Cof<Runnable, Integer> register(@NotNull Runnable r) {
        runs.add(CofTask.buildRun().task(r));
        return this;
    }

    @Override
    public Cof<Runnable, Integer> register(@NotNull Runnable r, @NotNull ExecutorService e) {
        runs.add(CofTask.buildRun().task(r).executorService(e));
        return this;
    }
}