package com.deep.crow.task.mix;

import com.deep.crow.multi.Multi;
import com.deep.crow.multi.MultiHelper;
import com.deep.crow.util.ThreadPool;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import junit.framework.TestCase;

public class RunnableMixTaskTest extends TestCase {

  public void testComplete() {
    ExecutorService executorService = ThreadPool.executorService();
    Multi<Object> multi =
        MultiHelper.supplyAsync(
            executorService,
            () -> {
              System.out.println("是否执行完成");
              try {
                TimeUnit.SECONDS.sleep(2);
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
              return null;
            });

    RunnableMixTask<Object> runnableMixTask = new RunnableMixTask<>("任务", multi);
    assertFalse(runnableMixTask.complete(false));
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    assertTrue(runnableMixTask.complete(true));
  }
}
