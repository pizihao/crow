package com.deep.crow.tree;

import java.util.function.Function;

/**
 * <h2>异步计算任务</h2>
 *
 * @author Create by liuwenhao on 2022/4/9 14:48
 */
public class ThenApplyNode<T, R> extends TreeNode {

    Function<T, R> function;

    public ThenApplyNode(Function<T, R> function) {
        this.function = function;
    }

    public ThenApplyNode(TreeNode head, Function<T, R> function) {
        super(head);
        this.function = function;
    }

    @Override
    @SuppressWarnings("unchecked")
    public R getResult() {
        TreeNode parent = parentNodeJust();
        R r = function.apply((T) parent.result);
        result = r;
        return r;
    }
}