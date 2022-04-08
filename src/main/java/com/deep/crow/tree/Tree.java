package com.deep.crow.tree;

import com.deep.crow.exception.CrowException;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import java.util.Collection;

/**
 * <h2>树，用于任务分配</h2>
 * 分配节点时，优先分配的一定是头结点，后来的节点都是从头结点衍生而来<br>
 * 一个节点不可能存在两个父节点，即一个节点不能由两个节点衍生而来，如：
 * <pre>
 *      String a = "hello";
 *      a = "world";
 * </pre>
 * 以上代码中，新的赋值会覆盖旧的值，分配节点时也是如此，新的父节点会覆盖旧的父节点，原先的连接会被删除<br>
 * 这样就保证了传统的树结构不被破坏，避免了多父节点和环的出现<br>
 * 最终的结果数量取决于叶子节点的数量，普通结构的树无法保证顺序，故在每个节点内部维持一个自增的数值来维持顺序<br>
 * 这样在获取一个复杂的树的结果时，可以按照装配的顺序拿到结果<br>
 * 同一高度的节点应该是并发的执行的，故推荐使用层序遍历来搜索整棵树<br>
 * 为了优化广度优先搜索的代码复杂性，故使用{@link Multimap}来管理各个高度的节点<br>
 *
 * @author Create by liuwenhao on 2022/4/6 14:11
 */
public class Tree {

    /**
     * 异常节点的深度 -1，头节点的深度1，其他不确定深度的节点深度0
     */
    static final int THROWABLE = -1;
    static final int UNSTEADY = 0;
    static final int HEADS = 1;

    /**
     * 头节点
     */
    TreeNode head;

    /**
     * 标识异常节点
     */
    TreeNode throwable;

    /**
     * 深度，树的深度
     */
    int depth;

    /**
     * 将各个深度的节点分成不同的队伍，同一个队伍的节点并发执行
     */
    Multimap<Integer, TreeNode> team = HashMultimap.create();

    public Tree(TreeNode head) {
        if (head.getDepth() != HEADS) {
            CrowException.of("头节点高度必须是{}", HEADS);
        }
        this.head = head;
        team.put(HEADS, head);
    }

    /**
     * <h2>在头节点下添加一个节点</h2>
     *
     * @param node 节点
     * @author liuwenhao
     * @date 2022/4/8 20:24
     */
    public void addNode(TreeNode node) {
        if (node instanceof ThrowNode) {
            CrowException.of("异常节点不能放入链中");
        }
    }

    /**
     * <h2>在头节点下添加一个节点</h2>
     *
     * @param node 节点
     * @author liuwenhao
     * @date 2022/4/8 20:24
     */
    public void addNodeForce(TreeNode node) {
        if (node instanceof ThrowNode) {
            CrowException.of("异常节点不能放入链中");
        }
    }

    /**
     * <h2>获取某个深度的节点</h2>
     *
     * @param depth 深度
     * @return java.util.Collection<com.deep.crow.tree.Node>
     * @author liuwenhao
     * @date 2022/4/8 19:59
     */
    public Collection<TreeNode> getDepthNode(int depth) {
        if (team.keys().size() < depth && this.depth < depth) {
            return Lists.newArrayList();
        }
        return team.get(depth);
    }
}