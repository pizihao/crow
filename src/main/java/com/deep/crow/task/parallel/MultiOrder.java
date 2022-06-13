package com.deep.crow.task.parallel;

import com.deep.crow.multi.Multi;

import java.util.Objects;
import java.util.function.Function;

/**
 * <h2>带有顺序性的Multi</h2>
 *
 * @author Create by liuwenhao on 2022/6/10 15:04
 */
class MultiOrder<T> implements Comparable<MultiOrder<T>> {

    Multi<T> multi;

    int order;

    Function<Throwable, ? extends T> fn;

    public MultiOrder(Multi<T> multi, int order) {
        this.multi = multi;
        this.order = order;
    }

    public Multi<T> getMulti() {
        return multi;
    }

    public void setMulti(Multi<T> multi) {
        this.multi = multi;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Function<Throwable, ? extends T> getFn() {
        return fn;
    }

    public void setFn(Function<Throwable, ? extends T> fn) {
        this.fn = fn;
    }

    /**
     * <h2>判断是否存在异常处理节点</h2>
     *
     * @return boolean
     * @author liuwenhao
     * @date 2022/6/10 16:51
     */
    public boolean isThrowable() {
        return Objects.isNull(fn);
    }

    @Override
    public int compareTo(MultiOrder o) {
        if (Objects.isNull(o)) {
            return -1;
        }
        return (int) (order - o.order);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MultiOrder<T> that = (MultiOrder<T>) o;
        return order == that.order;
    }

    @Override
    public int hashCode() {
        return Objects.hash(order);
    }
}