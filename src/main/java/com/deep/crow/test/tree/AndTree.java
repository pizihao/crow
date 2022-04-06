package com.deep.crow.test.tree;

import com.deep.crow.test.node.Node;

/**
 * <h2>&& 树</h2>
 *
 * @author Create by liuwenhao on 2022/4/6 20:21
 */
public class AndTree implements ChainTree<Node> {

    @Override
    public boolean add(Node node, Node parent) {
        return false;
    }

    @Override
    public boolean addThrowable(Node node) {
        return false;
    }

    @Override
    public Node[] nextNode(Node node) {
        return new Node[0];
    }

    @Override
    public int nodeOrder(Node node) {
        return 0;
    }

    @Override
    public Node[] identical(Node node) {
        return new Node[0];
    }

    @Override
    public void sort() {

    }

    @Override
    public boolean check() {
        return false;
    }
}