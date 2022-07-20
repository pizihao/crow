package com.deep.crow.completable;

import com.deep.crow.util.Tuple;

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
 * @deprecated {@link com.deep.crow.multi.Multi}
 * @author Create by liuwenhao on 2021/11/26 16:07
 */
@Deprecated
@SuppressWarnings("unused deprecated")
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
    public static Cof<Runnable, Integer> buildRunBlock(ExecutorService e) {
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
    public static Cof<Runnable, Integer> buildRunBlock(List<CofTask<Runnable>> r, ExecutorService e) {
        return new CofRunBlock(r, e);
    }

    /**
     * <h2>创建一个{@link CofRunAsync}</h2>
     *
     * @return cn.net.nova.trm.util.test.Com<java.lang.Runnable, java.lang.Integer>
     * @author liuwenhao
     * @date 2021/11/26 16:14
     */
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
    public static Cof<Runnable, Integer> buildRunAsync(ExecutorService e) {
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
    public static Cof<Runnable, Integer> buildRunAsync(List<CofTask<Runnable>> r, ExecutorService e) {
        return new CofRunAsync(r, e);
    }

    /**
     * <h2>创建一个{@link CofSupplyMap}</h2>
     *
     * @return cn.net.nova.trm.util.test.Com<java.util.function.Supplier < java.lang.Object>,java.util.Map<java.lang.String,java.lang.Object>>
     * @author liuwenhao
     * @date 2021/11/26 16:14
     */
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
    public static Cof<Supplier<Object>, Map<String, Object>> buildSupplyMap(ExecutorService e) {
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
    public static Cof<Supplier<Object>, Map<String, Object>> buildSupplyMap(List<CofTask<Supplier<Object>>> r, ExecutorService e) {
        return new CofSupplyMap(r, e);
    }

    /**
     * <h2>创建一个{@link CofSupplyTuple}</h2>
     *
     * @return cn.net.nova.trm.util.test.Com<java.util.function.Supplier < java.lang.Object>,cn.hutool.core.lang.Tuple>
     * @author liuwenhao
     * @date 2021/11/26 16:14
     */
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
    public static Cof<Supplier<Object>, Tuple> buildSupplyTuple(ExecutorService e) {
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
    public static Cof<Supplier<Object>, Tuple> buildSupplyTuple(List<CofTask<Supplier<Object>>> r, ExecutorService e) {
        return new CofSupplyTuple(r, e);
    }
}