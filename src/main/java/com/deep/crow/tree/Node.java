package com.deep.crow.tree;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * <h2>任务节点</h2>
 *
 * @author Create by liuwenhao on 2022/4/6 18:54
 */
public abstract class Node {

    /**
     * 节点名<br>
     * 唯一标识
     */
    String name;

    /**
     * 顺序<br>
     * <ul>
     *     <li>顺序的定义对于多线程情况下的串行化和并行化的交汇关系非常关键</li>
     *     <li>串行化情况下顺序就是链执行的顺序，这个顺序是系统自动加的，从小到大以一个单位的步长均匀的赋值给每一个节点</li>
     *     <li>因为串行化的链状关系是系统自主维系的，所以并不需要刻意的去维持order。<</li>
     *     <li>对于串行化和并行化同时存在的节点关系，可以维持成多个树的结构，也就是森林。</li>
     *     <li>注意：这个结构的内部不能出现环，出现环代表着死锁，届时会以异常的形式提示。</li>
     *     <li>在出现森林的情况下，order表示着当前节点在整个树中的位置，即树的深度，在这种情况下，会出现两个树的交汇。</li>
     *     <li>交汇出现后，就意味着结构不再是一个普通的树，但又没有必要因单向的交汇点将结构延展成一个图。</li>
     *     <li>引入order表示顺序，order在一次树的排列中只能出现一个取值，如果在其所在的链中出现排序错误的显现，说明出现了环。</li>
     *     <li>相同数值的node会并发的执行，order相对较大的node会延后执行</li>
     *     <li>在一次节点的排序中如果同一个节点出现了多个值，那么大的order覆盖小的order</li>
     *     <li>一次操作只能操作一个链</li>
     * </ul>
     */
    long order = -1;

    /**
     * 下一层节点<br>
     * 当前节点执行完成后会传播到下层节点，如果不存在下层节点则任务完成，将当前最终的结果到树中
     */
    Set<Node> next = new HashSet<>();

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
    public Object getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Node node = (Node) o;
        return name.equals(node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}