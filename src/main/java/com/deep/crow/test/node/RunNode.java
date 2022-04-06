package com.deep.crow.test.node;

/**
 * <h2>异步任务节点</h2>
 * 异步任务的起始点，也可能是终点<br>
 * 在一次任务的执行中最多只能有一个返回值，和一个起始点，
 *
 * @author Create by liuwenhao on 2022/4/6 19:55
 */
public interface RunNode extends Node {

    /**
     * <h2>执行</h2>
     * 存在一个Void的返回值
     *
     * @return java.lang.Void
     * @author liuwenhao
     * @date 2022/4/6 20:00
     */
    Void getResult();

    /**
     * <h2>执行任务的默认实现</h2>
     *
     * @return T
     * @author liuwenhao
     * @date 2022/4/6 21:20
     */
    @Override
    default Void exec() {
        return getResult();
    }

}