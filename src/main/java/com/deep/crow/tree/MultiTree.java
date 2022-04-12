package com.deep.crow.tree;

import com.deep.crow.task.parallel.ParallelMulti;
import com.deep.crow.task.serial.SerialMulti;

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
 *
 * @author Create by liuwenhao on 2022/4/13 0:04
 */
public class MultiTree {

    /**
     * 根节点
     */
    MultiNode root;






}