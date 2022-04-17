package com.deep.crow;

import com.deep.crow.parallel.ParallelMulti;

/**
 * <h2>test</h2>
 *
 * @author Create by liuwenhao on 2022/4/2 16:08
 */
public class ParallelMultiTest {

    public static void main(String[] args) {
        ParallelMulti.of()
            .add(() -> 1/0)
            .add(throwable -> {
                System.out.println(1);
                return 1;
            });
    }
}