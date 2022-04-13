package com.deep.crow.tree;

import com.deep.crow.multi.Multi;
import com.deep.crow.task.parallel.ParallelMulti;
import com.deep.crow.task.serial.SerialMulti;

import java.util.List;
import java.util.Objects;

/**
 * <h2>一个树形结构</h2>
 * 每个节点有零个或多个子节点；<br>
 * 没有父节点的节点称为根节点；<br>
 * 每一个非根节点有且只有一个父节点；<br>
 * 除了根节点外，每个子节点可以分为多个不相交的子树；<br>
 * <hr>
 * =================================================================
 * <hr>
 * 按照树的定义，深度为1时的树仅有一个节点。其坐标为(1,1)。从第二层开始就会出现无限个节点，其坐标为(2,x)<br>
 * 以此类推，存在于树的第n层的坐标为(n,x)，位于下层的节点一定会有一个父节点，现在定义，父节点是子节点的<br>
 * 依赖任务，即只有子节点完成之后才可以执行父节点，这样某一个节点的子节点就组成了一个{@link ParallelMulti}，<br>
 * 该节点与其兄弟节点也组成了一个{@link ParallelMulti}，一个节点和其下面所属的所有节点就组成了一个<br>
 * {@link SerialMulti}，从整体来看，一棵树就是一个{@link SerialMulti}
 * <hr>
 * =================================================================
 * <hr>
 * 按照以上定义，树的根节点就是整个树最终执行的任务，按照常理来说它是应该被最后创建的，但是对于一颗树来<br>
 * 说，根节点是必须优先定义的，所以无法使用常规的树来定义任务的串并联结构，但严格来说这依然是一个树，只<br>
 * 不过树中节点的高度和坐标需要是动态的，在某个节点的父节点发生改变时需要该节点和其子节点的坐标同时发生<br>
 * 变化，同时没有父节点的节点可能会出现多个，但一棵树只能有一个根节点，所以需要设置一个虚拟的根节点，来<br>
 * 作为这些节点的父节点，如果根节点已经存在了，那就直接指向根节点，如果不存在就使用虚拟节点
 *
 * @author Create by liuwenhao on 2022/4/13 0:04
 */
public class MultiTree {

    // ========================数据部分=========================

    /**
     * 执行过程
     */
    Multi<?> multi;

    // ========================节点部分=========================

    /**
     * 根节点
     */
    MultiTree root;

    /**
     * 父节点
     */
    MultiTree parent;

    /**
     * 子节点们
     */
    List<MultiTree> children;

    /**
     * 坐标
     */
    Coordinate coordinate;

    /**
     * <h2>生成头结点</h2>
     *
     * @param multi 执行过程
     * @author liuwenhao
     * @date 2022/4/13 15:52
     */
    public MultiTree(Multi<?> multi) {
        this(multi, null, 1, 1);
        this.root = this;
    }

    public MultiTree(Multi<?> multi, MultiTree root, MultiTree parent, int x, int y) {
        this.multi = multi;
        this.root = root;
        this.parent = parent;
        this.coordinate = new Coordinate(y, x);
    }

    /**
     * <h2>如果不指定父节点，那么根节点就是父节点</h2>
     *
     * @param root 根节点
     * @param x    x坐标
     * @param y    深度坐标
     * @author liuwenhao
     * @date 2022/4/13 14:50
     */
    public MultiTree(Multi<?> multi, MultiTree root, int x, int y) {
        this(multi, root, root, x, y);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MultiTree multiTree = (MultiTree) o;
        return coordinate.equals(multiTree.coordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate);
    }
}