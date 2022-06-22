package com.deep.crow.task.mix;

import com.deep.crow.multi.Multi;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/6/18 16:17
 */
class RunnableMixTask<T> implements MixTask<T> {

    /**
     * 当前任务的名称，即唯一标识
     */
    String name;

    /**
     * 前置任务的唯一标识
     */
    Set<String> preName = new HashSet<>();

    /**
     * 任务体
     */
    Multi<T> multi;

    /**
     * 是否是尾结点,这取决于任务是否存在后置任务，不可随意更改
     */
    @Deprecated
    boolean isTail;

    public RunnableMixTask(String name, Multi<T> multi) {
        this.name = name;
        this.multi = multi;
        this.isTail = true;
    }

    @Override
    public void addPreName(String preName) {
        this.preName.add(preName);
    }

    @Override
    public void removePreName(String preName) {
        this.preName.remove(preName);
    }

    @Deprecated
    @Override
    public boolean isTail() {
        return isTail;
    }

    @Deprecated
    @Override
    public void cancelTail() {
        this.isTail = false;
    }

    @Override
    public Set<String> pre() {
        return preName;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean complete(boolean force) {
        if (force) {
            multi.join();
            return true;
        }
        Object multiNow = multi.getNow(null);
        return multiNow != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RunnableMixTask<T> that = (RunnableMixTask<T>) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }


}