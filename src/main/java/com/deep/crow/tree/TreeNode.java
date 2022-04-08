package com.deep.crow.tree;

import com.deep.crow.exception.CrowException;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <h2>任务节点</h2>
 *
 * @author Create by liuwenhao on 2022/4/6 18:54
 */
public abstract class TreeNode implements Comparable<TreeNode> {

    /**
     * 异常节点的深度 -1，头节点的深度1，其他不确定深度的节点深度0
     */
    static final int THROWABLE = -1;
    static final int UNSTEADY = 0;
    static final int HEADS = 1;
    static AtomicInteger counter = new AtomicInteger(HEADS);

    /**
     * 节点位置<br>
     * 唯一标识
     */
    int pos;

    /**
     * 顺序，当前节点在树中的位置，该位置依赖于其父节点
     */
    int depth = UNSTEADY;

    /**
     * 标识头节点
     */
    TreeNode head;

    /**
     * 标识异常节点
     */
    TreeNode throwable;

    /**
     * 父节点
     */
    TreeNode parent;

    /**
     * 子节点
     */
    Set<TreeNode> children = new HashSet<>();

    /**
     * 返回值
     */
    Object result;

    /**
     * <h2>获取返回值</h2>
     *
     * @return T 返回值类型
     * @author liuwenhao
     * @date 2022/4/7 20:27
     */
    @SuppressWarnings("unchecked")
    public <T> T getResult() {
        return (T) result;
    }

    /**
     * 创建头节点
     */
    TreeNode() {
        this.depth = HEADS;
        this.head = this;
    }

    TreeNode(TreeNode head) {
        this.head = head;
        this.throwable = head.throwable;
    }

    /**
     * <h2>添加一个子节点</h2>
     * 返回子节点
     *
     * @param node 子节点
     * @return 新节点
     * @author liuwenhao
     * @date 2022/4/8 19:16
     */
    public TreeNode addChild(TreeNode node) {
        if (node instanceof ThrowNode) {
            CrowException.of("异常节点不能放入执行链中");
        }
        node.throwable = throwable;
        node.head = this.head;
        node.parent = this;
        node.pos = counter.incrementAndGet();
        node.depth = this.depth + 1;
        children.add(node);
        return node;
    }

    /**
     * <h2>添加一个子节点</h2>
     * 返回当前节点
     *
     * @param node 子节点
     * @return 新节点
     * @author liuwenhao
     * @date 2022/4/8 19:16
     */
    public TreeNode addChildBack(TreeNode node) {
        addChild(node);
        return this;
    }

    /**
     * <h2>添加一个异常</h2>
     * 返回当前节点
     *
     * @param node 子节点
     * @return 新节点
     * @author liuwenhao
     * @date 2022/4/8 19:16
     */
    public TreeNode addThrowable(TreeNode node) {
        if (node instanceof ThrowNode) {
            node.head = this.head;
            node.depth = THROWABLE;
            this.throwable = node.throwable;
        }
        return this;
    }

    /**
     * <h2>检验节点是否存在</h2>
     *
     * @param node 节点
     * @return boolean
     * @author liuwenhao
     * @date 2022/4/8 19:31
     */
    public boolean contains(TreeNode node) {
        return children.contains(node);
    }

    /**
     * <h2>检验节点是否存在</h2>
     *
     * @param pos 节点位置
     * @return boolean
     * @author liuwenhao
     * @date 2022/4/8 19:31
     */
    public boolean contains(int pos) {
        return getChild(pos) != null;
    }

    /**
     * <h2>获取一个节点</h2>
     *
     * @param pos 节点位置
     * @return com.deep.crow.tree.Node
     * @author liuwenhao
     * @date 2022/4/8 19:32
     */
    @Nullable
    public TreeNode getChild(int pos) {
        for (TreeNode child : children) {
            if (child.pos == pos) {
                return child;
            }
        }
        return null;
    }

    /**
     * <h2>获取头结点</h2>
     *
     * @return com.deep.crow.tree.Node
     * @author liuwenhao
     * @date 2022/4/8 19:37
     */
    public TreeNode getHead() {
        return head;
    }

    /**
     * <h2>当前节点是否是头节点</h2>
     *
     * @return boolean
     * @author liuwenhao
     * @date 2022/4/8 19:41
     */
    public boolean isHead() {
        return depth == HEADS && this == head && parent == null;
    }


    /**
     * <h2>当前节点是否是尾节点</h2>
     *
     * @return boolean
     * @author liuwenhao
     * @date 2022/4/8 19:41
     */
    public boolean isTali() {
        return children.isEmpty();
    }

    /**
     * <h2>当前节点是否是异常节点</h2>
     *
     * @return boolean
     * @author liuwenhao
     * @date 2022/4/8 19:48
     */
    public boolean isThrowable() {
        return depth == THROWABLE && this == throwable;
    }

    /**
     * <h2>获取当前节点高度</h2>
     *
     * @return int
     * @author liuwenhao
     * @date 2022/4/8 20:17
     */
    public int getDepth() {
        return depth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TreeNode node = (TreeNode) o;
        return pos == node.pos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos);
    }

    @Override
    public int compareTo(TreeNode o) {
        if (o == null) {
            return 1;
        }
        if (o.pos == this.pos) {
            CrowException.of("存在位置相同的节点");
        }
        return this.pos - o.pos;
    }
}