package com.deep.crow.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * <h2>坐标</h2>
 *
 * @author Create by liuwenhao on 2022/4/13 16:12
 */
public interface Coordinate<T> {

    /**
     * <h2>判断某个坐标是否已经存在数据</h2>
     *
     * @param x x轴
     * @param y y轴
     * @return boolean
     * @author liuwenhao
     * @date 2022/4/13 16:17
     */
    boolean contains(int x, int y);

    /**
     * <h2>判断某个横坐标是否已经存在数据</h2>
     *
     * @param x x轴
     * @return boolean
     * @author liuwenhao
     * @date 2022/4/13 16:20
     */
    boolean containsX(int x);

    /**
     * <h2>判断某个纵坐标是否已经存在数据</h2>
     *
     * @param y y轴
     * @return boolean
     * @author liuwenhao
     * @date 2022/4/13 16:20
     */
    boolean containsY(int y);

    /**
     * <h2>判断某个value是否已经存在数据</h2>
     *
     * @param value value
     * @return boolean
     * @author liuwenhao
     * @date 2022/4/13 16:20
     */
    boolean containsValue(T value);

    /**
     * <h2>获取一个坐标系中的值</h2>
     *
     * @param x 横坐标
     * @param y 纵坐标
     * @return T
     * @author liuwenhao
     * @date 2022/4/13 16:25
     */
    T get(int x, int y);

    /**
     * <h2>坐标系是否为空</h2>
     *
     * @return boolean
     * @author liuwenhao
     * @date 2022/4/13 16:26
     */
    boolean isEmpty();

    /**
     * <h2>已存在值的坐标数</h2>
     *
     * @return int
     * @author liuwenhao
     * @date 2022/4/13 16:28
     */
    int size();

    /**
     * <h2>清空坐标系</h2>
     *
     * @author liuwenhao
     * @date 2022/4/13 16:30
     */
    void clear();

    /**
     * <h2>添加一组映射，覆盖</h2>
     *
     * @param x     横坐标
     * @param y     纵坐标
     * @param value 数值
     * @return T 该坐标上一个映射的值，没有则为null
     * @author liuwenhao
     * @date 2022/4/13 16:31
     */
    T put(int x, int y, T value);

    /**
     * <h2>批量添加，覆盖添加</h2>
     *
     * @param coordinate 坐标系
     * @author liuwenhao
     * @date 2022/4/13 16:32
     */
    void putAll(Coordinate<T> coordinate);

    /**
     * <h2>断开某个坐标的映射</h2>
     *
     * @param x 横坐标
     * @param y 纵坐标
     * @return T 删除前的映射值
     * @author liuwenhao
     * @date 2022/4/13 18:20
     */
    T remove(int x, int y);

    /**
     * <h2>获取横坐标的投影</h2>
     * 在转化时会将横坐标转化为其包装类型
     *
     * @param y 纵坐标的投影线
     * @return java.util.Map<java.lang.Integer, T>
     * @author liuwenhao
     * @date 2022/4/13 18:25
     */
    Map<Integer, T> x(int y);

    /**
     * <h2>获取纵坐标的投影</h2>
     * 在转化时会将纵坐标转化为其包装类型
     *
     * @param x 横坐标的投影线
     * @return java.util.Map<java.lang.Integer, T>
     * @author liuwenhao
     * @date 2022/4/13 18:25
     */
    Map<Integer, T> y(int x);

    /**
     * <h2>值和坐标的组合</h2>
     * 对结果的操作会直接影响到原坐标
     *
     * @return java.util.Set<com.deep.crow.util.Coordinate.Element < T>>
     * @author liuwenhao
     * @date 2022/4/13 18:32
     */
    Set<Element<T>> elementSet();

    /**
     * <h2>值和横坐标的组合</h2>
     * 对结果的操作会直接影响到原坐标
     *
     * @return java.util.Set<com.deep.crow.util.Coordinate.Element < T>>
     * @author liuwenhao
     * @date 2022/4/13 18:32
     */
    Set<T> xSet();

    /**
     * <h2>值和纵坐标的组合</h2>
     * 对结果的操作会直接影响到原坐标
     *
     * @return java.util.Set<com.deep.crow.util.Coordinate.Element < T>>
     * @author liuwenhao
     * @date 2022/4/13 18:32
     */
    Set<T> ySet();

    /**
     * <h2>值的集合</h2>
     * 对结果的操作会直接影响到原值
     *
     * @return java.util.Set<com.deep.crow.util.Coordinate.Element < T>>
     * @author liuwenhao
     * @date 2022/4/13 18:32
     */
    Collection<T> values();

    /**
     * <h2>返回[x,[y,T]]映射表</h2>
     *
     * @return java.util.Map<java.lang.Integer, java.util.Map < java.lang.Integer, T>>
     * @author liuwenhao
     * @date 2022/4/13 18:36
     */
    Map<Integer, Map<Integer, T>> xMap();

    /**
     * <h2>返回[y,[x,T]]映射表</h2>
     *
     * @return java.util.Map<java.lang.Integer, java.util.Map < java.lang.Integer, T>>
     * @author liuwenhao
     * @date 2022/4/13 18:36
     */
    Map<Integer, Map<Integer, T>> yMap();

    /**
     * x，y和value的映射
     */
    interface Element<T> {

        /**
         * <h2>X轴坐标</h2>
         *
         * @return int int
         * @author liuwenhao
         * @date 2022/4/13 16:16
         */
        int getX();

        /**
         * <h2>Y轴坐标</h2>
         *
         * @return int int
         * @author liuwenhao
         * @date 2022/4/13 16:16
         */
        int getY();

        /**
         * <h2>value</h2>
         *
         * @return T value
         * @author liuwenhao
         * @date 2022/4/13 16:16
         */
        T getValue();

    }
}
