package com.deep.crow.task.mix;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <h2>混合等待任务</h2>
 * 在混合任务装配前需要验证前置任务是否已经结束
 * 如果没有结束则暂存于本对象中
 *
 * @author Create by liuwenhao on 2022/6/20 13:05
 */
public class MixWaitTask<T> {

    String name;
    Consumer<T> consumer;
    Set<String> preName;

    public MixWaitTask(String name, Consumer<T> consumer, Set<String> preName) {
        this.name = name;
        this.consumer = consumer;
        this.preName = preName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Consumer<T> getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    public Set<String> getPreName() {
        return preName;
    }

    public void setPreName(Set<String> preName) {
        this.preName = preName;
    }

}