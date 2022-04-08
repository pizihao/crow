package com.deep.crow.tree;

import java.util.function.Function;

/**
 * <h2>异常处理节点</h2>
 *
 * @author Create by liuwenhao on 2022/4/8 20:40
 */
public class ThrowNode<T> extends TreeNode {

    Function<? super Throwable, T> function;

    public ThrowNode(Function<Throwable, T> function) {
        this.depth = THROWABLE;
        this.throwable = this;
        this.function = function;
    }

    public ThrowNode(TreeNode head, Function<Throwable, T> function) {
        super(head);
        this.depth = THROWABLE;
        this.throwable = this;
        this.function = function;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getResult() {
        Throwable result = (Throwable) this.result;
        return function.apply(result);
    }
}