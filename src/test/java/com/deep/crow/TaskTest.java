package com.deep.crow;

import com.deep.crow.task.serial.SerialMulti;
import junit.framework.TestCase;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/4/11 15:08
 */
public class TaskTest  extends TestCase {

    public void testTask() {
        Integer integer = SerialMulti.of(() -> System.out.println(1))
            .add(unused -> 5)
            .add(i -> {
                System.out.println(i + 5);
                return 10;
            }).add(i -> "s")
            .add(() -> 1).join();

        System.out.println(integer);
    }

}