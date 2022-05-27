package com.deep.crow.queue;

import com.deep.crow.task.Task;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 * <h2>任务队列</h2>
 *
 * @author Create by liuwenhao on 2022/5/26 11:36
 */
public class TaskQueue implements Queue<Task> {

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<Task> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(Task task) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Task> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean offer(Task task) {
        return false;
    }

    @Override
    public Task remove() {
        return null;
    }

    @Override
    public Task poll() {
        return null;
    }

    @Override
    public Task element() {
        return null;
    }

    @Override
    public Task peek() {
        return null;
    }
}