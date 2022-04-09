package com.deep.crow.tree;

import java.util.function.Supplier;

/**
 * <h2>异步计算任务节点</h2>
 *
 * @author Create by liuwenhao on 2022/4/9 14:19
 */
public class ApplyNode<T> extends TreeNode {
    Supplier<T> supplier;

    public ApplyNode(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public ApplyNode(TreeNode head, Supplier<T> supplier) {
        super(head);
        this.supplier = supplier;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getResult() {
        T t = supplier.get();
        result = t;
        return t;
    }
}