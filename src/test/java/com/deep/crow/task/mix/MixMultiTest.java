package com.deep.crow.task.mix;

import com.deep.crow.util.ThreadPool;
import junit.framework.TestCase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * graph LR
 * A[开始] --> B --> D;
 * A --> C;
 * C --> D --> F;
 * A --> E --> G;
 * F --> G
 */
public class MixMultiTest extends TestCase {

    ExecutorService executorService;

    {
        executorService = ThreadPool.executorService();
    }

    public void testAdd() {
        MixMulti mixMulti = new MixMulti(executorService)
            .add("B", () -> wait("B", 3))
            .add("C", () -> wait("C", 2))
            .add("D", () -> wait("D", 0), "B", "C")
            .add("E", () -> wait("E", 0))
            .add("F", () -> wait("F", 1), "D");
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        mixMulti.add("G", () -> wait("G", 0), "E", "F");
        assertEquals(mixMulti.waitForTask.size(), 0);
        assertEquals(mixMulti.mixTasks.size(), 6);
    }


    private void wait(String str, long s) {
        System.out.println(str);
        try {
            TimeUnit.SECONDS.sleep(s);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}