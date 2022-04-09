package com.deep.crow.tree;

import java.util.function.Function;

/**
 * <h2>异常处理节点</h2>
 *
 * @author Create by liuwenhao on 2022/4/8 20:40
 */
public class ThrowNode<T> extends TreeNode {

    Function<? super Throwable, T> function;

    TreeNode node;

    public ThrowNode(Function<? super Throwable, T> function) {
        this.depth = THROWABLE;
        this.throwable = this;
        this.function = function;
    }

    public ThrowNode(TreeNode head, Function<? super Throwable, T> function) {
        super(head);
        this.depth = THROWABLE;
        this.throwable = this;
        this.function = function;
    }

    public void setNode(TreeNode node) {
        this.node = node;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getResult() {
        Throwable result = (Throwable) node.result;
        T t = function.apply(result);
        throwable.result = t;
        return t;
    }
}