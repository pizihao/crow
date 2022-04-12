package com.deep.crow;

import java.util.concurrent.*;

/**
 * @author Create by liuwenhao on 2022/4/10 10:54
 */
public class ThreadPool {

    public static ExecutorService executorService() {
        return new ThreadPoolExecutor(
            16,
            32,
            500,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadPoolExecutor.AbortPolicy()
        );
    }

}