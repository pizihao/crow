package com.deep.crow.completable;

import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

/**
 * <h2>任务实体</h2>
 * <p>将容器中的每一个元素封装为独立的任务个体，他们可以拥有已被定义好的name和执行器</p>
 * <p>注意：同一个容器中任务的类型应保证相同</p>
 * <p>name为各个任务的唯一标识</p>
 *
 * @deprecated {@link com.deep.crow.multi.Multi}
 * @author Create by liuwenhao on 2021/11/26 10:45
 */
@Deprecated
@SuppressWarnings("unused deprecated")
public final class CofTask<T> {

    protected T task;

    protected ExecutorService executorService;

    protected String name;

    private CofTask() {
    }

    public static CofTask<Runnable> buildRun() {
        return new CofTask<>();
    }

    public static CofTask<Supplier<Object>> buildSupply() {
        return new CofTask<>();
    }

    public CofTask<T> task(T task) {
        this.task = task;
        return this;
    }

    public CofTask<T> executorService(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }

    public CofTask<T> name(String name) {
        this.name = name;
        return this;
    }

    public T getTask() {
        return task;
    }

    public void setTask(T task) {
        this.task = task;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof CofTask) {
            CofTask<T> supply = (CofTask<T>) obj;
            return supply.name.equals(this.name);
        }
        return false;
    }


}