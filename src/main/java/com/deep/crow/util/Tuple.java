package com.deep.crow.util;

import com.deep.crow.multi.Multi;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 元组 用于存放各个并行的{@link Multi}的结果
 *
 * @author Create by liuwenhao on 2022/4/11 17:48
 */
public class Tuple implements Iterable<Object> {

  private final Object[] members;

  public Tuple(Object... members) {
    this.members = members;
  }

  /**
   * 获取指定位置元素
   *
   * @param index 位置索引
   * @return T
   * @author liuwenhao
   * @date 2022/4/11 17:50
   */
  @SuppressWarnings("unchecked")
  public <T> T get(int index) {
    return (T) members[index];
  }

  /**
   * 获取所有元素
   *
   * @return Object[]
   * @author liuwenhao
   * @date 2022/4/11 17:50
   */
  public Object[] getMembers() {
    return this.members;
  }

  /**
   * 将元组转换成List
   *
   * @return List
   * @author liuwenhao
   * @date 2022/4/11 17:50
   */
  public final List<Object> toList() {
    synchronized (members) {
      return Arrays.stream(this.members).collect(Collectors.toList());
    }
  }

  /**
   * size
   *
   * @return int
   * @author liuwenhao
   * @date 2022/4/11 17:50
   */
  public int size() {
    Objects.requireNonNull(getMembers());
    return members.length;
  }

  /**
   * 判断某元素是否存在
   *
   * @return List
   * @author liuwenhao
   * @date 2022/4/11 17:50
   */
  public boolean contains(Object value) {
    Objects.requireNonNull(getMembers());
    return toList().contains(value);
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + Arrays.deepHashCode(getMembers());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Tuple other = (Tuple) obj;
    return Arrays.deepEquals(getMembers(), other.getMembers());
  }

  @Override
  public String toString() {
    return Arrays.toString(getMembers());
  }

  @Override
  @SuppressWarnings("all")
  public Iterator<Object> iterator() {
    return new TupleIter(getMembers());
  }

  @Override
  public final Spliterator<Object> spliterator() {
    return Spliterators.spliterator(getMembers(), Spliterator.ORDERED);
  }

  public static class TupleIter<T> implements Iterable<T>, Iterator<T> {

    /** 数组 */
    private final Object array;

    /** 结束位置 */
    private final int endIndex;
    /** 当前位置 */
    private int index;

    public TupleIter(Object array) {
      this.endIndex = Array.getLength(array);
      this.array = array;
      this.index = 0;
    }

    @Override
    public Iterator<T> iterator() {
      return new TupleIter<>(array);
    }

    @Override
    public boolean hasNext() {
      return (index < endIndex);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      return (T) Array.get(array, index++);
    }
  }
}
