package com.deep.crow.completable;

import cn.hutool.core.lang.Tuple;
import com.sun.istack.internal.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

/**
 * <h2>构建Cof扩展器的工具类</h2>
 * 建立Cof容器的工具类，详情可见：
 * <ul>
 *     <li>{@link CofSupplyTuple}</li>
 *     <li>{@link CofSupplyMap}</li>
 *     <li>{@link CofRunAsync}</li>
 *     <li>{@link CofRunBlock}</li>
 * </ul>
 *
 * @author Create by liuwenhao on 2021/11/26 16:07
 */
@SuppressWarnings("unused")
public class CofHelper {
    private CofHelper() {
    }

    /**
     * <h2>创建一个{@link CofRunBlock}</h2>
     *
     * @return cn.net.nova.trm.util.test.Com<java.lang.Runnable, java.lang.Integer>
     * @author liuwenhao
     * @date 2021/11/26 16:14
     */
    @NotNull
    public static Cof<Runnable, Integer> buildRunBlock() {
        return new CofRunBlock();
    }

    /**
     * <h2>创建一个{@link CofRunBlock}</h2>
     *
     * @param e 执行器
     * @return cn.net.nova.trm.util.test.Com<java.lang.Runnable, java.lang.Integer>
     * @author liuwenhao
     * @date 2021/11/26 16:14
     */
    @NotNull
    public static Cof<Runnable, Integer> buildRunBlock(@NotNull ExecutorService e) {
        return new CofRunBlock(e);
    }

    /**
     * <h2>创建一个{@link CofRunBlock}</h2>
     *
     * @param r 任务集合
     * @param e 执行器
     * @return cn.net.nova.trm.util.test.Com<java.lang.Runnable, java.lang.Integer>
     * @author liuwenhao
     * @date 2021/11/26 16:14
     */
    @NotNull
    public static Cof<Runnable, Integer> buildRunBlock(@NotNull List<CofTask<Runnable>> r, @NotNull ExecutorService e) {
        return new CofRunBlock(r, e);
    }

    /**
     * <h2>创建一个{@link CofRunAsync}</h2>
     *
     * @return cn.net.nova.trm.util.test.Com<java.lang.Runnable, java.lang.Integer>
     * @author liuwenhao
     * @date 2021/11/26 16:14
     */
    @NotNull
    public static Cof<Runnable, Integer> buildRunAsync() {
        return new CofRunAsync();
    }

    /**
     * <h2>创建一个{@link CofRunAsync}</h2>
     *
     * @param e 执行器
     * @return cn.net.nova.trm.util.test.Com<java.lang.Runnable, java.lang.Integer>
     * @author liuwenhao
     * @date 2021/11/26 16:14
     */
    @NotNull
    public static Cof<Runnable, Integer> buildRunAsync(@NotNull ExecutorService e) {
        return new CofRunAsync(e);
    }

    /**
     * <h2>创建一个{@link CofRunAsync}</h2>
     *
     * @param r 任务集合
     * @param e 执行器
     * @return cn.net.nova.trm.util.test.Com<java.lang.Runnable, java.lang.Integer>
     * @author liuwenhao
     * @date 2021/11/26 16:14
     */
    @NotNull
    public static Cof<Runnable, Integer> buildRunAsync(@NotNull List<CofTask<Runnable>> r, @NotNull ExecutorService e) {
        return new CofRunAsync(r, e);
    }

    /**
     * <h2>创建一个{@link CofSupplyMap}</h2>
     *
     * @return cn.net.nova.trm.util.test.Com<java.util.function.Supplier < java.lang.Object>,java.util.Map<java.lang.String,java.lang.Object>>
     * @author liuwenhao
     * @date 2021/11/26 16:14
     */
    @NotNull
    public static Cof<Supplier<Object>, Map<String, Object>> buildSupplyMap() {
        return new CofSupplyMap();
    }

    /**
     * <h2>创建一个{@link CofSupplyMap}</h2>
     *
     * @param e 执行器
     * @return cn.net.nova.trm.util.test.Com<java.util.function.Supplier < java.lang.Object>,java.util.Map<java.lang.String,java.lang.Object>>
     * @author liuwenhao
     * @date 2021/11/26 16:14
     */
    @NotNull
    public static Cof<Supplier<Object>, Map<String, Object>> buildSupplyMap(@NotNull ExecutorService e) {
        return new CofSupplyMap(e);
    }


    /**
     * <h2>创建一个{@link CofSupplyMap}</h2>
     *
     * @param r 任务集合
     * @param e 执行器
     * @return cn.net.nova.trm.util.test.Com<java.util.function.Supplier < java.lang.Object>,java.util.Map<java.lang.String,java.lang.Object>>
     * @author liuwenhao
     * @date 2021/11/26 16:14
     */
    @NotNull
    public static Cof<Supplier<Object>, Map<String, Object>> buildSupplyMap(@NotNull List<CofTask<Supplier<Object>>> r, @NotNull ExecutorService e) {
        return new CofSupplyMap(r, e);
    }

    /**
     * <h2>创建一个{@link CofSupplyTuple}</h2>
     *
     * @return cn.net.nova.trm.util.test.Com<java.util.function.Supplier < java.lang.Object>,cn.hutool.core.lang.Tuple>
     * @author liuwenhao
     * @date 2021/11/26 16:14
     */
    @NotNull
    public static Cof<Supplier<Object>, Tuple> buildSupplyTuple() {
        return new CofSupplyTuple();
    }

    /**
     * <h2>创建一个{@link CofSupplyTuple}</h2>
     *
     * @param e 执行器
     * @return cn.net.nova.trm.util.test.Com<java.util.function.Supplier < java.lang.Object>,cn.hutool.core.lang.Tuple>
     * @author liuwenhao
     * @date 2021/11/26 16:14
     */
    @NotNull
    public static Cof<Supplier<Object>, Tuple> buildSupplyTuple(@NotNull ExecutorService e) {
        return new CofSupplyTuple(e);
    }


    /**
     * <h2>创建一个{@link CofSupplyTuple}</h2>
     *
     * @param r 任务集合
     * @param e 执行器
     * @return cn.net.nova.trm.util.test.Com<java.util.function.Supplier < java.lang.Object>,cn.hutool.core.lang.Tuple>
     * @author liuwenhao
     * @date 2021/11/26 16:14
     */
    @NotNull
    public static Cof<Supplier<Object>, Tuple> buildSupplyTuple(@NotNull List<CofTask<Supplier<Object>>> r, @NotNull ExecutorService e) {
        return new CofSupplyTuple(r, e);
    }
}