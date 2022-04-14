package com.deep.crow.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * <h2>坐标系</h2>
 * 通过两个整型来确定唯一对象，对于一个单纯的坐标系来说只是一个x轴到y轴的多重映射，给定一个x会得到一组
 * y，比如函数y=x，在坐标系上会得到一条直线，但是现在对于其中的x或者y，赋予映射的含义：在一个整数坐标
 * 系中，找到一个点，改点存在一个对象，将x轴向y轴投影，会得到一组对象的集合，反之相同
 * <hr>
 * =================================================================
 * <hr>
 * {@link Coordinate}在某些方面和{@link Map}是非常相似的，所以投影是依据{@link Map}来实现的
 * 当需要通过x轴或者y轴来建立投影时，会丢失重复的部分，比如一个坐标中存在(1,2)和(1,3)，两个点
 * 则将这个坐标系中的坐标向y轴投影时得到的是(1,3)，遍历的顺序依据与其hash的顺序，3将2覆盖
 * <hr>
 * =================================================================
 * <hr>
 * 在投影等得到视图的方法应该使用一个全新的对象，视图对象和原本的左边对象不应存在互相影响的关系，如：
 * <pre>
 *     MapCoordinate<User> coordinate = MapCoordinate.create();
 *     coordinate.put(1, 1, new User("11"));
 *     coordinate.put(1, 2, new User("12"));
 *     coordinate.put(1, 3, new User("13"));
 *     coordinate.put(5, 3, new User("53"));
 *
 *     Set<Coordinate.Element<User>> elements = coordinate.elementSet();
 *     elements.add(new MapCoordinate.MapElement<>(10, 12, new User("1012")));
 *
 *     elements.forEach(System.out::println);
 *     // (1,1)-->User{name='11'}
 *     // (1,2)-->User{name='12'}
 *     // (1,3)-->User{name='13'}
 *     // (10,12)-->User{name='1012'}
 *     // (5,3)-->User{name='53'}
 *     System.out.println("===============");
 *     coordinate.elementSet().forEach(System.out::println);
 *     // (1,1)-->User{name='11'}
 *     // (1,2)-->User{name='12'}
 *     // (1,3)-->User{name='13'}
 *     // (5,3)-->User{name='53'}
 * </pre>
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
     * 在转化时会将横坐标转化为其包装类型<br>
     * 投影时会失去重合的部分
     *
     * @param y 纵坐标的投影线
     * @return java.util.Map<java.lang.Integer, T>
     * @author liuwenhao
     * @date 2022/4/13 18:25
     */
    Map<Integer, T> x(int y);

    /**
     * <h2>获取纵坐标的投影</h2>
     * 在转化时会将纵坐标转化为其包装类型<br>
     * 投影时会失去重合的部分
     *
     * @param x 横坐标的投影线
     * @return java.util.Map<java.lang.Integer, T>
     * @author liuwenhao
     * @date 2022/4/13 18:25
     */
    Map<Integer, T> y(int x);

    /**
     * <h2>值和坐标的组合</h2>
     *
     * @return java.util.Set<com.deep.crow.util.Coordinate.Element < T>>
     * @author liuwenhao
     * @date 2022/4/13 18:32
     */
    Set<Element<T>> elementSet();

    /**
     * <h2>横坐标的组合</h2>
     *
     * @return java.util.Set<com.deep.crow.util.Coordinate.Element < T>>
     * @author liuwenhao
     * @date 2022/4/13 18:32
     */
    Set<Integer> xSet();

    /**
     * <h2>纵坐标的组合</h2>
     *
     * @return java.util.Set<com.deep.crow.util.Coordinate.Element < T>>
     * @author liuwenhao
     * @date 2022/4/13 18:32
     */
    Set<Integer> ySet();

    /**
     * <h2>值的集合</h2>
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
     * 相对于{@link #xMap()}来说是一个关于x轴对称的坐标系
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

        /**
         * <h2>toString</h2>
         *
         * @return java.lang.String
         * @author liuwenhao
         * @date 2022/4/14 13:14
         */
        @Override
        String toString();

    }
}
