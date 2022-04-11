package com.deep.crow;

import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.NamedThreadFactory;

import java.util.concurrent.*;

/**
 * @author Create by liuwenhao on 2022/4/10 10:54
 */
public class ThreadPool {

    public static ExecutorService executorService() {
        ThreadFactory threadFactory = new NamedThreadFactory("Thread-", false);
        return new ThreadPoolExecutor(
            16,
            32,
            500,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            threadFactory,
            new ThreadPoolExecutor.AbortPolicy()
        );
    }

}