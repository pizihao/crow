package com.deep.crow.tree;

import com.deep.crow.exception.CrowException;
import com.deep.crow.task.parallel.ParallelMulti;
import com.deep.crow.task.serial.SerialMulti;
import com.deep.crow.util.Coordinate;
import com.deep.crow.util.MapCoordinate;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <h2>一个树形结构</h2>
 * 每个节点有零个或多个子节点；<br>
 * 没有父节点的节点称为根节点；<br>
 * 每一个非根节点有且只有一个父节点；<br>
 * 除了根节点外，每个子节点可以分为多个不相交的子树；<br>
 * <hr>
 * =================================================================
 * <hr>
 * 按照树的定义，深度为1时的树仅有一个节点。其坐标为(0,0)。从第二层开始就会出现无限个节点，其坐标为(1,x)
 * 以此类推，存在于树的第n层的坐标为(n-1,x)，位于下层的节点一定会有一个父节点，现在定义，父节点是子节点的
 * 依赖任务，即只有子节点完成之后才可以执行父节点，这样某一个节点的子节点就组成了一个{@link ParallelMulti}，
 * 该节点与其兄弟节点也组成了一个{@link ParallelMulti}，一个节点和其下面所属的所有节点就组成了一个
 * {@link SerialMulti}，从整体来看，一棵树就是一个{@link SerialMulti}
 * <hr>
 * =================================================================
 * <hr>
 * 按照以上定义，树的根节点就是整个树最终执行的任务，按照常理来说它是应该被最后创建的，但是对于一颗树来
 * 说，根节点是必须优先定义的，所以无法使用常规的树来定义任务的串并联结构，但严格来说这依然是一个树，只
 * 不过树中节点的高度和坐标需要是动态的，在某个节点的父节点发生改变时需要该节点和其子节点的坐标同时发生
 * 变化，同时没有父节点的节点可能会出现多个，但一棵树只能有一个根节点，所以需要设置一个虚拟的根节点，来
 * 作为这些节点的父节点，如果根节点已经存在了，那就直接指向根节点，如果不存在就使用虚拟节点
 *
 * @author Create by liuwenhao on 2022/4/13 0:04
 */
@SuppressWarnings("unused")
public final class MultiTree {

    // ========================数据部分=========================

    /**
     * 串行化的执行过程
     */
    SerialMulti<?> serialMulti;

    /**
     * 异常节点
     */
    Function<Throwable, ?> throwableNode;

    // ========================节点部分=========================

    /**
     * 父节点
     */
    MultiTree parent;

    /**
     * 子节点们
     */
    List<MultiTree> children = new ArrayList<>();

    /**
     * 横坐标
     */
    int x;

    /**
     * 纵坐标
     */
    int y;

    /**
     * 坐标系
     */
    final Coordinate<MultiTree> coordinate;

    /**
     * <h2>生成头结点</h2>
     *
     * @param serialMulti 执行过程
     * @author liuwenhao
     * @date 2022/4/13 15:52
     */
    public MultiTree(SerialMulti<?> serialMulti) {
        this.serialMulti = serialMulti;
        this.coordinate = MapCoordinate.create();
        addChild(0, 0, this);
    }

    public MultiTree(SerialMulti<?> serialMulti, MultiTree parent, int x, int y) {
        if (parent == null) {
            throw CrowException.exception("请指明父节点");
        }
        this.serialMulti = serialMulti;
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.coordinate = parent.coordinate;
    }

    /**
     * <h2>以当前节点为父节点添加一个元素</h2>
     * 添加成功时会覆盖其对应坐标的元素<br>
     * 新元素的横坐标由其父节点推算
     *
     * @param serialMulti 执行过程
     * @param y           纵坐标
     * @return 新的元素
     * @author liuwenhao
     * @date 2022/4/14 20:22
     */
    public MultiTree addElement(SerialMulti<?> serialMulti, int y) {
        int x = this.x + 1;
        MultiTree multiTree = new MultiTree(serialMulti, this, x, y);
        this.children.add(multiTree);
        addChild(x, y, multiTree);
        return multiTree;
    }

    /**
     * <h2>指定父节点添加一个元素</h2>
     * 添加成功时会覆盖其对应坐标的元素<br>
     * 新元素的横坐标由其父节点推算
     *
     * @param serialMulti 执行过程
     * @param parent      父节点
     * @param y           纵坐标
     * @return 新的元素
     * @author liuwenhao
     * @date 2022/4/14 20:22
     */
    public MultiTree addElement(SerialMulti<?> serialMulti, MultiTree parent, int y) {
        return addElement(serialMulti, parent.getX(), parent.getY(), y);
    }

    /**
     * <h2>指定父节点添加一个元素</h2>
     * 添加成功时会覆盖其对应坐标的元素<br>
     * 新元素的横坐标由其父节点推算<br>
     * 付过父节点不存在则添加到根节点上
     *
     * @param serialMulti 执行过程
     * @param xParent     父节点横坐标
     * @param yParent     父节点纵坐标
     * @param y           当前节点纵坐标
     * @return 新的元素
     * @author liuwenhao
     * @date 2022/4/15 16:39
     */
    public MultiTree addElement(SerialMulti<?> serialMulti, int xParent, int yParent, int y) {
        int x = xParent + 1;
        MultiTree parent = coordinate.get(0, 0);
        if (coordinate.contains(xParent, yParent)) {
            parent = coordinate.get(xParent, yParent);
            x = 1;
        }
        MultiTree multiTree = new MultiTree(serialMulti, parent, x, y);
        parent.children.add(multiTree);
        addChild(x, y, multiTree);
        return multiTree;
    }

    /**
     * <h2>指定父节点添加一个元素</h2>
     * 添加成功时会覆盖其对应坐标的元素<br>
     * 新元素的横坐标由其父节点推算<br>
     * 付过父节点不存在则添加到根节点上
     *
     * @param serialMulti 执行过程
     * @param xParent     父节点横坐标
     * @param yParent     父节点纵坐标
     * @param y           当前节点纵坐标
     * @return 调用的元素
     * @author liuwenhao
     * @date 2022/4/15 16:39
     */
    public MultiTree addElementCall(SerialMulti<?> serialMulti, int xParent, int yParent, int y) {
        addElement(serialMulti, xParent, yParent, y);
        return this;
    }

    /**
     * <h2>以当前节点为父节点添加一个元素</h2>
     * 新元素的横坐标由其父节点推算<br>
     * 默认取父节点下一层结构最新的坐标位置
     *
     * @param serialMulti 执行过程
     * @return 新的元素
     * @author liuwenhao
     * @date 2022/4/15 16:39
     */
    public MultiTree addElement(SerialMulti<?> serialMulti) {
        int x = this.x + 1;
        Map<Integer, MultiTree> yMap = coordinate.y(x);
        int y = yMap.size() + 1;
        return addElement(serialMulti, this, y);
    }

    /**
     * <h2>以当前节点为父节点添加一个元素</h2>
     * 新元素的横坐标由其父节点推算<br>
     * 默认取父节点下一层结构最新的坐标位置
     *
     * @param runnable 任务
     * @return 新的元素
     * @author liuwenhao
     * @date 2022/4/15 16:39
     */
    public MultiTree addElement(Runnable runnable) {
        SerialMulti<?> serialMulti = SerialMulti.of(runnable);
        return addElement(serialMulti);
    }

    /**
     * <h2>以当前节点为父节点添加一个元素</h2>
     * 新元素的横坐标由其父节点推算<br>
     * 默认取父节点下一层结构最新的坐标位置
     *
     * @param supplier 任务
     * @return 新的元素
     * @author liuwenhao
     * @date 2022/4/15 16:39
     */
    public MultiTree addElement(Supplier<?> supplier) {
        SerialMulti<?> serialMulti = SerialMulti.of(supplier);
        return addElement(serialMulti);
    }

    /**
     * <h2>添加一个异常元素</h2>
     * 独立于所有节点外的异常节点，在形成最终的串行Multi时会拼接在最后
     *
     * @param function 异常处理
     * @return 新的元素
     * @author liuwenhao
     * @date 2022/4/15 16:39
     */
    public MultiTree addThrowableElement(Function<Throwable, ?> function) {
        this.throwableNode = function;
        return this;
    }


    /**
     * <h2>更换serialMulti并返回之前的serialMulti</h2>
     *
     * @param serialMulti 新的serialMulti
     * @return com.deep.crow.multi.Multi<T>
     * @author liuwenhao
     * @date 2022/4/15 16:10
     */
    @SuppressWarnings("unchecked")
    public <T> SerialMulti<T> setObj(SerialMulti<?> serialMulti) {
        SerialMulti<T> tMulti = (SerialMulti<T>) this.serialMulti;
        this.serialMulti = serialMulti;
        tMulti.addThrowable((Function<Throwable, T>) throwableNode);
        return tMulti;
    }

    /**
     * <h2>返回当前的SerialMulti</h2>
     *
     * @return SerialMulti
     * @author liuwenhao
     * @date 2022/4/15 16:10
     */
    @SuppressWarnings("unchecked")
    public <T> SerialMulti<T> getSerialMulti() {
        SerialMulti<T> tMulti = (SerialMulti<T>) this.serialMulti;
        tMulti.addThrowable((Function<Throwable, T>) throwableNode);
        return tMulti;
    }

    /**
     * <h2>返回当前的父节点</h2>
     *
     * @return com.deep.crow.tree.MultiTree
     * @author liuwenhao
     * @date 2022/4/15 16:10
     */
    public MultiTree getParent() {
        return parent;
    }

    /**
     * <h2>返回当前节点的所有子节点</h2>
     *
     * @return com.deep.crow.tree.MultiTree
     * @author liuwenhao
     * @date 2022/4/15 16:10
     */
    public List<MultiTree> getChildren() {
        return children;
    }

    /**
     * <h2>返回当前节点的存在的子节点数目/h2>
     *
     * @return int
     * @author liuwenhao
     * @date 2022/4/15 16:10
     */
    public int getChildrenSize() {
        return children.size();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private void addChild(int x, int y, MultiTree multiTree) {
        synchronized (coordinate) {
            coordinate.put(x, y, multiTree);
        }
    }

    /**
     * <h2>获取最终的执行结果</h2>
     * 最终会得到一个ParallelMulti，如果存在主节点，那么仅有一个并行任务<br>
     * ParallelMulti 的构建是从上到下的，横坐标越高的节点越会被优先使用<br>
     *
     * @return com.deep.crow.multi.Multi<T>
     * @author liuwenhao
     * @date 2022/4/16 15:40
     */
    public ParallelMulti getRootMulti() {
        // 从第一层开始构建
        MultiTree root = coordinate.get(0, 0);
        return root.getCurrentMulti();
    }

    /**
     * <h2>获的当前节点及其子节点构建的的ParallelMulti</h2>
     * 一个节点存在多少个子节点，那么他所构建的ParallelMulti就有多少个任务，执行完这些任务后会执行当前的任务
     *
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author liuwenhao
     * @date 2022/4/16 18:45
     */
    public ParallelMulti getCurrentMulti() {
        List<MultiTree> children = this.getChildren();
        ParallelMulti parallelMulti = ParallelMulti.of();
        if (children.isEmpty()) {
            // 当前是最后一个节点
            return parallelMulti.add(getSerialMulti());
        }
        // 不是最后一个节点
        for (MultiTree child : children) {
            parallelMulti.add(child.getCurrentMulti());
        }
        return parallelMulti;
    }

    /**
     * <h2>获的当前节点及其子节点构建的的ParallelMulti</h2>
     * 提供线程池
     *
     * @param executorService 线程池
     * @return com.deep.crow.task.parallel.ParallelMulti
     * @author liuwenhao
     * @date 2022/4/16 18:45
     */
    public ParallelMulti getCurrentMulti(ExecutorService executorService) {
        ParallelMulti currentMulti = getCurrentMulti();
        return currentMulti.setExecutorService(executorService);
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
        return x == multiTree.x && y == multiTree.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}