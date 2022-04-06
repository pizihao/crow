package com.deep.crow.test.node;

/**
 * <h2>异步计算节点</h2>
 *
 * @author Create by liuwenhao on 2022/4/6 19:55
 */
public interface SupplyNode extends Node {

    /**
     * <h2>获取结果</h2>
     * 区分于Void
     *
     * @return T
     * @author liuwenhao
     * @date 2022/4/6 19:57
     */
    <T> T getResult();

    /**
     * <h2>执行任务的默认实现</h2>
     *
     * @return T
     * @author liuwenhao
     * @date 2022/4/6 21:20
     */
    @Override
    default <T> T exec() {
        return getResult();
    }
}