package com.deep.crow.task.mix;

import com.deep.crow.model.User;
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
        User user = new User();
        MixMulti<User> mixMulti = new MixMulti<>(user, executorService)
            .add("B", o -> wait(o, "B", 30))
            .add("C", o -> wait(o, "C", 20))
            .add("D", o -> wait(o, "D", 0), "B", "C")
            .add("E", o -> wait(o, "E", 0))
            .add("F", o -> wait(o, "F", 10), "D");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        mixMulti.add("G", o -> wait(o, "G", 0), "E", "F");
        mixMulti.exec();
        assertEquals(mixMulti.waitForTask.size(), 0);
        assertEquals(mixMulti.mixTasks.size(), 6);
        assertEquals(user.getName(), "G");
    }


    private void wait(User user, String str, long s) {
        try {
            TimeUnit.SECONDS.sleep(s);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        user.setName(str);
        System.out.println(str);
    }
}