package com.deep.crow;

import com.deep.crow.serial.SerialMulti;

import java.util.concurrent.ExecutorService;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/4/18 2:55
 */
public class SerialMultiTest {
    public static void main(String[] args) {
        SerialMultiTest multiTest = new SerialMultiTest();
        multiTest.addTest();
    }

    public void addTest() {

        ExecutorService executorService = ThreadPool.executorService();

        SerialMulti<Object> of = SerialMulti.of(executorService);
        Integer join = of.add(() -> System.out.println("新增一个 runnable"))
            .add(() -> {
                System.out.println("新增一个 supplier");
                return 1;
            })
            .add(integer -> {
                System.out.println("新增一个 consumer");
                System.out.println(integer);
            })
            .add(() -> 5)
            .add(o -> {
                System.out.println("新增一个 function");
                return 3 + o;
            })
            .addThrowable(throwable -> {
                System.out.println(throwable.getMessage());
                return 5;
            }).join();
        System.out.println(join);
    }

}