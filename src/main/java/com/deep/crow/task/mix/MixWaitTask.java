package com.deep.crow.task.mix;

import java.util.Set;

/**
 * <h2>混合等待任务</h2>
 * 在混合任务装配前需要验证前置任务是否已经结束
 * 如果没有结束则暂存于本对象中
 *
 * @author Create by liuwenhao on 2022/6/20 13:05
 */
public class MixWaitTask {

    String name;
    Runnable runnable;
    Set<String> preName;

    public MixWaitTask(String name, Runnable runnable, Set<String> preName) {
        this.name = name;
        this.runnable = runnable;
        this.preName = preName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public Set<String> getPreName() {
        return preName;
    }

    public void setPreName(Set<String> preName) {
        this.preName = preName;
    }

}