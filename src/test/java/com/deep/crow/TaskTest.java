package com.deep.crow;

import com.deep.crow.serial.SerialMulti;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/4/11 15:08
 */
public class TaskTest {

    public static void main(String[] args) {
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