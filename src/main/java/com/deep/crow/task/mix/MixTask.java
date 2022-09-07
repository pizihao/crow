package com.deep.crow.task.mix;

import com.deep.crow.multi.Multi;
import com.deep.crow.task.Task;
import java.util.Set;

/**
 * 混合任务节点
 *
 * @author Create by liuwenhao on 2022/6/17 19:07
 */
interface MixTask<T> extends Task {

  /**
   * 获取任务的前置任务的标识
   *
   * @return Set<String>
   * @author liuwenhao
   * @date 2022/6/17 19:10
   */
  Set<String> pre();

  /**
   * 添加一个前置任务
   *
   * @param preName 前置任务标识
   * @author liuwenhao
   * @date 2022/6/18 16:58
   */
  void addPreName(String preName);

  /**
   * 删除前置任务
   *
   * @param preName 前置任务标识
   * @author liuwenhao
   * @date 2022/6/18 16:59
   */
  void removePreName(String preName);

  /**
   * 是否是尾结点
   *
   * @return boolean
   * @author liuwenhao
   * @date 2022/6/18 17:10
   */
  @Deprecated
  boolean isTail();

  /**
   * 声明当前节点不再是尾结点
   *
   * @author liuwenhao
   * @date 2022/6/18 17:11
   */
  void cancelTail();

  /**
   * 获取任务名称
   *
   * @return java.lang.String
   * @author liuwenhao
   * @date 2022/6/18 15:58
   */
  String name();

  /**
   * 判断任务是否完成 如果强制则使用join 如果非强制则使用getNow
   *
   * @param force 是否强制
   * @return T
   * @author liuwenhao
   * @date 2022/6/17 19:10
   */
  boolean complete(boolean force);

  /**
   * 获得任务
   *
   * @return com.deep.crow.multi.Multi<T>
   * @author liuwenhao
   * @date 2022/6/22 18:00
   */
  Multi<T> multi();
}
