package com.deep.crow.completable;

import com.deep.crow.util.Tuple;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * <h2>基于{@link CompletableFuture}的扩展器</h2>
 * <p>其实现类应提供对应的容器存放需要执行的任务，任务的形式支持{@link CompletableFuture}所提供的几种形式：</p>
 * <ul>
 *     <li>{@link Supplier}</li>
 *     <li>{@link Runnable}</li>
 * </ul>
 * <p>本类的实现者可根据需求使用{@link CompletableFuture#get()}或者{@link CompletableFuture#join()}方法获取结果</p>
 * <p>注意执行器的适用范围，针对整个容器，针对一个任务或是不使用执行器</p>
 * <p>说明：</p>
 * <ul>
 *     <li>S: 任务所属类型，如：{@link Supplier}或{@link Runnable}</li>
 *     <li>R: 容器最终返回值类型，如：{@link Map}或{@link Tuple}</li>
 * </ul>
 *
 * @author Create by liuwenhao on 2021/11/26 10:08
 */
@SuppressWarnings("unused")
public interface Cof<S, R> {

    /**
     * <h2>执行容器中的任务</h2>
     *
     * @return R
     * @author liuwenhao
     * @date 2021/11/26 10:15
     */
    default R exec() {
        return exec(s -> true);
    }

    /**
     * <h2>执行容器中的任务</h2>
     *
     * @param p 过滤条件
     * @return R
     * @author liuwenhao
     * @date 2021/11/26 10:15
     */
    R exec(Predicate<CofTask<S>> p);

    // =============================任务注册器==================================

    /**
     * <h2>注册一个任务</h2>
     *
     * @param e 任务实体
     * @return cn.net.nova.trm.util.test.Com<R>
     * @author liuwenhao
     * @date 2021/11/26 11:10
     */
    default Cof<S, R> register(CofTask<S> e) {
        throw new UnsupportedOperationException();
    }

    /**
     * <h2>注册一批任务</h2>
     *
     * @param e 任务实体
     * @return cn.net.nova.trm.util.test.Com<R>
     * @author liuwenhao
     * @date 2021/11/26 11:10
     */
    default Cof<S, R> registers(List<CofTask<S>> e) {
        throw new UnsupportedOperationException();
    }

    /**
     * <h2>注册一个任务，仅适用于{@link CofRun}</h2>
     *
     * @param r 任务
     * @return cn.net.nova.trm.util.test.Com<S, R>
     * @author liuwenhao
     * @date 2021/11/26 15:44
     */
    default Cof<S, R> register(Runnable r) {
        throw new UnsupportedOperationException();
    }

    /**
     * <h2>注册一个任务，仅适用于{@link CofRun}</h2>
     * <p>执行时使用任务自带的执行器执行</p>
     *
     * @param r 任务
     * @param e 执行器
     * @return cn.net.nova.trm.util.test.Com<S, R>
     * @author liuwenhao
     * @date 2021/11/26 15:44
     */
    default Cof<S, R> register(Runnable r, ExecutorService e) {
        throw new UnsupportedOperationException();
    }

    /**
     * <h2>注册一个任务，仅适用于{@link CofSupplyMap}</h2>
     * <p>执行时使用任务自带的执行器执行</p>
     *
     * @param s 任务
     * @param e 执行器
     * @param n 名，唯一标识
     * @return cn.net.nova.trm.util.test.Com<S, R>
     * @author liuwenhao
     * @date 2021/11/26 15:44
     */
    default Cof<S, R> register(Supplier<Object> s, ExecutorService e, String n) {
        throw new UnsupportedOperationException();
    }

    /**
     * <h2>注册一个任务，仅适用于{@link CofSupplyMap}</h2>
     *
     * @param s 任务
     * @param n 名，唯一标识
     * @return cn.net.nova.trm.util.test.Com<S, R>
     * @author liuwenhao
     * @date 2021/11/26 15:44
     */
    default Cof<S, R> register(Supplier<Object> s, String n) {
        throw new UnsupportedOperationException();
    }

    /**
     * <h2>注册一个任务，仅适用于{@link CofSupplyTuple}</h2>
     * <p>执行时使用任务自带的执行器执行</p>
     *
     * @param s 任务
     * @param e 执行器
     * @return cn.net.nova.trm.util.test.Com<S, R>
     * @author liuwenhao
     * @date 2021/11/26 15:44
     */
    default Cof<S, R> register(Supplier<Object> s, ExecutorService e) {
        throw new UnsupportedOperationException();
    }

    /**
     * <h2>注册一个任务，仅适用于{@link CofSupplyTuple}</h2>
     *
     * @param s 任务
     * @return cn.net.nova.trm.util.test.Com<S, R>
     * @author liuwenhao
     * @date 2021/11/26 15:44
     */
    default Cof<S, R> register(Supplier<Object> s) {
        throw new UnsupportedOperationException();
    }
}