package com.deep.crow.task.parallel;

import com.deep.crow.multi.Multi;

import java.util.Set;

/**
 * <h2>并行执行过程索引重复策略</h2>
 *
 * @author Create by liuwenhao on 2022/4/11 17:15
 */
public interface IndexRepeat {

    /**
     * 执行策略，得到一个可以使用的MultiOrder
     *
     * @param order 需要检验的索引
     * @param index 已存在的索引
     * @return 可以使用的MultiOrder
     */
    MultiOrder<?> get(Set<Integer> index, int order, Multi<?> multi);

}
