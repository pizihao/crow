package com.deep.crow.tree;

/**
 * <h2>异步任务节点</h2>
 *
 * @author Create by liuwenhao on 2022/4/8 20:28
 */
public class RunNode extends TreeNode {

    Runnable runnable;

    public RunNode(Runnable runnable) {
        this.runnable = runnable;
    }

    public RunNode(TreeNode head, Runnable runnable) {
        super(head);
        this.runnable = runnable;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getResult() {
        runnable.run();
        return (T) result;
    }
}