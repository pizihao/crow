package com.deep.crow.team;

import cn.hutool.core.lang.Tuple;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Create by liuwenhao on 2022/4/9 22:29
 */
@SuppressWarnings("unused")
public interface Multi<T> {

    /**
     * 采用异步的方式执行一个任务，并将改任务的结果传递给下一个Multi
     *
     * @param fn 任务
     * @return com.deep.crow.team.Multi<U>
     * @author Created by liuwenhao on 2022/4/9 23:33
     */
    <U> Multi<U> thenApply(Function<? super T, ? extends U> fn);

    /**
     * 任务执行的中间过程，接收上一个Multi的结果进行处理，本操作无返回信息
     *
     * @param action 任务
     * @return com.deep.crow.team.Multi<java.lang.Void>
     * @author Created by liuwenhao on 2022/4/9 23:36
     */
    Multi<Void> thenAccept(Consumer<? super T> action);

    /**
     * 执行一个中间任务，在不出现异常的情况下本过程不会对周围的过程产生任务影响
     *
     * @param action 任务
     * @return com.deep.crow.team.Multi<java.lang.Void>
     * @author Created by liuwenhao on 2022/4/9 23:39
     */
    Multi<Void> thenRun(Runnable action);

    /**
     * 接收一个Multi，将其结果和当前Multi的结果作为原料用来执行一个任务
     *
     * @param other 参数Multi
     * @param fn    任务
     * @return com.deep.crow.team.Multi<V>
     * @author Created by liuwenhao on 2022/4/9 23:42
     */
    <U, V> Multi<V> thenCombine(Multi<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn);

    /**
     * 接收一个Multi，将其结果和当前Multi的结果作为原料用来执行一个任务
     *
     * @param other  参数Multi
     * @param action 任务
     * @return com.deep.crow.team.Multi<java.lang.Void>
     * @author Created by liuwenhao on 2022/4/9 23:45
     */
    <U> Multi<Void> thenBiAccept(Multi<? extends U> other, BiConsumer<? super T, ? super U> action);

    /**
     * 接收一个Multi，当该Multi执行完成时执行后续的任务
     *
     * @param other  参数Multi
     * @param action 任务
     * @return com.deep.crow.team.Multi<java.lang.Void>
     * @author Created by liuwenhao on 2022/4/9 23:48
     */
    Multi<Void> runRunBoth(Multi<?> other, Runnable action);

    /**
     * 接收一个Multi，将其结果作为原料执行一个任务
     *
     * @param other 参数Multi
     * @param fn    任务
     * @return com.deep.crow.team.Multi<U>
     * @author Created by liuwenhao on 2022/4/9 23:50
     */
    <U> Multi<U> applyFun(Multi<? extends T> other, Function<? super T, U> fn);

    /**
     * 接收一个Multi，将其结果作为原料执行一个任务
     *
     * @param other  参数Multi
     * @param action 任务
     * @return com.deep.crow.team.Multi<java.lang.Void>
     * @author Created by liuwenhao on 2022/4/9 23:58
     */
    Multi<Void> acceptFun(Multi<? extends T> other, Consumer<? super T> action);

    /**
     * 接收一个Multi，其执行完成后执行一个任务
     *
     * @param other  参数Multi
     * @param action 任务
     * @return com.deep.crow.team.Multi<java.lang.Void>
     * @author Created by liuwenhao on 2022/4/10 0:02
     */
    Multi<Void> runFun(Multi<?> other, Runnable action);

    /**
     * 使用截止当前阶段时得到的参数执行一个任务，并产生一个结果
     *
     * @param fn 任务
     * @return com.deep.crow.team.Multi<U>
     * @author Created by liuwenhao on 2022/4/10 0:05
     */
    <U> Multi<U> thenCompose(Function<? super T, ? extends Multi<U>> fn);

    /**
     * 当产生异常时调用，处理异常并返回处理结果
     *
     * @param fn 任务
     * @return com.deep.crow.team.Multi<T>
     * @author Created by liuwenhao on 2022/4/10 0:17
     */
    Multi<T> exceptionally(Function<Throwable, ? extends T> fn);

    /**
     * 使用当前过程产生的结果和之前产生的异常信息为原料执行一个任务
     *
     * @param action 任务
     * @return com.deep.crow.team.Multi<T>
     * @author Created by liuwenhao on 2022/4/10 0:12
     */
    Multi<T> whenComplete(BiConsumer<? super T, ? super Throwable> action);

    /**
     * 使用当前过程产生的结果和之前产生的异常信息为原料执行一个任务，并产生一个结果
     *
     * @param fn 任务
     * @return com.deep.crow.team.Multi<U>
     * @author Created by liuwenhao on 2022/4/10 0:14
     */
    <U> Multi<U> handle(BiFunction<? super T, Throwable, ? extends U> fn);

    /**
     * 按照参数的顺序执行，同时返回结果，类似于 Collection.stream().map()
     * 如果Multi的返回结果为null,则转化后对应位置的值依然为null
     *
     * @param c 过程集合
     * @return com.deep.crow.team.Multi<java.lang.Void>
     * @author Created by liuwenhao on 2022/4/9 22:51
     */
    List<Object> multipleList(List<Multi<?>> c);

    /**
     * 按照参数的顺序执行，同时返回结果，类似于 Collection.stream().map()
     * 如果Multi的返回结果为null,则转化后对应位置的值依然为null
     *
     * @param c 过程集合
     * @return Tuple
     * @author Created by liuwenhao on 2022/4/9 22:51
     */
    Tuple multipleTuple(List<Multi<?>> c);

    /**
     * 转化成CompletableFuture
     *
     * @return java.util.concurrent.CompletableFuture<T>
     * @author Created by liuwenhao on 2022/4/9 23:54
     */
    CompletableFuture<T> getCpf();

    /**
     * 获取结果
     *
     * @return T 结果
     * @author Created by liuwenhao on 2022/4/10 0:42
     */
    T get();
}
