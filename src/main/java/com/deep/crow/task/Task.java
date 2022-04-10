package com.deep.crow.task;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Create by liuwenhao on 2022/4/9 21:48
 */
public interface Task {

    /**
     * <h2>获取执行结果</h2>
     * 如果任务本身不存在返回值，则返回null
     *
     * @return T
     * @author Created by liuwenhao on 2022/4/10 17:23
     */
    <T> T result();

}